package com.example.SOS;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoogleSignInPage extends AppCompatActivity {
    private static final int RC_SIGN_IN = 4290;
    private static final String TAG = "GoogleSignInPage";
    private GoogleSignInClient gsic;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_sign_in_page);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("Restart");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("Restart");
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Fitness.SCOPE_BODY_READ)
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        gsic = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        if(newString != null && newString.equals("Restart")){
            signIn();
        }

    }

    private void updateUI(GoogleSignInAccount account) {
        if(account == null){
        }
        else{
            //Get google fit data
            //Needs to be run asynchronously
            FitnessRetriever.googleFitAsync(executorService,this, DataType.TYPE_WEIGHT, "weight");
            FitnessRetriever.googleFitAsync(executorService,this, DataType.TYPE_HEIGHT, "height");

            //Start up the SetInfo
            Intent intent = new Intent(this, SetInfo.class);
            intent.putExtra("Restart", newString);
            startActivity(intent);
        }
    }

    public void signIn(){
        Intent signInIntent = gsic.getSignInIntent();
        //When finished goes to onActivityResult
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            updateUI(null);
        }
    }

}

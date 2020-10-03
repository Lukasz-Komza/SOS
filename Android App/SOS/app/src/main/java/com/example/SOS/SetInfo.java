package com.example.SOS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.text.DateFormat.getDateInstance;

public class SetInfo extends AppCompatActivity {


    private static final String TAG = "SetInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_info);

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        //Create a checkbox object
        CheckBox c1 = (CheckBox) findViewById(R.id.LocationBox);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Is the view now checked?
                boolean checked = ((CheckBox) view).isChecked();
                LocalFileRetriever.storeToMap("dataMap", "share_loc",Boolean.toString(checked), view.getContext());
            }
        } );
        c1.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_loc"));


        CheckBox c2 = (CheckBox) findViewById(R.id.HealthBox);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Store that it is checked
                LocalFileRetriever.storeToMap("dataMap", "share_health","true", view.getContext());
                //Hide the checkbox
                view.setVisibility(View.GONE);
                //Open the google sign in page
                Intent intent = new Intent(view.getContext(), GoogleSignInPage.class);
                startActivity(intent);
            }
        } );
        c2.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_loc"));


        //Create EditText object to accept user input
        EditText nameText = (EditText) findViewById(R.id.nameText);
        nameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Get the text they entered and store it as name
                    LocalFileRetriever.storeToMap("dataMap", "name", v.getText().toString(), v.getContext());

                    //Set the background to dark
                    v.setBackgroundResource(R.drawable.text_boxless_dark);
                    return true;
                }
                return false;

            }
        });
        nameText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_hint_name"));
        //Set the content if the user has already filled this field out
        String text = LocalFileRetriever.retrieveMap("dataMap", this).get("name");
        if(!isNullOrEmpty(text)){
            nameText.setText(text);
            nameText.setBackgroundResource(R.drawable.text_boxless_dark);
        }


        //Create EditText object to accept user input
        EditText genderText = (EditText) findViewById(R.id.genderText);
        genderText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Get the text they entered and store it as gender
                    LocalFileRetriever.storeToMap("dataMap", "gender", v.getText().toString(), v.getContext());

                    //Set the background to dark
                    v.setBackgroundResource(R.drawable.text_boxless_dark);
                    return true;
                }
                return false;

            }
        });
        genderText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_hint_gender"));
        //Set the content if the user has already filled this field out
        text = LocalFileRetriever.retrieveMap("dataMap", this).get("gender");
        if(!isNullOrEmpty(text)){
            genderText.setText(text);
            genderText.setBackgroundResource(R.drawable.text_boxless_dark);
        }


        //Create EditText object to accept user input
        EditText ageText = (EditText) findViewById(R.id.ageText);
        ageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Get the text they entered and store it as name
                    LocalFileRetriever.storeToMap("dataMap", "age", v.getText().toString(), v.getContext());

                    //Set the background to dark
                    v.setBackgroundResource(R.drawable.text_boxless_dark);
                    return true;
                }
                return false;

            }
        });
        ageText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_hint_age"));
        //Set the content if the user has already filled this field out
        text = LocalFileRetriever.retrieveMap("dataMap", this).get("age");
        if(!isNullOrEmpty(text)){
            ageText.setText(text);
            ageText.setBackgroundResource(R.drawable.text_boxless_dark);
        }

        //Set up the map for the following data points
        Map<String, String> healthMap = LocalFileRetriever.retrieveMap("healthMap",this);

        //Create EditText object to accept user input
        EditText heightText = (EditText) findViewById(R.id.heightText);
        heightText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Get the text they entered and store it as name
                    LocalFileRetriever.storeToMap("healthMap", "height", v.getText().toString(), v.getContext());

                    //Set the background to dark
                    v.setBackgroundResource(R.drawable.text_boxless_dark);
                    return true;
                }
                return false;

            }
        });
        heightText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_hint_height"));
        //Set the content if the user has already filled this field out
        if(healthMap != null && healthMap.get("height") != null){
            heightText.setText(healthMap.get("height"));
            heightText.setBackgroundResource(R.drawable.text_boxless_dark);
        }


        //Create EditText object to accept user input
        EditText weightText = (EditText) findViewById(R.id.weightText);
        weightText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Get the text they entered and store it as name
                    LocalFileRetriever.storeToMap("healthMap", "weight", v.getText().toString(), v.getContext());

                    //Set the background to dark
                    v.setBackgroundResource(R.drawable.text_boxless_dark);
                    return true;
                }
                return false;

            }
        });
        weightText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_hint_weight"));
        //Set the content if the user has already filled this field out
        if(healthMap != null && healthMap.get("height") != null){
            heightText.setText(healthMap.get("height"));
            heightText.setBackgroundResource(R.drawable.text_boxless_dark);
        }


        //Calls nextPage() on button click
        final Button button = findViewById(R.id.NextButton2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((SetInfo) v.getContext()).nextPage();
            }
        });
        button.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_submit"));
    }

    public void nextPage() {
        //Enables location
        if (!isNullOrEmpty(LocalFileRetriever.retrieveMap("dataMap",this).get("share_loc")) && LocalFileRetriever.retrieveMap("dataMap",this).get("share_loc").equals("true")) {
            statusCheck();
            //A client which can be used to get the location
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            //Check if the permissions are now appropriate
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Creates a client which can fetch location
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    //Return to set info to store
                                    SetInfo a = (SetInfo) getApplicationContext();
                                    a.interpretLocation(location);
                                }
                            }
                        });
            }
        }

        //Goes to the menu page
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);


    }
    public void interpretLocation(Location location){
        //Convert these to address form
        String lat = Double.toString(location.getLatitude());
        String lon = Double.toString(location.getLongitude());

        Map<String,String> map = LocalFileRetriever.retrieveMap("dataMap", this);
        map.put("lat", lat);
        map.put("lon", lon);
        LocalFileRetriever.storeMap("dataMap", map, this);

        try{
            StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
            StrictMode.setThreadPolicy(tp);

            Map<String, String> addressMap = ReverseGeocoder.ReverseGeocode(lat, lon);
            LocalFileRetriever.storeMap("locMap", addressMap,this);

            map.put("country_code", addressMap.get("\"country_code\""));
            LocalFileRetriever.storeMap("dataMap", map,this);
        }
        catch(Exception e){
        }

    }

    //Checks if the location services are enables
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    //Creates dialogue box to prompt location services
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void googleFitSync(){
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        //Log these dates, not sure why
        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        //Create a request to read the data of the scopes we want
        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .read(DataType.TYPE_WEIGHT)
                        .build();

        //read in the datasets from the time range we want
        Task<DataReadResponse> response = Fitness
                .getHistoryClient(this, GoogleSignIn.getAccountForScopes(this, Fitness.SCOPE_BODY_READ))
                .readData(readRequest);
        DataReadResponse readDataResponse = null;
        try {
            readDataResponse = Tasks.await(response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataSet> dataSets = readDataResponse.getDataSets();

        //Parse through the datasets, store the data in memory
        Map<String, String> healthMap = new HashMap<>();
        for(DataSet ds : dataSets){
            for(DataPoint dp: ds.getDataPoints()){
                for (Field f : dp.getDataType().getFields()){
                    healthMap.put(f.getName(), dp.getValue(f).toString());
                }
            }
        }
        LocalFileRetriever.storeMap("healthMap", healthMap,this);
    }

}
package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AddMedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_media);

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        //Set the user id for this session
        int id = 1111;
        MessageSender.setID(id);

        //Set the imageNum to 1
        int imageNum = 1;
        MessageSender.setImageNum(1);

        //Set up the director path that lukasz wants
        String dirPath = "/Media/";
        MessageSender.setDirPath(dirPath);

        //Listen for the add image button
        final Button imageButton = findViewById(R.id.addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((AddMedia) v.getContext()).dispatchTakePictureIntent();
            }
        });

        //Listen for the textbox
        EditText editText = (EditText) findViewById(R.id.message);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Get the text they entered and send it to the server
                    MessageSender.sendMessage(v);

                    //Set the text in the box to nothing
                    v.setText("");
                    return true;
                }
                return false;

            }
        });

    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Retrieve the image
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            MessageSender.sendImage(imageBitmap);
            MessageSender.sendData();

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
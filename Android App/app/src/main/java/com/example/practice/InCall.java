package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
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
import java.util.HashMap;
import java.util.Map;

public class InCall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_call);

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        //Set the user id for this session
        int id = getId();
        MessageSender.setId(id);

        //Set the imageNum to 1
        int imageNum = 1;
        MessageSender.setImageNum(1);

        //Set up the director path that lukasz wants
        String dirPath = "/Media/";
        MessageSender.setDirPath(dirPath);

        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        Button fire = findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Fire");
                UserData.emergency_type = "Fire";
                sendData();
            }
        });
        fire.setText(UserData.wordmap.get("word_fire"));

        Button ems = findViewById(R.id.ems);
        ems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Ambulance");
                UserData.emergency_type = "EMS";
                sendData();
            }
        });
        ems.setText(UserData.wordmap.get("word_ems"));

        Button police = findViewById(R.id.police);
        police.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Police");
                UserData.emergency_type = "Police";
                sendData();
            }
        });
        police.setText(UserData.wordmap.get("word_police"));

        final Button imageButton = findViewById(R.id.call_addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).dispatchTakePictureIntent();
            }
        });
        imageButton.setText(UserData.wordmap.get("word_add_image"));

        //Listen for the textbox
        EditText editText = (EditText) findViewById(R.id.call_message);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                //Check if they clicked the done button
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //If so then hide the keyboard
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    String message = v.getText().toString();
                    if(!UserData.lang_new.equals("English")){
                        String translated = LanguageTranslation.translate(message, "English", UserData.lang_new);
                        v.setText(translated);
                    }
                    //Get the text they entered and send it to the server
                    MessageSender.sendMessage(v);

                    //Set the text in the box to nothing
                    v.setText("");
                    return true;
                }
                return false;
            }
        });
        editText.setHint(UserData.wordmap.get("word_media_hint"));
    }
    public void callNumber(String service){
        String number = null;
        //Retrieve the number for your location
        if (UserData.country_code != null) {
            try {
                OutputStream os = FTPCommunication.retrieveFile("emergency_numbers.txt", false);
                String message = os.toString();
                String[] rows = message.split("\n");
                String[] data = rows[0].trim().split(",");
                int i = 0;
                for (i = 0; i < 4; i++) {
                    if (data[i].equals(service)) {
                        break;
                    }
                }
                System.out.println(i);
                for (String s : rows) {
                    data = s.split(",");
                    if (data[0].equals(UserData.country_code)) {
                        number = data[i];
                        break;
                    }
                }
            } catch (Exception e) {
                //TODO
            }
        }
        else{
            number = "2015614917";
        }

        //Make the Call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));//change the number
        startActivity(callIntent);
    }
    public static void sendData(){
        Map<String, String> map = new HashMap<>();
        map.put("lat", UserData.lat);
        map.put("lon", UserData.lon);
        map.put("house_number", UserData.locationMap.get("\"house_number\""));
        map.put("road", UserData.locationMap.get("\"road\""));
        map.put("city", UserData.locationMap.get("\"city\""));
        map.put("country", UserData.locationMap.get("\"country\""));
        map.put("name", UserData.name);
        map.put("gender", UserData.gender);
        map.put("height", UserData.height);
        map.put("weight",UserData.weight);
        map.put("language", UserData.lang_new);
        map.put("emergency_type", UserData.emergency_type);

        map.put("tts_true", "true");
        map.put("tts_content", null);

        MessageSender.sendData(map);

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

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(imageBitmap);
        }
    }
    public int getId(){
        try {
            OutputStream os = FTPCommunication.retrieveFile("nextid.txt", false);
            String stringId = os.toString();
            int id = Integer.parseInt(stringId);
            String newId = Integer.toString(id + 1);
            InputStream is = new ByteArrayInputStream(newId.getBytes());
            FTPCommunication.addMedia(is, "nextid.txt", false);
            return id;
        }catch(Exception e){
            return -1;
        }
    }

}
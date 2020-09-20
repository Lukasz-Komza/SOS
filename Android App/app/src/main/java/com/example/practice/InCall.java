package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class InCall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_call);

        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        Button fire = findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Fire");
            }
        });
        fire.setText(UserData.country_code);

        Button ems = findViewById(R.id.ems);
        ems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Ambulance");
            }
        });
        ems.setText(prefs.getString("word_ems", null));

        Button police = findViewById(R.id.police);
        police.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Police");
            }
        });
        police.setText(prefs.getString("word_police",null));

        final Button hangup = findViewById(R.id.hangup);
        hangup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).hangup();
            }
        });
        final Button media_dir = findViewById(R.id.media_dir);
        media_dir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).media_dir();
            }
        });
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
    public void hangup(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
    public void media_dir(){
        Intent intent = new Intent(this, AddMedia.class);
        startActivity(intent);
    }

}
package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Determine if the app is being opened for the first time
        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean FirstTime = prefs.getBoolean("FirstTime", true);

        //Set up text map
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("word_language", "Please select language");
        editor.putString("word_next", "Next");
        editor.putString("word_lang_dir", "Edit Language");
        editor.putString("word_fire", "Fire Department");
        editor.putString("word_ems", "Emergency Medical Service");
        editor.putString("word_police", "Police Department");

        //Set lang_old
        editor.putString("lang_old", "English");

        //If so then mark it as opened and start SetLang
        if (FirstTime) {

            Intent intent = new Intent(this, SetLang.class);
            startActivity(intent);
        }
        //If not then open menu
        else {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        }

    }

}
package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Determine if the app is being opened for the first time
        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        boolean FirstTime = prefs.getBoolean("FirstTime", true);

        //Set up text map
        Map<String, String> map = new HashMap<>();
        map.put("word_fire", "Fire Department");
        map.put("word_ems", "Emergency Medical Service");
        map.put("word_police", "Police Department");
        map.put("word_app_name", "Practice");
        map.put("word_language", "Please select language");
        map.put("word_next", "Next");
        map.put("word_loc", "Sync Location Data");
        map.put("word_hint_name", "Name (Joseph Smith)");
        map.put("word_hint_gender", "Gender");
        map.put("word_hint_age", "Age (19 yo)");
        map.put("word_hint_height", "Height (5'9\")");
        map.put("word_hint_weight", "Weight (170 lbs)");
        map.put("word_prompt_info", "Tell us about yourself:");
        map.put("word_lang_dir", "Edit Language");
        map.put("word_info_dir", "My Info");
        map.put("word_call_dir", "Media Call");
        map.put("word_text_dir", "Silent SOS");
        map.put("word_in_queue", "Waiting...");
        map.put("word_out_queue", "Calling");
        map.put("word_hangup", "End Call");
        map.put("word_media_dir ", "Add Media");
        map.put("word_add_text", "Add Text");
        map.put("word_add_image", "Add Image");
        map.put("word_media_hint", "Describe your emergency:");
        UserData.wordmap = map;

        //Set lang_old
        UserData.lang_old="English";
        UserData.lang_new="English";

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
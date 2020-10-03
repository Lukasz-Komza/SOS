package com.example.SOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;

public class MainActivity extends AppCompatActivity {
    public static String sha_1 = "32:68:FF:23:7D:D4:2B:5C:96:53:43:34:AE:8F:93:C1:F1:9E:DF:0D";
    public static String client_secret = "ySwKNIuWMCi838Qlh-lW0drP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Determine if the app is being opened for the first time
        boolean FirstTime = isNullOrEmpty(LocalFileRetriever.retrieveMap("dataMap",this).get("FirstTime"));


        //If so then mark it as opened and start SetLang
        if (FirstTime) {
            Map<String, String> dataMap = new HashMap<>();
            Map<String, String> stringMap = new HashMap<>();

            //Set up map of all the strings in the app view
            stringMap.put("word_fire", "Fire Department");
            stringMap.put("word_ems", "Emergency Medical Service");
            stringMap.put("word_police", "Police Department");
            stringMap.put("word_app_name", "Practice");
            stringMap.put("word_language", "Please select language");
            stringMap.put("word_next", "Next");
            stringMap.put("word_submit", "Submit");
            stringMap.put("word_loc", "Sync Location Data");
            stringMap.put("word_health", "Sync Google Fit Data");
            stringMap.put("word_hint_name", "Name (Joseph Smith)");
            stringMap.put("word_hint_gender", "Gender");
            stringMap.put("word_hint_age", "Age (19 yo)");
            stringMap.put("word_hint_height", "Height (5'9\")");
            stringMap.put("word_hint_weight", "Weight (170 lbs)");
            stringMap.put("word_prompt_info", "Tell us about yourself:");
            stringMap.put("word_lang_dir", "Edit Language");
            stringMap.put("word_info_dir", "Update Account");
            stringMap.put("word_call_dir", "Media Call");
            stringMap.put("word_text_dir", "Silent SOS");
            stringMap.put("word_in_queue", "Waiting...");
            stringMap.put("word_out_queue", "Calling");
            stringMap.put("word_hangup", "End Call");
            stringMap.put("word_media_dir ", "Add Media");
            stringMap.put("word_add_text", "Add Text");
            stringMap.put("word_add_image", "Add Image");
            stringMap.put("word_media_hint", "Describe your emergency:");
            stringMap.put("word_text_id", "Your id is ");
            LocalFileRetriever.storeMap("stringMap", stringMap, this);

            //Set the default language
            stringMap.put("lang_old", "English");
            stringMap.put("lang_new", "English");

            //Set first time variable and store
            dataMap.put("FirstTime","false");
            LocalFileRetriever.storeMap("dataMap", dataMap,this);

            //Starts up from the user settings
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
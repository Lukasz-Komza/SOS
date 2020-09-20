package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Map;

public class SetLang extends AppCompatActivity {
    public static String lang_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_lang);

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        //Set text of the language textview
        final TextView tv = (TextView) findViewById(R.id.lang_text);
        tv.setText(UserData.wordmap.get("word_language"));
        final Button b = (Button) findViewById(R.id.NextButton1);
        b.setText(UserData.wordmap.get("word_next"));

        //get the spinner from the xml.
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //create a list of items for the spinner from the IBM language list
        Map<String, String> langMap = LanguageTranslation.getLanguages();
        String[] items = new String[langMap.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : langMap.entrySet()) {
            items[i] = entry.getKey();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //set the spinners OnItemSelectedListener
        //Note: language set to the SharedPreferences in here
        LangSpinnerActivity listener = new LangSpinnerActivity();
        spinner.setOnItemSelectedListener(listener);


        //Calls nextPage() on button click
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((SetLang) v.getContext()).nextPage();
            }
        });

    }
    public void nextPage(){
        //Change the language of the app
        String lang_old = UserData.lang_old;
        String lang_new = UserData.lang_new;

        Map<String, String> map = UserData.wordmap;
        if (!lang_old.equals(lang_new)) {
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().split("_")[0].equals("word")){
                    map.put(entry.getKey(), LanguageTranslation.translate((String) entry.getValue(), lang_old, lang_new));
                }
            }
        }

        //Goes to the setinfo page
        Intent intent = new Intent(this, SetInfo.class);
        startActivity(intent);
    }
}
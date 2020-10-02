package com.example.SOS;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;


public class LangSpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //Retrieve the selected item
        String language = (String) parent.getItemAtPosition(pos);
        //Store data in LanguageAssistant
        UserData.lang_new = language;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Store data in Shared Preferences
    }
}
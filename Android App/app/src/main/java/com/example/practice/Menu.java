package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        //Sets the text of the elements
        final Button b = (Button) findViewById(R.id.LangButton);
        b.setText(prefs.getString("word_lang_dir", null));

        //Associate Buttons with methods
        final Button langButton = findViewById(R.id.LangButton);
        langButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).lang();
            }
        });
        final Button infoButton = findViewById(R.id.InfoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).info();
            }
        });
        final Button callButton = findViewById(R.id.CallButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).queue("Call");
            }
        });
        final Button textButton = findViewById(R.id.TextButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).queue("Text");
            }
        });
    }
    public void lang(){
        //Goes to the setlang page
        Intent intent = new Intent(this, SetLang.class);
        startActivity(intent);
    }
    public void info(){
        //Goes to the setinfo page
        Intent intent = new Intent(this, SetInfo.class);
        startActivity(intent);
    }
    public void queue(String file_header){
        //TODO: Implement call on local like prank call apps
        //Goes to the queue page
        Intent intent = null;
        if(file_header.equals("Call")){
            intent = new Intent(this, InCall.class);
        }
        else if(file_header.equals("Text")){
            intent = new Intent(this, AddMedia.class);
        }
        else{
            intent = new Intent(this, Menu.class);
        }
        startActivity(intent);
    }

}
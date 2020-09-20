package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Queue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue);

        //Determines the information sent from intent
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("EXTRA_MESSAGE");
            if (j.equals("Call")){
                //TODO
            }
            if (j.equals("Text")){
                Intent intent = new Intent(this, AddMedia.class);
                startActivity(intent);
            }
            if (j.equals("Error")){
                //TODO
            }
        }
        else {
            //Restarts Queue with an error message
            Intent intent = new Intent(this, Queue.class);
            intent.putExtra(EXTRA_MESSAGE, "Error");
            startActivity(intent);
        }
    }
}
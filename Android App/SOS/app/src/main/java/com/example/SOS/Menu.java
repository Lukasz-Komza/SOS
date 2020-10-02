package com.example.SOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(tp);

        //Set the user id for this session
        int id = getId();
        MessageSender.setId(id);

        //Set up the director path that lukasz wants
        String dirPath = "/Media/";
        MessageSender.setDirPath(dirPath);


        //Associate Buttons with methods
        final Button langButton = findViewById(R.id.LangButton);
        langButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).lang();
            }
        });
        langButton.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_lang_dir"));
        final Button infoButton = findViewById(R.id.InfoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).info();
            }
        });
        infoButton.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_info_dir"));
        final Button callButton = findViewById(R.id.CallButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).queue("Call");
            }
        });
        callButton.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_call_dir"));
        final Button textButton = findViewById(R.id.TextButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).queue("Text");
            }
        });
        textButton.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_text_dir"));
        final Button redButton = findViewById(R.id.redButton);
        redButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((Menu) v.getContext()).sendData();
                ((Menu) v.getContext()).callNumber("Police");
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
    public void sendData(){
        Map<String, String> map = new HashMap<>();
        map.put("lat", LocalFileRetriever.retrieveMap("dataMap",this).get("lat"));
        map.put("lon", LocalFileRetriever.retrieveMap("dataMap",this).get("lon"));
        map.put("house_number", LocalFileRetriever.retrieveMap("dataMap",this).get("\"house_number\""));
        map.put("road", LocalFileRetriever.retrieveMap("dataMap",this).get("\"road\""));
        map.put("city", LocalFileRetriever.retrieveMap("dataMap",this).get("\"city\""));
        map.put("country", LocalFileRetriever.retrieveMap("dataMap",this).get("\"country\""));
        map.put("name", LocalFileRetriever.retrieveMap("dataMap",this).get("name"));
        map.put("gender", LocalFileRetriever.retrieveMap("dataMap",this).get("gender"));
        map.put("height", LocalFileRetriever.retrieveMap("dataMap",this).get("height"));
        map.put("weight",LocalFileRetriever.retrieveMap("dataMap",this).get("weight"));
        map.put("language", LocalFileRetriever.retrieveMap("dataMap",this).get("lang_new"));
        map.put("emergency_type", "Police");

        map.put("tts_true", "false");
        map.put("tts_content", null);

        MessageSender.sendData(map);

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
    public void callNumber(String service){
        String number = null;
        //Retrieve the number for your location
        if (LocalFileRetriever.retrieveMap("dataMap",this).get("country_code") != null) {
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
                    if (data[0].equals(LocalFileRetriever.retrieveMap("dataMap",this).get("country_code"))) {
                        number = data[i];
                        break;
                    }
                }
            } catch (Exception e) {
                //TODO
            }
        }
        else{
            //TODO: change placeholder
            number = "2015614917";
        }

        //Make the Call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));//change the number
        startActivity(callIntent);
    }

}
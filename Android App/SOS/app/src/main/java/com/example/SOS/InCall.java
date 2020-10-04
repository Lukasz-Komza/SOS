package com.example.SOS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class InCall extends AppCompatActivity {

    private int REQUEST_TAKE_PHOTO = 1;
    private File file;

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

        final TextView tv = (TextView) findViewById(R.id.idView);
        tv.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_text_id") + Integer.toString(id));

        Button fire = findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Fire");
                LocalFileRetriever.storeToMap("dataMap", "emergency_type", "Fire", v.getContext());
                ((InCall) v.getContext()).sendData();
            }
        });
        fire.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_fire"));

        Button ems = findViewById(R.id.ems);
        ems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Ambulance");
                LocalFileRetriever.storeToMap("dataMap", "emergency_type", "EMS", v.getContext());
                ((InCall) v.getContext()).sendData();
            }
        });
        ems.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_ems"));

        Button police = findViewById(R.id.police);
        police.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).callNumber("Police");
                LocalFileRetriever.storeToMap("dataMap", "emergency_type", "Police", v.getContext());
                ((InCall) v.getContext()).sendData();
            }
        });
        police.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_police"));

        final Button imageButton = findViewById(R.id.call_addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InCall) v.getContext()).dispatchTakePictureIntent();
            }
        });
        imageButton.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_add_image"));

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
                    if(!LocalFileRetriever.retrieveMap("dataMap",v.getContext()).get("lang_new").equals("English")){
                        message = LanguageTranslation.translate(message, "English", LocalFileRetriever.retrieveMap("dataMap",v.getContext()).get("lang_new"));
                    }
                    //Get the text they entered and send it to the server
                    MessageSender.sendMessage(message);

                    //Set the text in the box to nothing
                    v.setText("");
                    return true;
                }
                return false;
            }
        });
        editText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_media_hint"));
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
            number = "2015614917";
        }

        //Make the Call
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));//change the number
        startActivity(callIntent);
    }
    public void sendData(){
        Map<String, String> map = new HashMap<>();
        map.put("lat", LocalFileRetriever.retrieveMap("dataMap",this).get("lat"));
        map.put("lon", LocalFileRetriever.retrieveMap("dataMap",this).get("lon"));
        map.put("house_number", LocalFileRetriever.retrieveMap("locMap",this).get("\"house_number\""));
        map.put("road", LocalFileRetriever.retrieveMap("locMap",this).get("\"road\""));
        map.put("city", LocalFileRetriever.retrieveMap("locMap",this).get("\"city\""));
        map.put("country", LocalFileRetriever.retrieveMap("locMap",this).get("\"country\""));
        map.put("name", LocalFileRetriever.retrieveMap("dataMap",this).get("name"));
        map.put("gender", LocalFileRetriever.retrieveMap("dataMap",this).get("gender"));
        map.put("height", LocalFileRetriever.retrieveMap("dataMap",this).get("height"));
        map.put("weight",LocalFileRetriever.retrieveMap("dataMap",this).get("weight"));
        map.put("language", LocalFileRetriever.retrieveMap("dataMap",this).get("lang_new"));
        map.put("emergency_type", "Police");

        map.put("tts_true", "true");
        map.put("tts_content", null);

        MessageSender.sendData(map);

    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                file = photoFile;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Retrieve the image

            String encodedPath = file.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(encodedPath);
            createDialog(bitmap);
        }
    }
    private File createImageFile() throws IOException {
        String currentPhotoPath;

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
    public void createDialog(Bitmap bmp){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(LocalFileRetriever.retrieveMap("stringMap",this).get("description_text"));

        // Set up the input
        final ImageAndText input = new ImageAndText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setImageBitMap(bmp);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
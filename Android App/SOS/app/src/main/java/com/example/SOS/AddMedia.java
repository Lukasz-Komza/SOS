package com.example.SOS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class AddMedia extends AppCompatActivity implements Scrollable{

    private int REQUEST_TAKE_PHOTO = 1;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_media);

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

        //Listen for the add image button
        final Button imageButton = findViewById(R.id.addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((AddMedia) v.getContext()).dispatchTakePictureIntent();
            }
        });
        imageButton.setText(LocalFileRetriever.retrieveMap("stringMap",this).get("word_add_image"));

        //Listen for the textbox
        EditText editText = (EditText) findViewById(R.id.message);
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
                        message = LanguageTranslation.translate(message, LocalFileRetriever.retrieveMap("dataMap",v.getContext()).get("lang_new"), "English");
                    }

                    //Get the text they entered and send it to the server
                    MessageSender.sendMessage(message);

                    //Put the message to the scroll
                    ((Scrollable) v.getContext()).addToScroll("Description: " + v.getText().toString());

                    //Set the text in the box to nothing
                    //TODO  make the textbox darker
                    return true;
                }
                return false;
            }
        });
        editText.setHint(LocalFileRetriever.retrieveMap("stringMap",this).get("word_media_hint"));

        //TODO create buttons with emergencies

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
        map.put("height", LocalFileRetriever.retrieveMap("healthMap",this).get("height"));
        map.put("weight",LocalFileRetriever.retrieveMap("healthMap",this).get("weight"));
        map.put("language", LocalFileRetriever.retrieveMap("dataMap",this).get("lang_new"));
        map.put("emergency_type", "Police");

        map.put("tts_true", "false");
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

            //Add the image to a scrolling image view
            ImageView myImage = new ImageView(this);
            myImage.setImageBitmap(bitmap);
            myImage.setRotation(90);
            myImage.setAdjustViewBounds(true);
            myImage.setPadding(0,75,0,75);
            LinearLayout picLL = findViewById(R.id.image_scroll);
            picLL.addView(myImage);
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
    public void addToScroll(String s){
        //Add the text to scrolling text view
        TextView myText = new TextView(this);
        myText.setText(s);
        myText.setPadding(100,75,0,75);
        LinearLayout picLL = findViewById(R.id.image_scroll);
        picLL.addView(myText);
    }
}
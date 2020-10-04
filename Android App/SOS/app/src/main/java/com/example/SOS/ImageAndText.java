package com.example.SOS;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAndText extends RelativeLayout {
    View rootView;
    private EditText et;
    private ImageView iv;
    private Bitmap bmp;

    public ImageAndText(Context context) {
        super(context);
        init(context);
    }

    public ImageAndText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        rootView = inflate(context, R.layout.image_and_text, this);

        et = rootView.findViewById(R.id.description_text);
        iv = rootView.findViewById(R.id.phone_image);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
                    MessageSender.sendImage(bmp, message);

                    //Put the text in the scroll
                    ((Scrollable) context).addToScroll("Caption: " +v.getText().toString());

                    //TODO make the textbox darker
                    return true;
                }
                return false;
            }
        });
    }
    public void setImageBitMap(Bitmap bitMap){
        iv.setImageBitmap(bitMap);
        iv.setRotation(90);
        bmp = bitMap;
    }
    public String getText(){
        return et.getText().toString();
    }
}

package com.example.practice;

import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MessageSender {
    private static String dirPath;
    private static int id;
    private static int imageNum;
    public static void setDirPath(String dirPath){
        MessageSender.dirPath = dirPath;
    }
    public static void setID(int id){
        MessageSender.id = id;
    }
    public static void setImageNum(int imageNum){
        MessageSender.imageNum = imageNum;
    }
    public static void sendData(){
        String message = "{\n" +
                "\t\"Location\": {\n" +
                "\t\t\"house_number\": \"459\",\n" +
                "\t\t\"road\": \"Beech Street\",\n" +
                "\t\t\"city\": \"Washington Township\",\n" +
                "\t\t\"state\": \"New Jersey\",\n" +
                "\t\t\"postcode\": \"07676\",\n" +
                "\t\t\"country\": \"United States of America\",\n" +
                "\t\t\"lat\": \"40.99014\",\n" +
                "\t\t\"lon\": \"-74.05355\"\n" +
                "\t},\n" +
                "\t\"User\": {\n" +
                "\t\t\"name\": \"Ethan\",\n" +
                "\t\t\"gender\":\"male\",\n" +
                "\t\t\"height\": \"71\",\n" +
                "\t\t\"weight\": \"200\",\n" +
                "\t\t\"heart_rate\":\"70\",\n" +
                "\t\t\"language\":\"English\"\n" +
                "\t},\n" +
                "\t\"Emergency\":\"Fire\",\n" +
                "\t\"tts_true\":\"false\",\n" +
                "\t\"tts_content\":\"\"\n" +
                "}\n" +
                "\t\t";
        InputStream is = new ByteArrayInputStream(message.getBytes());
        try {
            FTPCommunication.addMedia(is, dirPath + id + ".json", false);
        }catch(Exception e){
            //TODO
        }
    }
    public static void sendMessage(TextView v){
        String s = v.getText().toString();
        InputStream is = new ByteArrayInputStream(s.getBytes());
        try {
            FTPCommunication.addMedia(is, dirPath + id + ".txt", false);
        }catch(Exception e){
            //TODO
        }
    }
    public static void sendImage(Bitmap imageBitmap){
        //Convert bitmap to input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

        //Add the image to the server
        try {
            FTPCommunication.addMedia(bs, dirPath + imageNum +"_" + id +".jpg", true);
        }catch(Exception e){
            //TODO
        }

        imageNum++;
    }
}

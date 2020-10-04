package com.example.SOS;

import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

public class MessageSender {
    private static String dirPath;
    private static int id;
    private static int imageNum;
    public static void setDirPath(String dirPath){
        MessageSender.dirPath = dirPath;
    }
    public static void setId(int id){
        MessageSender.id = id;
    }
    public static void setImageNum(int imageNum){
        MessageSender.imageNum = imageNum;
    }
    public static void sendData(Map<String, String> map){
        String message = "{\n" +
                "\t\"Location\": {\n" +
                "\t\t\"lat\": \"" + map.get("lat") + "\",\n" +
                "\t\t\"lon\": \"" + map.get("lon") + "\",\n" +
                "\t\t\"house_number\": \"" + map.get("house_number") + "\",\n" +
                "\t\t\"road\": \"" + map.get("road") + "\",\n" +
                "\t\t\"city\": \"" + map.get("city") + "\",\n" +
                "\t\t\"country\": \"" + map.get("country") + "\"\n" +
                "\t},\n" +
                "\t\"User\": {\n" +
                "\t\t\"name\": \"" + map.get("name") + "\",\n" +
                "\t\t\"gender\":\"" + map.get("gender") + "\",\n" +
                "\t\t\"height\": \"" + map.get("height") + "\",\n" +
                "\t\t\"weight\": \"" + map.get("weight") + "\",\n" +
                "\t\t\"language\":\"" + map.get("language") + "\"\n" +
                "\t},\n" +
                "\t\"Emergency\":\"" + map.get("emergency_type") + "\",\n" +
                "\t\"tts_true\":\"" + map.get("tts_true") + "\"\n" +
                "}\n" +
                "\t\t";
        System.out.println(message);
        InputStream is = new ByteArrayInputStream(message.getBytes());
        try {
            FTPCommunication.addMedia(is, dirPath + id + ".json", false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void sendMessage(String s){
        InputStream is = new ByteArrayInputStream(s.getBytes());
        try {
            FTPCommunication.addMedia(is, dirPath + id + ".txt", false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void sendImage(Bitmap imageBitmap, String s){
        //Convert description text to stream
        InputStream is = new ByteArrayInputStream(s.getBytes());

        //Convert bitmap to input stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

        //Add the image to the server
        try {
            FTPCommunication.addMedia(bs, dirPath + imageNum +"_" + id +".jpg", true);
            FTPCommunication.addMedia(is, dirPath + imageNum + "_" + id + ".txt", false);
        }catch(Exception e){
            e.printStackTrace();
        }

        imageNum++;
    }
}

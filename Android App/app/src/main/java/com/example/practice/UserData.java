package com.example.practice;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

import java.util.Map;

public class UserData {
    public static String country_code;
    public static Map<String, String> locationMap;

    public static String lat;
    public static String lon;
    public static String height;
    public static String weight;
    public static String age;
    public static String gender;
    public static String name;
    public static String lang_old;
    public static String lang_new;
    public static Map<String, String> wordmap;
    public static String emergency_type;
}

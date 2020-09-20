package com.example.practice;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

import java.util.Map;

public class UserData {
    public static String country_code;
    public static Location location;
    public static Map<String, String> locationMap;
    public static Task<DataReadResponse> response;

}

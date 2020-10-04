package com.example.SOS;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReverseGeocoder {
    public static Map<String, String> ReverseGeocode(String lat, String lon) throws IOException, Exception {
        String private_key = "fb828dae4ee651";
        String url = "https://us1.locationiq.com/v1/reverse.php";

        //Creates a dictionary of parameters that must be passed to the URL
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", private_key);
        parameters.put("lat", lat);
        parameters.put("lon", lon);
        parameters.put("format", "json");

        //Formats the url string to add these params
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(getParamsString(parameters, url)).build();

        Response response = client.newCall(request).execute();

        String toParse = response.body().string();
        return parseAddress(toParse);
    }


    public static String getParamsString(Map<String, String> params, String url){

        url = url + "/?";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url = url + entry.getKey() + "=" + entry.getValue() + "&";
        }
        return url;
    }

    public static Map<String, String> parseAddress(String jsonAddress){
        //Extracts the address data from the json string
        System.out.println("reached");
        Pattern p = Pattern.compile(":\\{.*\\}");
        Matcher m = p.matcher(jsonAddress);
        m.find();
        String addressData = m.group();
        addressData = addressData.substring(2, addressData.length()-2);
        System.out.println(addressData);

        //Extracts each key, value pair into a Map object
        Map<String, String> addressMap = new HashMap<>();
        p = Pattern.compile("\"\\p{Alnum}+(\\p{Alnum}+.)*\"");
        m = p.matcher(addressData);
        while (m.find()){
            String key = m.group();
            if(m.find()){
                System.out.println(key);
                System.out.println(m.group());
                addressMap.put(key, m.group());
            }
        }
        return addressMap;
    }
}

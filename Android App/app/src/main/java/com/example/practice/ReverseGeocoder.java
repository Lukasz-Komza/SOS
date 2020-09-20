package com.example.practice;

/*import android.content.Intent;

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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
 */

public class ReverseGeocoder {
    /*public static Map<String, String> ReverseGeocode(String lat, String lon) throws IOException, Exception {
        String private_key = "fb828dae4ee651";
        String url = "https://us1.locationiq.com/v1/reverse.php";

        //Creates a dictionary of parameters that must be passed to the URL
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", private_key);
        parameters.put("lat", lat);
        parameters.put("lon", lon);
        parameters.put("format", "json");

        //Formats the url string to add these params
        url = getParamsString(parameters, url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        return parseAddress(response.body().toString());
    }

    public static void tester() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            System.out.println("Internet is connected");
        } catch (MalformedURLException e) {
            System.out.println("Internet is not connected");
        } catch (IOException e) {
            System.out.println("Internet is not connected");
        }
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
        Pattern p = Pattern.compile(":\\{.*\\}");
        Matcher m = p.matcher(jsonAddress);
        m.find();
        String addressData = m.group();
        addressData = addressData.substring(2, addressData.length()-2);
        System.out.println(addressData);

        //Extracts each key, value pair into a Map object
        Map<String, String> addressMap = new HashMap<>();
        p = Pattern.compile("\"(\\p{Alnum}+.)*\"");
        m = p.matcher(addressData);
        while (m.find()){
            String key = m.group();
            System.out.println(key);
            if(m.find()){
                addressMap.put(key, m.group());
            }
        }
        return addressMap;
    }*/
}

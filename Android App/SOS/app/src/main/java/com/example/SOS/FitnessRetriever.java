package com.example.SOS;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;

public class FitnessRetriever{
    private static final String TAG = "FitnessFragment";

    public static void googleFitAsync(Executor executor, final Context context, final DataType dt, final String key){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                googleFitSync(context, dt, key);
            }
        });
    }

    private static void googleFitSync(Context context, DataType dt, String key){
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        //Log these dates, not sure why
        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        //Create a request to read the data of the scopes we want
        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .read(dt)
                        .build();

        //read in the datasets from the time range we want
        Task<DataReadResponse> response = Fitness
                .getHistoryClient(context, GoogleSignIn.getAccountForScopes(context, Fitness.SCOPE_BODY_READ))
                .readData(readRequest);
        DataReadResponse readDataResponse = null;
        try {
            readDataResponse = Tasks.await(response);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataSet> dataSets = readDataResponse.getDataSets();

        //Parse through the datasets, store the data in memory
        Map<String, String> healthMap = new HashMap<>();
        for(DataSet ds : dataSets){
            for(DataPoint dp: ds.getDataPoints()){
                for (Field f : dp.getDataType().getFields()){
                    healthMap.put(key, dp.getValue(f).toString());
                }
            }
        }
        LocalFileRetriever.storeMap("healthMap", healthMap,context);
    }
}

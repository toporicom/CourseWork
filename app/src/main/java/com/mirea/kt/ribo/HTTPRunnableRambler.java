package com.mirea.kt.ribo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPRunnableRambler implements Runnable {
    private String address;
    private List<String> result = new ArrayList<String>();
    private StringBuilder response;
    public List<String> getResult (){
        return result;
    }
    public HTTPRunnableRambler(String address) {
        this.address = address;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            Log.i("News response code", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    result.add(inputLine);
                }
                in.close();

            } else {
                System.out.println("Failed with code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
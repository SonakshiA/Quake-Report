package com.example.quakereportupdated;


import 
        android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {

    //private static final String JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    private static URL createURL(String stringURL){
        java.net.URL url = null;
        try{
            url = new URL(stringURL);
        } catch(MalformedURLException e){
            Log.e("MalformedURLException",e.getMessage());
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String json = "";
        HttpURLConnection urlConnection=null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                json = readFromStream(inputStream);
            }
        }catch(IOException e){
            Log.e("Error in HTTP request",e.getMessage());
        }finally{
            if (urlConnection!=null){
                urlConnection.disconnect();
            } if(inputStream!=null){
                inputStream.close();
            }
        }
        return json;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String earthquakeJSON) {

        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        } else {
            // Create an empty ArrayList that we can start adding earthquakes to
            List<Earthquake> earthquakes = new ArrayList<>();

            // Try to parse the SAMPLE_JSON_RESPONSE. If there's a
            // problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {
                JSONObject jsonObject = new JSONObject(earthquakeJSON);
                JSONArray earthquakeArray = jsonObject.optJSONArray("features");

                for (int i = 0; i < earthquakeArray.length(); i++) {
                    JSONObject currentEarthquake = earthquakeArray.optJSONObject(i);
                    JSONObject properties = currentEarthquake.optJSONObject("properties");
                    double magnitude = properties.optDouble("mag");
                    String place = properties.optString("place");
                    long time = properties.optLong("time");
                    String url = properties.optString("url");

                    Earthquake earthquake = new Earthquake(magnitude, place, time, url);
                    earthquakes.add(earthquake);
                }

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of earthquakes
            return earthquakes;
        }
    }
    public static List<Earthquake> fetchEarthquakeData(String requestURL){
        try{
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch(IOException e){
            Log.e("Error",e.getMessage());
        }
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;
    }
}

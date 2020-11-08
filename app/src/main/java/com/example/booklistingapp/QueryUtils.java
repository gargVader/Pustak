package com.example.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    // Tag for log messages
    private static String LOG_TAG = QueryUtils.class.getName();

    // Private Constructor
    private QueryUtils(){

    }

    // Queries the given stringUrl and returns an ArrayList<Book>
    public static ArrayList<Book> extractBooks(String stringUrl){

        Log.e(LOG_TAG, "Making network request to fetch data");

        // Create URL object
        URL url = createUrl(stringUrl);

        // Make a HTTP request and get stringJsonResponse
        String stringJsonResponse = null;
        try{
            stringJsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract ArrayList from stringJsonResponse
        ArrayList<Book> bookArrayList = extractFeaturesFromJson(stringJsonResponse);

        // Return ArrayList
        return bookArrayList;
    }

    private static URL createUrl(String stringUrl){
        Log.e(LOG_TAG, "Creating URL");
        if(stringUrl==null || stringUrl.isEmpty()) return  null;
        URL url = null;
        try{
            url = new URL(stringUrl);
            Log.e(LOG_TAG, url.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error in creating URL");
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            Log.e(LOG_TAG, "Opening URL connection");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(Exception e){
            Log.e(LOG_TAG, "Problem in retrieving JSON results");
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
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

    private static ArrayList<Book> extractFeaturesFromJson(String stringJsonResponse){

        if(stringJsonResponse==null || stringJsonResponse.isEmpty()) return null;

        ArrayList<Book> bookArrayList = new ArrayList<Book>();

        try {
            JSONObject root = new JSONObject(stringJsonResponse);
            JSONArray jsonArray = root.getJSONArray("items");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject currJsonObject = jsonArray.getJSONObject(i);

                JSONObject volumeInfoJsonObject = currJsonObject.getJSONObject("volumeInfo");

                String title = volumeInfoJsonObject.getString("title");
                Log.e(LOG_TAG, title);
                ArrayList<String> authorArrayList = new ArrayList<String>();
                JSONArray authorJsonArray = volumeInfoJsonObject.getJSONArray("authors");
                for(int j=0; j<authorJsonArray.length(); j++){
                    String currAuthor = authorJsonArray.getString(j);
                    authorArrayList.add(currAuthor);
                }

                String imageStringUrl = null;
                JSONObject imageLinksJsonObject = volumeInfoJsonObject.getJSONObject("imageLinks");
                imageStringUrl = imageLinksJsonObject.getString("thumbnail");

                Bitmap imageBitmap = extractImageBitmap(imageStringUrl);


                String infoLink = volumeInfoJsonObject.getString("infoLink");

                bookArrayList.add(new Book(title, authorArrayList, imageBitmap, infoLink));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return bookArrayList;
    }

    private static Bitmap extractImageBitmap(String imageStringUrl) throws IOException {
        Log.e(LOG_TAG, "Fetching Image Bitmap");
        String imageStringUrl_substring = imageStringUrl.substring(4);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https");
        stringBuilder.append(imageStringUrl_substring);

        imageStringUrl = stringBuilder.toString();
        URL url = createUrl(imageStringUrl);

        Bitmap imageBitmap = null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            Log.e(LOG_TAG, "Opening URL connection");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                imageBitmap = readFromStreamForBitmap(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(Exception e){
            Log.e(LOG_TAG, "Problem in retrieving JSON results");
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return imageBitmap;

    }

    private static Bitmap readFromStreamForBitmap(InputStream inputStream){

        Bitmap imageBitmap = null;
        imageBitmap = BitmapFactory.decodeStream(inputStream);
        return imageBitmap;
    }





}

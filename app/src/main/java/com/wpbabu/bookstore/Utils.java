package com.wpbabu.bookstore;

import android.app.LoaderManager;
import android.util.Log;
import android.widget.Toast;

import com.wpbabu.bookstore.Book;

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

/**
 * Created by danish on 27/3/18.
 */

public class Utils {

    private static String LOG_TAG = "Utils";
    private static String error = "Error";

    private static URL createUrl(String ur){
        URL url = null;
        try{
            url = new URL(ur);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, error);
        }
        return url;
    }

    private static String readFromStream(InputStream stream){
        StringBuilder output = new StringBuilder();
        if(stream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader reader= new BufferedReader(inputStreamReader);
            try{
                String line = reader.readLine();
                while (line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            }catch (IOException e){
                Log.e(LOG_TAG, error);
            }
        }

        return output.toString();
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection http = null;
        InputStream stream = null;
        try{
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setReadTimeout(10000);
            http.setConnectTimeout(15000);

            http.connect();

            if(http.getResponseCode() == 200){
                stream = http.getInputStream();
                jsonResponse = readFromStream(stream);
            }
        }catch (IOException e){
            Log.e(LOG_TAG, error);
        }finally {
            if(http != null){
                http.disconnect();
            }
            if(stream != null){
                stream.close();
            }
        }
        return jsonResponse;
    }

    private static ArrayList<Book> extractData(String jsonResponse){

        ArrayList<Book> books = new ArrayList<Book>();

        try{
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray items = root.getJSONArray("items");



            for(int i=0; i<items.length(); i++){

                JSONObject book = items.getJSONObject(i);
                JSONObject volume_info = book.getJSONObject("volumeInfo");
                JSONObject sale_info = book.getJSONObject("saleInfo");

                JSONObject image_info = volume_info.getJSONObject("imageLinks");
                //JSONArray authorArray = volume_info.getJSONArray("authors");

                String title = volume_info.getString("title");
                String category = null;
                String pages = null;
                String buyLink = null;
                String image = image_info.getString("thumbnail");


                try{
                    category = (volume_info.getJSONArray("categories")).getString(0);
                }catch (JSONException e){
                    category = "N.A.";
                }
                try {
                    buyLink = sale_info.getString("buyLink");;
                }catch (JSONException e){
                    buyLink = "";
                }
                try {
                    pages = volume_info.getString("pageCount");
                }catch (JSONException e){
                    pages = "N.A.";
                }




                    /*StringBuilder authors = new StringBuilder();
                    if(authorArray.length() > 1){
                        for(int  j=0; j<authorArray.length(); j++){
                            authors.append(", "+authorArray.getString(j));
                        }
                    }else {
                        authors.append(authorArray.getString(0));
                    }*/

                books.add(new Book(title, pages, "auth", buyLink, image, category));
            }

        }catch (JSONException e){
            Log.e(LOG_TAG, error);
        }

        return books;

    }

    public static List<Book> fetchBookData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = "";
        try{
            jsonResponse = makeHTTPRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, error);
        }

        List<Book> books = extractData(jsonResponse);
        return books;
    }
}

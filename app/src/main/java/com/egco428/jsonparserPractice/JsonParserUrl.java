package com.egco428.jsonparserPractice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JitCrazySmart on 3/11/2559.
 */
public class JsonParserUrl {

    private List<String> name = new ArrayList<>();
    private List<String> url = new ArrayList<>();
    private String allName="";
    private String urlString = null;

    public volatile boolean parsingComplete = true;

    public JsonParserUrl(String url){
        this.urlString = url;
    }
    public List<String> getName(){
        return name;
    }
    public List<String> getUrl(){
        return url;
    }

    public void readAndParseJSON(String in){
        try {
            JSONArray jsonArray = new JSONArray(in);

            for(int i = 0 ; i < jsonArray.length() ; i++){
                allName = allName+jsonArray.getJSONObject(i).getString("display")+"\n";

                name.add(jsonArray.getJSONObject(i).getString("display"));
                url.add(jsonArray.getJSONObject(i).getString("url"));
            }

            parsingComplete = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fetchJSON(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    String data = convertStreamToString(stream);

                    readAndParseJSON(data);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }




}

package com.abc.blindkiosk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MenuAPI {
    static String storeName(String name){
        String storeId = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/menu?name="+name);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }
            bf.close();

            Log.d("Chk", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");

            storeId = jsonObject1.getString("_id");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeId;
    }

    static List<String> category(String id){
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/category?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            //Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray categoryArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<String>();
            for (int i = 0; i < categoryArray.length(); i++) {
                list.add(categoryArray.get(i).toString());
                Log.d("Lhk", categoryArray.get(i).toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    static List<String> subcategory(String id){
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/subcategory?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray categoryArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<String>();
            for (int i = 0; i < categoryArray.length(); i++) {
                list.add(categoryArray.get(i).toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    static List<String> food(String id){
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/food?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray categoryArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<String>();
            for (int i = 0; i < categoryArray.length(); i++) {
                list.add(categoryArray.get(i).toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    static String custom(String id){
        String customId = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/custom?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            JSONObject jsonObject = new JSONObject(result);
            JSONArray categoryArray = (JSONArray) jsonObject.get("data");

            customId = categoryArray.get(0).toString();
            Log.d("Chk",customId);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customId;
    }

    static List<String> allMenu(String id){
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/allfood?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line;
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray categoryArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<>();
            for (int i = 0; i < categoryArray.length(); i++) {
                list.add(categoryArray.get(i).toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}

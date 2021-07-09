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
import java.util.ArrayList;
import java.util.List;

public class StoreAPI {
    List<String> access(String x, String y){
        List<String> list = null;
        try {
            URL url = new URL("http://18.117.207.121:8080/store?x="+x+"&y="+y);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray storeArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<String>();

            for (int i = 0; i < storeArray.length(); i++) {
                list.add((String) storeArray.get(i));
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


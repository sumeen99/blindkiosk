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
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MenuAPI {
    String accessMenu(String name) {
        List<String> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/menu?name=" + name);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            Log.d("Chk", "################");

            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk", result);
            Log.d("Chk", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            JSONObject jsonObject = new JSONObject(result);
            JSONObject storeObject = jsonObject.getJSONObject("data");
            String storeId = storeObject.getString("_id");
            return storeId;

        } catch (MalformedURLException e) {
            Log.d("Chk", "111111111");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Chk", "222222222");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("Chk", "333333333");
            e.printStackTrace();
        }
        Log.d("Chk", "?????????????????????????????");
        return null;
    }

    List<MenuInfo> accessCategory(String id) {
        List<MenuInfo> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/category?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray dataArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<MenuInfo>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject categoryObject = dataArray.getJSONObject(i);
                MenuInfo category = new MenuInfo(
                        categoryObject.getString("_id"),
                        categoryObject.getString("storeId"),
                        categoryObject.getString("name"));
                list.add(category);
            }
            return list;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<MenuInfo> accessSubcategory(String id) {
        List<MenuInfo> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/subcategory?id=" + id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");


            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray dataArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<MenuInfo>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject categoryObject = dataArray.getJSONObject(i);
                MenuInfo subcategory = new MenuInfo(
                        categoryObject.getString("_id"),
                        categoryObject.getString("categoryId"),
                        categoryObject.getString("name"));
                list.add(subcategory);
            }
            return list;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<FoodInfo> accessFood(String id) {
        List<FoodInfo> list = null;
        try {
            URL url = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/food?id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            String line = "";
            String result = "";
            BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
            }

            Log.d("Chk", result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray dataArray = (JSONArray) jsonObject.get("data");

            list = new ArrayList<FoodInfo>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject foodObject = dataArray.getJSONObject(i);

                List<String> customIdList = new ArrayList<String>();

                JSONArray customIdArray = (JSONArray) foodObject.get("customId");
                for (int j = 0; j < customIdArray.length(); j++) {
                    customIdList.add(customIdArray.getString(j));
                }

                JSONArray sizeArray = (JSONArray) foodObject.get("size");
                JSONArray priceArray = (JSONArray) foodObject.get("price");
                for (int j = 0; j < sizeArray.length(); j++) {
                    FoodInfo food = new FoodInfo(
                            foodObject.getString("_id"),
                            foodObject.getString("name"),
                            foodObject.getString("subcategoryId"),
                            customIdList,
                            foodObject.getBoolean("temp"),
                            sizeArray.getString(j),
                            priceArray.getString(j));

                    list.add(food);
                }

            }
            return list;

        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    CustomInfo accessCustom(String customId) {
        URL customURL = null;
        try {
            customURL = new URL("http://ec2-18-117-207-121.us-east-2.compute.amazonaws.com:8080/custom?id=" + customId);
            HttpURLConnection conn = (HttpURLConnection) customURL.openConnection();
            conn.setRequestMethod("GET");

            String customLine = "";
            String customResult = "";
            BufferedReader customBf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((customLine = customBf.readLine()) != null) {
                customResult = customResult.concat(customLine);
            }

            Log.d("Chk", customResult);
            JSONObject jsonObject = new JSONObject(customResult);
            JSONArray customDataArray = (JSONArray) jsonObject.get("data");
            JSONObject customObject = (JSONObject) customDataArray.get(0);

            List<String> typeList = new ArrayList<String>();
            List<String> typePriceList = new ArrayList<String>();

            JSONArray typeArray = (JSONArray) customObject.get("type");
            for (int j = 0; j < typeArray.length(); j++) {
                typeList.add(typeArray.getString(j));
            }
            Log.d("Chk",customObject.get("price")+"????????????????????");
            if (customObject.get("price").toString().equals("null")) {
                Log.d("Chk","null");
                typePriceList = null;
            }else{
                JSONArray typePriceArray = (JSONArray) customObject.get("price");
                for (int j = 0; j < typePriceArray.length(); j++) {
                    typePriceList.add(typePriceArray.getString(j));
                }
            }
            CustomInfo customInfo = new CustomInfo(customObject.getString("_id"),
                    customObject.getString("name"),
                    typeList,
                    typePriceList);

            return customInfo;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 영현이 함수
     */
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
                list.add(categoryArray.getString(i));
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
                list.add(categoryArray.getString(i));
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

            //Log.d("Chk",result);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray customArray = (JSONArray) jsonObject.get("data");

            customId = customArray.getString(0);
            Log.d("Chk", customId);

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
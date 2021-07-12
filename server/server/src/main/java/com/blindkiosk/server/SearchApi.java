package com.blindkiosk.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchApi {

    /**
     *
     * @param x 위도
     * @param y 경도
     * @param radius 거리
     * @return
     */
    public List<String> search(double x, double y, int radius) throws ParseException {


        String apiURL_restaurant = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=FD6&x="+x+"&y="+y+"&radius="+radius+"&sort=distance"; // json 결과 //
        String apiURL_cafe = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=CE7&x="+x+"&y="+y+"&radius="+radius+"&sort=distance";
        String apiKey = "c420b0e6d0b3acc0a3348d7e95f11ead";
        String responseBody_restaurant = get(apiURL_restaurant, apiKey);
        String responseBody_cafe = get(apiURL_cafe, apiKey);
        List<String> restaurant=json(responseBody_restaurant);
        System.out.println(responseBody_restaurant);
        List<String> cafe=json(responseBody_cafe);
        List<String> store=new ArrayList<>();
        store.addAll(restaurant);
        store.addAll(cafe);
        return store;
    }


    private static String get(String apiUrl, String apiKey) {
        HttpURLConnection con = connect(apiUrl);
        String auth = "KakaoAK " + apiKey;
        con.setRequestProperty("Authorization", auth);
        try {
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) throws UnsupportedEncodingException {


        InputStreamReader streamReader = new InputStreamReader(body,"UTF-8");
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }

    }

    public static List<String> json(String answer) throws ParseException {
        List<String> imageList = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(answer);
        JSONArray array = (JSONArray) jsonObject.get("documents");
        if (array != null) {
            for (Object o : array) {
                JSONObject row = (JSONObject) o;
                String image = (String) row.get("place_name");
                imageList.add(image);
            }
        }


        return imageList;

    }

}

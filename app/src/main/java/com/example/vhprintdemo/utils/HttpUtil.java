package com.example.vhprintdemo.utils;

import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    private static final String TAG = HttpUtil.class.getSimpleName();

    public static String get(final String strUrl) {

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int response = conn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();
                return dealResponseResult(stream);

            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doDelete(String urlStr,String params){
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("DELETE");

            //获得一个输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(params.getBytes());

            int response = conn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();
                return dealResponseResult(stream);

            }else{
                Log.d(TAG,"<<<<<response="+response);
                return null;
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }



    public static String post(final String strUrl, String params) {

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");

            //设置请求体的类型是文本类型
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            //设置请求体的长度
            conn.setRequestProperty("Content-Length",String.valueOf(params.getBytes().length));

            //获得一个输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(params.getBytes());

            int response = conn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();
                return dealResponseResult(stream);

            }else{
                Log.d(TAG,"<<<<<response="+response);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String put(final String strUrl, String params) {
        try{
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("PUT");

            //获得一个输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(params.getBytes());

            int response = conn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();
                return dealResponseResult(stream);

            }else{
                Log.d(TAG,"<<<<<response="+response);
                return null;
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String dealResponseResult(InputStream stream) throws IOException{
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        return buffer.toString();
    }


}

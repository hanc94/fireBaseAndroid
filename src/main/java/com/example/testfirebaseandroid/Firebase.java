
package com.example.testfirebaseandroid;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class Firebase {

    private String project;
    private final String host=".firebaseio.com/";
    private String secret;
    public Firebase(String iProject,String iSecret){
        project=iProject;
        secret=iSecret;
    }

    public String read(String document){

        URL url = null;
        String str=null;
        try {
            url = new URL("https://"+project+host+document+".json?auth="+secret);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;

        BufferedReader bufferedReader = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestMethod("GET");



            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        } catch (IOException exception) {
            exception.printStackTrace();

        } finally {
            if (bufferedReader != null) {
                try {

                    str=(bufferedReader.readLine());

                } catch (IOException exception) {
                    exception.printStackTrace();

                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return str;

    }


    public int write(String document,String value){

        URL url = null;
        int rcode=-1;
        try {
            url = new URL("https://"+project+host+document+".json?auth="+secret);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;



        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(value.length()));


            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("PUT");


            bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));

            bufferedWriter.write(value);
            bufferedWriter.flush();

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (bufferedWriter != null ) {
                try {
                    rcode=httpURLConnection.getResponseCode();
                    bufferedWriter.flush();
                    bufferedWriter.close();

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return rcode;
    }

}

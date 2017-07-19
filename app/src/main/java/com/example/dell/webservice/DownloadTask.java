package com.example.dell.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DELL on 7/18/2017.
 */

class DownloadTask extends AsyncTask<String , Void, String> {
    //variables
    private Context context;
    private TextView resultTextview;
    private final static String GET_REQUEST_TYPE= "GET";
    private final static int CONNECTION_TIME_OUT= 1000*60*60;//IN MILLISECS
    private final static int READ_TIME_OUT= 1000*60*60;
    private final static String TAG= DownloadTask.class.getSimpleName();


    public DownloadTask(Context context, TextView resultTextview){

        this.context=context;
        this.resultTextview=resultTextview;

    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection httpURLConnection=null;
        try
        {
            //1.create URL
            URL webURL= new URL(strings[0]);
            // 2.HTTPURLConnection
            httpURLConnection= (HttpURLConnection) webURL.openConnection();
            //3.set Headers
            httpURLConnection.setRequestMethod(GET_REQUEST_TYPE);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            //4.make connection
            httpURLConnection.connect();//long running : goes to the web server , makes the request, and come with the response , where the response is available inside the httpURLConnection object
            //5. check if response code is OK
            int responseCode=httpURLConnection.getResponseCode();
            if(HttpURLConnection.HTTP_OK==responseCode)
            {
                //6.if true, you have the raw data here
                InputStream inputStream=httpURLConnection.getInputStream();
                String response=convertInputStringIntoString(inputStream);
                return response;
            }
        }
        catch (MalformedURLException e) {
            Log.d(TAG,"MalformedURLException  "+ e.getMessage());
            e.printStackTrace();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            Log.d(TAG,"IOException :  "+e.getMessage());
            e.printStackTrace();
            httpURLConnection.disconnect();
        }finally {
            httpURLConnection.disconnect();
        }


        return null;
    }

    private String convertInputStringIntoString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String line= new String();
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) != null)
        {
            stringBuilder =stringBuilder.append(line);
        }
        // convert string builder to string and return response
        String data= stringBuilder.toString();
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(null == s)
        {
            resultTextview.setText("No response");
        }
        else
        {
            resultTextview.setText(s);
        }
    }
}

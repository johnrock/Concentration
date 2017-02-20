package com.sho.hire.hw.piserjohnmemory.helpers;

import android.os.AsyncTask;

import com.sho.hire.hw.piserjohnmemory.util.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;

/**
 * @author John Piser johnpiser@yahoo.com
 *
 *  Helper class for making HTTP requests off of the UI thread
 */

@Singleton
public class HttpHelper {

    public interface ResponseListener{
        void handleResponseInBackground(String url, String response);
        void handleResponseCompleteOnUiThread(String url, String response);
    }

    public static final String GET = "GET";

    LogHelper logHelper;

    @Inject
    public HttpHelper(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

    public void get(final ResponseListener responseListener, final String url){

        if(responseListener == null || url == null || url.length() < 8){
            logHelper.error(Constants.LOGTAG, "Error: Unable to make HTTP request with responseListener:["+responseListener+"] and url[" + url + "]");
            return;
        }

        AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                URL urlObj;
                try {
                    urlObj = new URL(url);
                } catch (MalformedURLException e) {
                    logHelper.error(Constants.LOGTAG, "Error: Unable to make HTTP request with MalformedUrl:["+url+"]");
                    return null;
                }

                StringBuilder response = new StringBuilder();

                try {
                    HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();

                    con.setRequestMethod(GET);
                    con.setDoOutput(true);
                    DataOutputStream dataOutputStream = new DataOutputStream(con.getOutputStream());

                    int responseCode = con.getResponseCode();

                    logHelper.debug(Constants.LOGTAG, "...making GET request to url: " + url);
                    logHelper.debug(Constants.LOGTAG, "responseCode: " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(response != null){
                    //handle any JSON parsing in the same background thread
                    responseListener.handleResponseInBackground(url, response.toString());
                }
                return response != null ? response.toString() : null;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                logHelper.debug(Constants.LOGTAG, " in onPostExecute with result: " + response);
                responseListener.handleResponseCompleteOnUiThread(url, response);

            }
        }.execute();




    }
}

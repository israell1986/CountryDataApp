package com.example.israellorenz.countrydata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by israel.lorenz on 08/09/2017.
 */

class GetJsonTask extends AsyncTask<String, String, String> {

    private Context context;
    private ProgressDialog pd;
    private GetJsonTaskListener listener;

    public interface GetJsonTaskListener {
        void gotJsonSuccess(String json);
        void gotJsonFailed();
    }

    public GetJsonTask(Activity activity, GetJsonTaskListener listener) {
        context = activity;
        this.listener = listener;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.gotJsonFailed();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pd.isShowing()) {
            pd.dismiss();
        }
        if (listener != null) {
            if (result == null) {
                listener.gotJsonFailed();
            } else if (listener != null) {
                listener.gotJsonSuccess(result);
            }
        }

    }
}

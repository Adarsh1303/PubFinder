package com.example.petr_panda.application;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by petr_panda on 9/7/17.
 */
public class DownloadUrl {

    public String readUrl(String strUrl) throws IOException
    {

            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            inputStream = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            Log.d("downloadUrl", data.toString());
            bufferedReader.close();
        }catch (Exception e)
        {
            Log.d("Exception",e.toString());
        }finally {
                inputStream.close();
                urlConnection.disconnect();
        }
        return data;
    }
}

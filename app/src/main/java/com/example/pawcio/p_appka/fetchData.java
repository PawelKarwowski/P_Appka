package com.example.pawcio.p_appka;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class fetchData extends AsyncTask<Void, Void, Void> {
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://ckan2.multimediagdansk.pl/delays?stopId=1340");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject obj = new JSONObject(data);
            JSONArray arr = obj.getJSONArray("delay");
            for (int i = 0; i < arr.length(); i++)

            {
                String estimatedTime = arr.getJSONObject(i).getString("estimatedTime");
                String headsign = arr.getJSONObject(i).getString("headsign");
                String routeId = arr.getJSONObject(i).getString("routeId");
                String theoreticalTime = arr.getJSONObject(i).getString("theoreticalTime");

                DateFormat f = new SimpleDateFormat("hh:mm");
                Date d1 = f.parse(theoreticalTime);
                Date d2 = f.parse(estimatedTime);
                long difference = (d1.getTime() - d2.getTime())/60000;
                String min_dif = Long.toString(difference);


                singleParsed =  routeId + " -> " + headsign + "\n" +
                                "Odjedzie za: " + min_dif + " min" + "\n";

                dataParsed = dataParsed + singleParsed + "\n";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.fetcheddata.setText(this.dataParsed);
    }
}

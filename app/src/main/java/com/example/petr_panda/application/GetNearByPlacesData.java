package com.example.petr_panda.application;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by petr_panda on 9/7/17.
 */
public class GetNearByPlacesData extends AsyncTask<Object,String,String> {

    String googlePlacedata;
    GoogleMap mGoogleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {

        try {

            mGoogleMap = (GoogleMap)objects[0];
            url = (String)objects[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacedata = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask","doInBackgroundExit");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return googlePlacedata;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d("GooglePlaceReadTask","onPostExecuteStarted");
        List<HashMap<String,String>> nearbyPlaceList = null;
        DataParser dataParser = new DataParser();
        nearbyPlaceList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlaceList);
        if(nearbyPlaceList.size()>0)
        {
            MapsActivity.goButton.setVisibility(View.VISIBLE);
        }
        else
        {
            MapsActivity.goButton.setVisibility(View.INVISIBLE);
        }
        Log.d("GooglePlacesReadTask","onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList)
    {

        MapsActivity.googlePlaces = nearbyPlaceList;
        for(int i = 0 ; i < nearbyPlaceList.size() ; i++)
        {
            Log.d("OnPostExecute","Showing places now");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace = nearbyPlaceList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lon = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat,lon);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mGoogleMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        }
    }

}

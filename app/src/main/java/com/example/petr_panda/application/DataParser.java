package com.example.petr_panda.application;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by petr_panda on 9/7/17.
 */
public class DataParser {

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try{

            Log.d("Places","Parse");
            jsonObject = new JSONObject((String)jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  getPlaces(jsonArray);


    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray) {
        int placeCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "Adding Places");

        for (int i = 0; i < placeCount; i++)
        {
            try{

                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places","Adding Places");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String,String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String,String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String rating = "-NA-";
        String phone_Number = "-NA-";
        String type = "-NA-";
//        String timing = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        Log.d("getPlace","Entered");

        try{
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            if (!googlePlaceJson.isNull("formatted_phone_number")) {
                phone_Number = googlePlaceJson.getString("formatted_phone_number");
            }

            if (!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getString("rating");
            }

            if (!googlePlaceJson.isNull("types")) {
                String type1 = googlePlaceJson.getString("types");
                StringBuilder sb = new StringBuilder(type1);
                sb.deleteCharAt(0);
                sb.deleteCharAt(sb.length()-1);
                String parts[] = sb.toString().split(",");
                if(parts.length>1) {
                    type = parts[0]  + parts[1];
                }
                else
                {
                    type = parts[0];
                }
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("phone_number", phone_Number);
            googlePlaceMap.put("rating", rating);
            googlePlaceMap.put("type", type);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            Log.d("getPlace", "Putting Places");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}

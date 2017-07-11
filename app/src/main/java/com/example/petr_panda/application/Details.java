package com.example.petr_panda.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by petr_panda on 9/7/17.
 */
public class Details extends Activity {

    TextView name,address,type,rating,phone_number;
    Button save_button;
    String name_var,address_var,types,ratings,phone_numbers;
    DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pubdetails);
        name = (TextView)findViewById(R.id.name_detail);
        address = (TextView)findViewById(R.id.address_detail);
        type = (TextView)findViewById(R.id.type_detail);
        rating = (TextView)findViewById(R.id.rating_detail);
        phone_number = (TextView)findViewById(R.id.phone_detail);
        save_button = (Button)findViewById(R.id.save);
        dbHandler = new DBHandler(this,null,null,1);
        Intent intent = getIntent();
        int index = intent.getIntExtra("Index",2);
        populate_details(index);
    }


    private void populate_details(int index) {

        HashMap<String,String> googlePlace = MapsActivity.googlePlaces.get(index);
        name_var =  googlePlace.get("place_name");
        address_var =  googlePlace.get("vicinity");
        types =  googlePlace.get("type");
        ratings =  googlePlace.get("rating");
        phone_numbers =  googlePlace.get("phone_number");
        name.setText("Name : " +name_var);
        address.setText("Address :" +address_var);
        type.setText("Type : " +types);
        rating.setText("Rating : " +ratings);
        phone_number.setText("PhoneNumber : " +phone_numbers);
    }

    public void save_place(View view)
    {
        Place place = new Place(name_var,address_var,types,ratings,phone_numbers);
        dbHandler.addplace(place);
        Toast.makeText(this,"Place Added",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Details.this,Front.class);
        finish();
        startActivity(intent);
    }
}

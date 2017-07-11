package com.example.petr_panda.application;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by petr_panda on 11/7/17.
 */
public class Offline extends Activity {

    TextView name,address,type,rating,phone_number;
    DBHandler dbHandler;
    Cursor c;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.offline);
//        name = (TextView)findViewById(R.id.offline_name_detail);
//        dbHandler = new DBHandler(this,null,null,1);
//        Intent intent = getIntent();
//        String index = intent.getStringExtra("Index");
//        c = dbHandler.get_place_detail(index);
//        c.moveToFirst();
//        Toast.makeText(this,c.getString(1),Toast.LENGTH_LONG).show();
//    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.offline);
        dbHandler = new DBHandler(this,null,null,1);
        name = (TextView)findViewById(R.id.offline_name_detail);
        address = (TextView)findViewById(R.id.offline_address_detail);
        type = (TextView)findViewById(R.id.offline_type_detail);
        rating = (TextView)findViewById(R.id.offline_rating_detail);
        phone_number = (TextView)findViewById(R.id.offline_phone_detail);
        Intent intent = getIntent();
        String index = intent.getStringExtra("Index");
        c = dbHandler.get_place_detail(index);
        c.moveToFirst();
//        Toast.makeText(this,c.getString(1),Toast.LENGTH_LONG).show();
            populate();
//        name.setText(c.getString(1));

    }

    public void populate() {

        name.setText("Name : " +c.getString(1));
        address.setText("Address :" +c.getString(2));
        type.setText("Type : " +c.getString(3));
        rating.setText("Rating : " +c.getString(4));
        phone_number.setText("PhoneNumber : " +c.getString(5));
    }
}



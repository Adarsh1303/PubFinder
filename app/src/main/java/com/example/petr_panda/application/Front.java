package com.example.petr_panda.application;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by petr_panda on 11/7/17.
 */
public class Front extends Activity {

    Button search_button;
    ListView listView;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front);
        search_button = (Button)findViewById(R.id.search_button);
        listView = (ListView)findViewById(R.id.placeList);
        dbHandler = new DBHandler(this,null,null,1);
        populate();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String place = (String)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(Front.this,Offline.class);
                intent.putExtra("Index",place);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),place,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populate() {

        Cursor cursor = dbHandler.getall();
        ArrayList<String> thelist =  new ArrayList<>();
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_LONG).show();

        }

        else{
            cursor.moveToFirst();
            do {
                thelist.add(cursor.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,thelist);
                listView.setAdapter(listAdapter);

            }while(cursor.moveToNext());
        }

    }

    public void search(View view)
    {
        Intent intent = new Intent(Front.this,MapsActivity.class);
        startActivity(intent);
    }


}

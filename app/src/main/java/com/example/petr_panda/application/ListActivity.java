package com.example.petr_panda.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by petr_panda on 9/7/17.
 */
public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publist);
        populateList();
        registerClick();
    }

    private void registerClick() {

        ListView listView = (ListView)findViewById(R.id.pubList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView)view;
                String message = ((TextView) view).getText().toString();
                Intent intent = new Intent(ListActivity.this,Details.class);
                intent.putExtra("Index",i);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateList() {

        String [] place_names = new String[MapsActivity.googlePlaces.size()];
        for(int i = 0 ; i < MapsActivity.googlePlaces.size();i++)
        {
            place_names[i] = MapsActivity.googlePlaces.get(i).get("place_name");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.itempub,place_names);
        ListView listView = (ListView)findViewById(R.id.pubList);
        listView.setAdapter(adapter);
    }
}

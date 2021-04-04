package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Tricks extends AppCompatActivity {

    ListView simpleList;
    String trick[] = {"Online","During Parties","On a date","Walking alone","At home"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tricks);

        getSupportActionBar().setTitle("Tips for you");
        simpleList = (ListView) findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), trick);
        simpleList.setAdapter(customAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Tricks.this,TrickDisplay.class);
                //String p = String.valueOf(position);
                i.putExtra("tipname",trick[position]);
                i.putExtra("pos",position);
                startActivity(i);
            }
        });
    }
}

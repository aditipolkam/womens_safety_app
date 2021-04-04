package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        getSupportActionBar().setTitle("Instructions");
    }
    public void back(View v) {
        Intent i_back=new Intent(Instructions.this,Start.class);
        startActivity(i_back);
    }
}

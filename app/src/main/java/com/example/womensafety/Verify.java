package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Verify extends AppCompatActivity {

    String usermail,username,userpass,emailPattern;
    EditText email,name,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        getSupportActionBar().setTitle("My Profile");
        email = (EditText)findViewById(R.id.userEmail);
        name = (EditText)findViewById(R.id.userName);
        pass = (EditText)findViewById(R.id.userPassword);

        emailPattern = "/^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$/";
    }
    public void verify_no(View v) {
        username = name.getText().toString().trim();
        usermail = email.getText().toString().trim();
        userpass = pass.getText().toString().trim();

        if (username.isEmpty()) {
            name.setError("Please enter your name.");
            name.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(usermail).matches()) {
            email.setError("Please enter a valid email.");
            email.requestFocus();
        }
        else if (userpass.isEmpty()) {
            pass.setError("Please enter the password.");
            pass.requestFocus();
        }
        else
        {
            SQLiteDatabase db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS source(username varchar,email varchar,password varchar);");
            db.execSQL("DELETE FROM source");
            db.execSQL("INSERT INTO source VALUES('"+username+"','"+usermail+"','"+userpass+"');");
            Toast.makeText(getApplicationContext(), "Registration successful.",Toast.LENGTH_SHORT).show();
            db.close();
            back(v);
        }
    }
    public void back(View view)
    {
        Intent intent=new Intent(Verify.this, Start.class);
        startActivity(intent);
    }
}

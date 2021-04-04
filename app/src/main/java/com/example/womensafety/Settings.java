package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private LinearLayout update,register,view;
    private TextView textView,name,email;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");

        update=(LinearLayout) findViewById(R.id.btn_updateProfile);
        register=(LinearLayout) findViewById(R.id.btn_registerGuardians);
        view=(LinearLayout) findViewById(R.id.btn_viewGuardians);
        name=(TextView)findViewById(R.id.tv_settings_name);
        email=(TextView)findViewById(R.id.tv_settings_email);
        textView = (TextView) findViewById(R.id.main_tv);

        sqLiteDatabase=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS source(username varchar,email varchar,password varchar);");
        cursor=sqLiteDatabase.rawQuery("SELECT * FROM source",null);
        cursor.moveToFirst();
        name.setText(cursor.getString(0));
        email.setText(cursor.getString(1));
        cursor.close();
        sqLiteDatabase.close();

        textView.setText("from RESCUER");
        TextPaint paint = textView.getPaint();
        float width = paint.measureText("from RESCUER");
        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this,Verify.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this,Register.class);
                startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this,Display.class);
                startActivity(i);
            }
        });
    }
}

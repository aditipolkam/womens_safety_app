package com.example.womensafety;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Register extends AppCompatActivity {

    EditText name,number,email;
    String str_name,str_number,str_email;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    int Uid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register Guardians");

        name = (EditText) this.findViewById(R.id.guardian_name);
        number = (EditText) this.findViewById(R.id.guardian_phone);
        email = (EditText) this.findViewById(R.id.guardian_email);

        Intent i = getIntent();
        Uid = i.getIntExtra("ID",0);
        if(Uid != 0)
        {
            sqLiteDatabase=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(id integer primary key,name VARCHAR,number VARCHAR, email VARCHAR);");
            String sql = "SELECT * FROM details where id = "+ Uid +";";
            cursor=sqLiteDatabase.rawQuery(sql, null);
            if(cursor.moveToNext())
            {
                name.setText(cursor.getString(1));
                number.setText(cursor.getString(2));
                email.setText(cursor.getString(3));
            }
            cursor.close();
            sqLiteDatabase.close();
        }
    }

    public void storeInDB(View v) {

        boolean ans=check();
        if(ans == true)
        {
            if(Uid != 0) {
                editDB();
                return;
            }
            SQLiteDatabase db;
            db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS details(id integer primary key,name VARCHAR,number VARCHAR,email VARCHAR);");
            Cursor c=db.rawQuery("SELECT * FROM details", null);
            int count = c.getCount();
            if(count<5)
            {
                db.execSQL("INSERT INTO details VALUES('"+(count+1)+"','"+str_name+"','"+str_number+"','"+str_email+"');");
                Toast.makeText(getApplicationContext(), "Guardian Registered.",Toast.LENGTH_SHORT).show();
                sendSMS(str_name,str_number);
                onBackPressed();
            }
            else {
                Toast.makeText(getApplicationContext(), "Maximum limit reached. Please delete previous guardians to register new.",Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    private void editDB() {
        SQLiteDatabase db;
        db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",str_name);
        contentValues.put("number",str_number);
        contentValues.put("email",str_email);
        db.update("details",contentValues,"id = ?",new String[]{String.valueOf(Uid)});
        Toast.makeText(getApplicationContext(), "Details edited successfully.",Toast.LENGTH_SHORT).show();
        onBackPressed();
        db.close();
        Intent data = new Intent();
        //data.setData(Uri.parse())
        setResult(RESULT_OK,data);
        finish();
    }

    public boolean check() {
        str_name = name.getText().toString().trim();
        str_number = number.getText().toString().trim();
        str_email = email.getText().toString().trim();

        if (str_name.isEmpty()) {
            name.setError("Please enter name.");
            name.requestFocus();
            return false;
        } else if (str_number.isEmpty()) {
            number.setError("Please enter phone number.");
            number.requestFocus();
            return false;
        } else if (str_number.length() != 10) {
            number.setError("Please enter valid number.");
            number.requestFocus();
            return false;
        } else if (str_email.isEmpty()) {
            email.setError("Please enter email id.");
            email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            email.setError("Please enter valid email id.");
            email.requestFocus();
            return false;
        }
        return true;
    }
    public void sendSMS(String name, String number)
    {
        sqLiteDatabase=openOrCreateDatabase("NumDB",Context.MODE_PRIVATE,null);
        cursor=sqLiteDatabase.rawQuery("SELECT username FROM source",null);
        while (cursor.moveToNext())
        {
            String userName=cursor.getString(0);
            String msg="The sender of this message i.e. " +userName+ " has registered your phone number on the Rescuer women's safety app.";
            int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if(permissionCheck== PackageManager.PERMISSION_GRANTED)
            {
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(number,null,msg,null,null);
                Toast.makeText(this, "Message sent.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
            }
            break;
        }


    }
}

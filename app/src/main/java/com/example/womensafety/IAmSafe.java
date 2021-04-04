package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IAmSafe extends AppCompatActivity {

    private EditText safeMessage;
    private Button editText,sendText;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    String number;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_am_safe);

        getSupportActionBar().setTitle("Safe Status");
        safeMessage=(EditText)findViewById(R.id.et_safe_msg);
        safeMessage.setEnabled(false);

        editText=(Button) findViewById(R.id.btn_editText);
        sendText=(Button)findViewById(R.id.btn_sendText);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeMessage.setEnabled(true);
                safeMessage.requestFocus();
            }
        });
        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE,null);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(id integer primary key,name VARCHAR,number VARCHAR,email VARCHAR);");
                cursor=sqLiteDatabase.rawQuery("SELECT * FROM details",null);
                while (cursor.moveToNext())
                {
                    number=cursor.getString(2);
                    sendSMS(number);
                }
                if (cursor.getCount()<=0)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(IAmSafe.this);
                    builder.setTitle("No guardians found.");
                    builder.setMessage("Please register guardians and try again.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //dialogInterface.dismiss();
                            Intent intent = new Intent(IAmSafe.this,Register.class);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        });
    }
    public void sendSMS(String number)
    {
        String msg=safeMessage.getText().toString().trim();
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
    }
}

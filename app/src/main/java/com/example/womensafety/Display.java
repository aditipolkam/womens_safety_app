package com.example.womensafety;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Display extends AppCompatActivity {

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> number = new ArrayList<String>();
    ArrayList<String> email = new ArrayList<String>();
    ListView simpleList;
    int i=0,idPos;
    Cursor c;
    DisplayAdapter adapter;
    final int REQ_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        getSupportActionBar().setTitle("Guardians");
        simpleList = (ListView) findViewById(R.id.simpleListView);
        getGuardianDetails();
    }

    private void getGuardianDetails() {
        SQLiteDatabase db;
        db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS details(id integer primary key,name VARCHAR,number VARCHAR, email VARCHAR);");

        c=db.rawQuery("SELECT * FROM details", null);
        if(c.getCount()==0) {
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            while(c.moveToNext())
            {
                name.add(c.getString(1));
                number.add(c.getString(2));
                email.add(c.getString(3));
                i++;
            }

            adapter = new DisplayAdapter(getApplicationContext(), name,number,email);
            simpleList.setAdapter(adapter);

            simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    idPos = position+1;
                    showMessage("Edit/Delete","Your Guardian: "+name.get(position));
                }
            });
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dialogInterface.dismiss();
                Intent intent = new Intent(Display.this,Register.class);
                intent.putExtra("ID",idPos);
                startActivityForResult(intent,REQ_CODE);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase db;
                db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
                String sql = "delete from details where id= '"+idPos+"';";
                db.execSQL(sql);
                name.clear();
                number.clear();
                email.clear();
                getGuardianDetails();
                adapter.notifyDataSetChanged();
                simpleList.invalidateViews();
                simpleList.refreshDrawableState();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                getGuardianDetails();
            }
        }
    }
}

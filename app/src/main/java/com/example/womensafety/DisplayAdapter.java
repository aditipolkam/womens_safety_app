package com.example.womensafety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DisplayAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> number = new ArrayList<String>();
    ArrayList<String> email = new ArrayList<String>();
    LayoutInflater inflter;

    public DisplayAdapter(Context applicationContext, ArrayList<String> name,ArrayList<String> number,ArrayList<String> email) {
        this.context = applicationContext;
        this.name = name;
        this.number = number;
        this.email = email;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.display_list, null);
        TextView tv_name = (TextView)view.findViewById(R.id.name);
        TextView tv_number = (TextView)view.findViewById(R.id.number);
        TextView tv_email = (TextView)view.findViewById(R.id.email);
        tv_name.setText("Name: "+name.get(i));
        tv_number.setText("Contact: "+number.get(i));
        tv_email.setText("Email: "+email.get(i));
        return view;
    }
}

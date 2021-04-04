package com.example.womensafety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context context;
    String trick[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] trick) {
        this.context = applicationContext;
        this.trick = trick;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return trick.length;
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
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView item = (TextView)view.findViewById(R.id.trick);
        item.setText(trick[i]);
        return view;
    }
}

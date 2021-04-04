package com.example.womensafety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
    Context context;
    int logos[];
    String titles[];
    LayoutInflater inflter;

    public GridAdapter(Context applicationContext, int[] logos,String titles[])
    {
        this.context = applicationContext;
        this.logos = logos;
        this.titles = titles;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return  logos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.activity_gridview, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        icon.setImageResource(logos[i]);
        title.setText(titles[i]);
        return view;
    }
}

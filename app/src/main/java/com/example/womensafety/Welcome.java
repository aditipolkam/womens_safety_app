package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private TextView[]dotstv;
    private int[]layouts;
    private Button btnSkip;
    private Button btnNext;
    private MyPageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(!isFirstTimeStartApp())
        {
            startMainActivity();
            finish();
        }
        setContentView(R.layout.activity_welcome);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        layoutDot=(LinearLayout)findViewById(R.id.dotLayout);
        btnNext=(Button)findViewById(R.id.btn_next);
        btnSkip=(Button)findViewById(R.id.btn_skip);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage=viewPager.getCurrentItem()+1;
                if(currentPage<layouts.length)
                {
                    viewPager.setCurrentItem(currentPage);
                }
                else
                {
                    startMainActivity();
                }
            }
        });
        layouts=new int[]{R.layout.slider1,R.layout.slider2,R.layout.slider5,R.layout.slider3,R.layout.slider4};
        pageAdapter=new MyPageAdapter(layouts,getApplicationContext());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==layouts.length-1)
                {
                    btnNext.setText("START");
                }
                else
                {
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }
    private boolean isFirstTimeStartApp()
    {
        SharedPreferences ref=getApplicationContext().getSharedPreferences("IntroSlider", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag",true);
    }
    private void setFirstTimeStartStatus(boolean stt)
    {
        SharedPreferences ref=getApplicationContext().getSharedPreferences("IntroSlider", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=ref.edit();
        editor.putBoolean("FirstTimeStartFlag",stt);
        editor.commit();
    }

    private void setDotStatus(int page)
    {
        layoutDot.removeAllViews();
        dotstv=new TextView[layouts.length];
        for (int i=0; i<dotstv.length; i++)
        {
            dotstv[i]=new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#4b87a2"));
            layoutDot.addView(dotstv[i]);
        }
        if (dotstv.length>0)
        {
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }
    private void startMainActivity()
    {
        setFirstTimeStartStatus(false);
        SQLiteDatabase db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS source(username varchar,email varchar,password varchar);");
        String isEmpty="SELECT * FROM source";
        Cursor cursor=db.rawQuery(isEmpty,null);
        int count=cursor.getCount();
        if (count<=0)
        {
            Intent intent=new Intent(Welcome.this, Verify.class);
            startActivity(intent);
        }
        else
        {
            Intent intent=new Intent(Welcome.this, Start.class);
            startActivity(intent);
        }
    }
}

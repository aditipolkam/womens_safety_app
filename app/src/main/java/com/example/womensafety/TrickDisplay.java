package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TrickDisplay extends AppCompatActivity {

    String trick[] = new String[]{"Online","During Parties","On a date","Walking alone","At home"};

    String online[] = new String[]{
            "1. Don't share personal information with strangers.",
            "2. Don't share your location online.",
            "3. Don't share private pictures online.",
            "4. Follow online dating safety tips."
    };
    String parties[] = new String[]{
            "1. Be ready to help others if necessary.",
            "2. Go with friends.",
            "3. Limit or avoid alcohol consumption.",
            "4. Don't leave your drink unattended."
    };
    String date[] = new String[] {
            "1. Choose carefully who you are going on a date with.",
            "2. Choose public spaces with people around.",
            "3. Set your limits.",
            "4. Inform atleast one friend about your whereabouts."
    };
    String alone[] = new String[] {
            "1. Be aware of the surroundings.",
            "2. Avoid isolated areas.",
            "3. Always bring your phone.",
            "4. Walk confident and trust your gut instinct."
    };
    String home[] = {
            "1. Don't give your address and phone number to strangers.",
            "2. Don't open the door to strangers.",
            "3. Have good locks and close the doors and windows.",
            "4. Draw curtains and shut blinds at night."
    };

    int images[] = {R.drawable.online_tips,R.drawable.party_tips,R.drawable.date_tips,R.drawable.alone_tips,R.drawable.home_tips};

    String tips[]={};

    ImageView tips_image;
    TextView tips_heading;
    ListView tips_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trick_display);

        Intent intent = getIntent();
        String position=intent.getStringExtra("tipname");

        tips_listview=(ListView)findViewById(R.id.tips);
        tips_image=(ImageView)findViewById(R.id.tips_img);

        if (position.equals("Online")) {
            getSupportActionBar().setTitle(position);
            tips = online;
        }
        else if (position.equals("During Parties")) {
            getSupportActionBar().setTitle(position);
            tips = parties;
        }
        else if (position.equals("On a date")) {
            getSupportActionBar().setTitle(position);
            tips = date;
        }
        else if(position.equals("Walking alone")) {
            getSupportActionBar().setTitle(position);
            tips = alone;
        }
        else if (position.equals("At home")) {
            getSupportActionBar().setTitle(position);
            tips = home;
        }

        tips_image.setImageResource(images[intent.getIntExtra("pos",0)]);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,tips);
        tips_listview.setAdapter(adapter);

    }
}

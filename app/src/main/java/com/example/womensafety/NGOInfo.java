package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NGOInfo extends AppCompatActivity {

    private ImageView imageView;
    private TextView title,info,contact,email,website;
    private String ngo_title,ngo_info,ngo_contact,ngo_email,ngo_website;
    int logos[]={R.drawable.ngo_actionaid,R.drawable.ngo_care,R.drawable.ngo_guria,R.drawable.ngo_majlis,R.drawable.ngo_pcvc,R.drawable.ngo_sskk};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_g_o_info);

        getSupportActionBar().setTitle("NGO information");

        final Intent intent=getIntent();
        ngo_title=intent.getStringExtra("ngo_title");
        ngo_info=intent.getStringExtra("ngo_info");
        ngo_contact=intent.getStringExtra("ngo_contact");
        ngo_email=intent.getStringExtra("ngo_email");
        ngo_website=intent.getStringExtra("ngo_website");

        title=(TextView)findViewById(R.id.tv_ngoTitle);
        title.setText(ngo_title);

        imageView=(ImageView) findViewById(R.id.iv_ngoImg);
        imageView.setImageResource(logos[intent.getIntExtra("position",0)]);

        info=(TextView)findViewById(R.id.tv_ngoInfo);
        info.setText(ngo_info);

        contact=(TextView)findViewById(R.id.tv_ngoContact);
        contact.setText("Contact us: "+ngo_contact);

        email=(TextView)findViewById(R.id.tv_ngoEmail);
        email.setText("Mail us: "+ngo_email);

        website=(TextView)findViewById(R.id.tv_ngoWebsite);
        website.setText("Visit our website: "+ngo_website);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(ngo_website));
                startActivity(intent1);
            }
        });
    }
}

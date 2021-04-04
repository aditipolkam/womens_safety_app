package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class NGO extends AppCompatActivity {

    private GridView gridView;
    int logos[]={R.drawable.ngo_actionaid,R.drawable.ngo_care,R.drawable.ngo_guria,R.drawable.ngo_majlis,R.drawable.ngo_pcvc,R.drawable.ngo_sskk};
    String titles[]={"ActionAid India","CARE India","Guria India","Majlis Manch","International Foundation for Crime Prevention and Victim Care (PCVC)","Shikshan ane Samaj Kalyan Kendra"};
    String websites[]={"https://www.actionaidindia.org/","https://www.careindia.org/","http://www.guriaindia.org/","https://www.majlislaw.com/","http://www.pcvconline.org/","https://sskkamreli.org/"};
    String info[]={
            "ActionAid partners with numerous organizations ranging from grass-roots organizations who actively engage with the most " +
                    "disadvantaged sections of society to large corporates who help make a considerable difference to the battle against " +
                    "empowerment through logistical, financial and other support",
            "CARE has been working in India for over 68 years, focusing on alleviating poverty and social exclusion. We do this " +
                    "through well-planned and comprehensive programmes in health, education, livelihoods and disaster preparedness and response. " +
                    "Our overall goal is the empowerment of women and girls from poor and marginalised communities, leading to improvement in " +
                    "their lives and livelihoods. ",
            "Guria is non-profit organization that is both dedicated to fighting child prostitution, second generation prostitution, " +
                    "and sex trafficking in Northern India, and to achieving freedom worldwide.",
            "Majlis Legal Centre is a forum for women's rights discourse and legal initiatives. We are a group of women lawyers and social " +
                    "activists committed to informing, educating and empowering women on their legal rights. Majlis offers legal services, " +
                    "conducts legal awareness trainings, engages in policy level interventions, public campaigns and public interest litigation " +
                    "in order to help women access justice.",
            "PCVC for women provides several necessary services like crisis management, legal advocacy, support and resource services. " +
                    "PCVC started the national domestic violence hotline to help women who are struggling with different forms of abuse. " +
                    "The organization also provides victims with legal representatives and support through referrals.",
            "Shikshan ane Samaj Kalyan Kendra is dedicated to helping women through activities like health, education, women empowerment, " +
                    "etc. It holds workshops about the cause and effects of domestic violence and takes the issues of violence to district " +
                    "level authorities and lower courts. "
    };
    String contact_no[]={"0120-4048250","0120-4048250","91-542-2504253","91-22-26662394 ","18001027282","+91 9427280028"};
    String email[]={"info@actionaidindia.org","info@careindia.org","info@guriaindia.org","majlislaw@gmail.com","info@pcvconline.org","info@new.sskkamreli.org"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_g_o);

        getSupportActionBar().setTitle("NGO's for Women");

        gridView=(GridView)findViewById(R.id.simpleGridView);
        GridAdapter gridAdapter=new GridAdapter(getApplicationContext(),logos,titles);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(NGO.this, NGOInfo.class);
                intent.putExtra("ngo_title",titles[position]);
                intent.putExtra("ngo_info",info[position]);
                intent.putExtra("ngo_contact",contact_no[position]);
                intent.putExtra("ngo_email",email[position]);
                intent.putExtra("ngo_website",websites[position]);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}

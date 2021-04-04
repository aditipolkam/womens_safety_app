package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class AudioEmail extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private String pathSave = "";
    Session session = null;
    Uri newUri;
    Context context;
    String rec,subject,textMessage,emailID,pass;
    final int REQUEST_PERMISSION_CODE = 1000;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private int i=0;
    private String guardian_address="",addresses="";
    private String recAd[] = {};
    InternetAddress[] internetAddresses = {};
    TextView status,timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_email);

        getSupportActionBar().setTitle("Email audio");

        status = findViewById(R.id.status);
        context = this;
        subject = "Women's Safety by Rescuer.";
        textMessage = "This an audio of the incident happened here with me.";

        pathSave = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/"
                + UUID.randomUUID().toString() + "recording.3gp";

        setUpMediaRecorder();
        try{
            mRecorder.prepare();
            mRecorder.start();
            Toast.makeText(this,"Recording started",Toast.LENGTH_LONG).show();
            new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRecorder.stop();
                                addToMediaLibrary();
                                status.setText("Audio Recording finished.");
                                sendMail();
                                mRecorder.reset();
                                mRecorder.release();
                            }
                        });
                    }
                    },10000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void getDetails() {
        sqLiteDatabase=openOrCreateDatabase("NumDB",Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS details(id integer primary key,name VARCHAR,number VARCHAR, email VARCHAR);");
        cursor=sqLiteDatabase.rawQuery("SELECT * FROM DETAILS",null);
        while (cursor.moveToNext())
        {
            if (cursor.isLast())
            {
                guardian_address=guardian_address+cursor.getString(3);
            }
            else
            {
                guardian_address=guardian_address+cursor.getString(3)+",";
            }
        }
    }

    private void getUser() {
        sqLiteDatabase=openOrCreateDatabase("NumDB",Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS source(username varchar,email varchar,password varchar);");
        cursor= sqLiteDatabase.rawQuery("Select * from source",null);
        cursor.moveToFirst();
        emailID= cursor.getString(1);
        pass = cursor.getString(2);
        cursor.close();
        sqLiteDatabase.close();
    }

    private void sendMail() {
        getUser();
        getDetails();
        Toast.makeText(this,"Sending Email...",Toast.LENGTH_LONG).show();
        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.port","465");

        session = Session.getDefaultInstance(props,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(emailID,pass);
            }
        });
        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    class RetreiveFeedTask extends AsyncTask<String,Void,String> {
        protected String doInBackground(String... params){
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailID));
                message.setSubject(subject);
                message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(guardian_address));

                BodyPart m1 = new MimeBodyPart();
                m1.setText(textMessage);
                MimeBodyPart m2 = new MimeBodyPart();
                String filename = pathSave;
                DataSource source = new FileDataSource(filename);
                m2.setDataHandler(new DataHandler(source));
                m2.setFileName(filename);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(m1);
                multipart.addBodyPart(m2);

                message.setContent(multipart);
                Transport.send(message);

                //Intent intent = new Intent(AudioEmail.this,Start.class);
                //startActivity(intent);
            }
            catch (MessagingException m) {
                m.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result)
        {
            Toast.makeText(AudioEmail.this,"Message sent to "+(cursor.getCount())+" guardians.",Toast.LENGTH_LONG).show();
        }
    }

    private void setUpMediaRecorder() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mRecorder.setOutputFile(pathSave);
    }

    protected void addToMediaLibrary()
    {
        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentValues contentValues = new ContentValues(4);
        long current = System.currentTimeMillis();
        contentValues.put(MediaStore.Audio.Media.TITLE,"audio" + "sample.3gp");
        contentValues.put(MediaStore.Audio.Media.DATE_ADDED,(int)(current/1000));
        contentValues.put(MediaStore.Audio.Media.MIME_TYPE,"audio/3gpp");
        contentValues.put(MediaStore.Audio.Media.DATA,pathSave);
        newUri = contentResolver.insert(base,contentValues);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,newUri));
    }
}

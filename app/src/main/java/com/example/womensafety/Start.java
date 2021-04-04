package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Start extends AppCompatActivity {

    private TextView name;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    int PERMISSION_ALL = 1;
    LocationManager mLocationManager;
    Cursor c;
    double latitude;
    double longitude;
    LocationTrack locationTrack;
    private String NUMBER = "";
    String message = "Emergency..I am in danger..I need help";
    Context context;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private Button fakeCall,alarmbtn,safe;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL); // PERMISSION_ALL = 1
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationTrack = new LocationTrack(Start.this);

        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            if (longitude == 0.0){
                showSettingsAlert();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Cannot access location.", Toast.LENGTH_SHORT).show();
        }

        setName();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if (hasPermissions(context, PERMISSIONS)) {

                    SQLiteDatabase db;
                    db = openOrCreateDatabase("NumDB", Context.MODE_PRIVATE, null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS details(id integer primary key,name VARCHAR,number VARCHAR, email VARCHAR);");
                    c = db.rawQuery("SELECT * FROM details", null);
                    if (c.getCount() == 0) {
                        return;
                    }
                    while (c.moveToNext()) {
                        double longitude = locationTrack.getLongitude();
                        double latitude = locationTrack.getLatitude();
                        try {
                            if (String.valueOf(longitude).contains("0")){
                                showSettingsAlert();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        Location location = locationTrack.getLocation();
                        if (location != null) {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(Start.this, Locale.getDefault());
                            StringBuilder stringBuilder = new StringBuilder();
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();
                                String sublocation = addresses.get(0).getSubLocality();
                                String premisea = addresses.get(0).getPremises();
                                stringBuilder.append(sublocation+"\n");
                                stringBuilder.append(premisea+"\n");
                                stringBuilder.append(postalCode+"\n");
                                stringBuilder.append(city+"\n");

                                String number = c.getString(2);
                                NUMBER = c.getString(2);
                                message = "Emergency, I am in danger, I need help at " +address;
                            }
                            catch (IOException e) {
                                e.printStackTrace();

                                String number = c.getString(2);
                                NUMBER = c.getString(2);
                                message = "Emergency, I am in danger, I need help";
                            }
                        } else {
                            String number = c.getString(2);
                            NUMBER = c.getString(2);
                            message = "Emergency, I am in danger, I need help";
                        }
                        SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(NUMBER,null,message,null,null);
                        Toast.makeText(Start.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+NUMBER));
                    startActivity(intent);
                }
                else {
                    ActivityCompat.requestPermissions(Start.this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        });

        safe = (Button)findViewById(R.id.safeStatus);
        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Start.this,IAmSafe.class);
                startActivity(intent);
            }
        });

        fakeCall=(Button) findViewById(R.id.fakeCall);
        fakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Start.this,RecieveCall.class);
                startActivity(intent);
            }
        });

        final MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.policesiren);
        alarmbtn=(Button) findViewById(R.id.playAlarm);
        alarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    alarmbtn.setText("PLAY ALARM");
                }
                else
                {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    alarmbtn.setText("PAUSE ALARM");
                }
            }
        });
    }

    private void setName() {
        name=(TextView)findViewById(R.id.tv_name);
        SQLiteDatabase db=openOrCreateDatabase("NumDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS source(username varchar,email varchar,password varchar);");
        String fetchName="SELECT * FROM source";
        Cursor cursor=db.rawQuery(fetchName,null);
        cursor.moveToFirst();
        String userName=cursor.getString(0);
        name.setText("Welcome "+userName);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onStop();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL) {
            for (int i = 0; i < permissions.length; i++) {

                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (i == permissions.length - 1) {

                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        locationTrack = new LocationTrack(Start.this);

                    } else {
                        ActivityCompat.requestPermissions(Start.this, PERMISSIONS, PERMISSION_ALL);
                    }
                }
            }
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Start.this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Start.this.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){

            case R.id.action_settings:
                i = new Intent(this, com.example.womensafety.Settings.class);
                startActivity(i);
                return true;

            case R.id.action_instructions:
                i = new Intent(this,Instructions.class);
                startActivity(i);
                return true;

            case R.id.action_tips:
                i = new Intent(this,Tricks.class);
                startActivity(i);
                return true;

            case R.id.action_ngo:
                i = new Intent(this,NGO.class);
                startActivity(i);
                return true;

            case R.id.action_audio:
                i = new Intent(this,AudioEmail.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

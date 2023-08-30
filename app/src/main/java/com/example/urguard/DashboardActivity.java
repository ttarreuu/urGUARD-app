package com.example.urguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class DashboardActivity extends AppCompatActivity {

    ImageButton btn_setting, btn_sos, btn_message, btn_location;
    private LocationRequest locationRequest;
    String locationUrl;
    user user;

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        turnOnGPS();
        // Ambil data dari sqlite
        DBAdapter dbAdapter = new DBAdapter(this.getBaseContext());
        user = dbAdapter.getUser();

        btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btn_sos = findViewById(R.id.btn_sos);
        btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SOSActivity.class);
                startActivity(intent);
            }
        });



        ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        // SMS
        btn_message = findViewById(R.id.btn_message);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS(user, user.getMessage(), false);
            }
        });

        // Location
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(view -> {
            Toast.makeText(this, "Sending location...", Toast.LENGTH_SHORT).show();
            getCurrentLocation(user);
        });


        // SOS
        btn_sos = findViewById(R.id.btn_sos);
        btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, SOSActivity.class);
                startActivity(intent);
            }
        });

    }

    public void sendSMS(user user, String message, boolean isLocation){
        message += " (sent via urGUARD)";
        String contact_1 = user.getContact_1();
        String contact_2 = user.getContact_2();
        String contact_3 = user.getContact_3();

        SmsManager mySmsManager = SmsManager.getDefault();
        int ct = 0;
        try {
            if (contact_1 == null || contact_1.length() == 0) throw new Error();
            mySmsManager.sendTextMessage(contact_1, null, message, null, null);
            ct++;
        } catch (Error ignored) {

        }
        try {
            if (contact_2 == null || contact_2.length() == 0) throw new Error();
            mySmsManager.sendTextMessage(contact_2, null, message, null, null);
            ct++;
        } catch (Error ignored) {

        }
        try {
            if (contact_3 == null || contact_3.length() == 0) throw new Error();
            mySmsManager.sendTextMessage(contact_3, null, message, null, null);
            ct++;
        } catch (Error ignored) {

        }
        if (isLocation) {
            Toast.makeText(DashboardActivity.this, "Location sent to " + ct + " contacts.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DashboardActivity.this, "SMS sent to " + ct + " contacts.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation(user);

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation(user);
            }
        }
    }


    private void getCurrentLocation(user user) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(DashboardActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(DashboardActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        locationUrl = "Emergency! " + user.getName() + " sent a location https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude;
                                        Toast.makeText(DashboardActivity.this, "Location sent", Toast.LENGTH_SHORT).show();
                                        sendSMS(user, locationUrl, true);
                                    } else {
                                        Toast.makeText(DashboardActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(DashboardActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
}
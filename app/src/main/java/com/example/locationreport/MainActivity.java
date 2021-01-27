package com.example.locationreport;

import android.content.Context;
import android.content.SharedPreferences;

import android.location.Geocoder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {

    GPSTracker1 gps;
    Context context;
    double longitude;
    double latitude;
    private static final String TAG = "MainActivity";


    String Lat = "", Long = "";


    Geocoder geocoder;
    List<android.location.Address> addresses;


    String Address = "";


    String name;
    String title;

    TextView textview;
    FirebaseFirestore db;

    Timestamp time;
    String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        //TemporaryProviderInstallerProvider provider = new TemporaryProviderInstallerProvider();

        textview = (TextView)findViewById(R.id.textView);
        SharedPreferences sharedPref = this.getSharedPreferences(
                "com.example.locationreport", Context.MODE_PRIVATE);
        name = sharedPref.getString("name", "");
        title = sharedPref.getString("title", "");
        db = FirebaseFirestore.getInstance();

    }

    public void Location() {
        gps = new GPSTracker1(MainActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Lat = String.valueOf(latitude);
            Long = String.valueOf(longitude);
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            if (addresses == null) {
                //Toast.makeText(getApplicationContext(), "Couldn't get your location. Please check App Permissions and sim card in phone", Toast.LENGTH_LONG).show();
                textview.setTextColor(getResources().getColor(R.color.design_default_color_error));
                textview.setText("Couldn't get your location. Please check App Permissions and sim card in phone");
            } else {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                Address = address;
                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is " + address + city + state + country + postalCode, Toast.LENGTH_LONG).show();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss' '");
                Date date = new Date();
                time= Timestamp.now();
                datetime =  dateformat.format(time.toDate());;
                textview.setText("Connecting Database"+ name + title +datetime+ Address );
                connectDatabase();
            }
        } else {
            // can't get location
            // GPS or Net
            // work is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    public void initializeSSLContext(Context context) {
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch ( NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(context);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }




    public void sendLocation(View view){

            initializeSSLContext(MainActivity.this);
            Location();




    }

    public void connectDatabase(){
        // Access a Cloud Firestore instance from your Activity
        //FirebaseApp.initializeApp(this);

        // Create a new user with a first and last name

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("title", title);
        user.put("time", time);
        user.put("address", Address);


        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        textview.setTextColor(getResources().getColor(R.color.teal_200));
                        textview.setText("Sent successfully.\nName: "+ name +", "+ title + "\nTime: " + datetime + "\nLocation: " + Address);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textview.setTextColor(getResources().getColor(R.color.design_default_color_error));
                        textview.setText("Couldn't send your location. Please check if you have internet access.");
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }



}
package com.example.locationreport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.net.ssl.SSLContext;


public class Login extends Activity {
    EditText username,title;
    Button login;
    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = this.getSharedPreferences(
                "com.example.locationreport", Context.MODE_PRIVATE);
        String name = sharedPref.getString("name", "");
        String title_string = sharedPref.getString("title", "");

        try {
            SSLContext.getInstance("TLSv1.2");
        } catch ( NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        if (!hasPermissions1(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

        if (name != "")
        {
            if (name.equals("admin") && title_string.equals("admin123")){
                Intent locationview = new Intent(getApplicationContext(), LocationView.class);
                startActivity(locationview);
            }
            else {
                Intent locationreport = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(locationreport);
            }
        }

        else {
        setContentView(R.layout.login);


            username = findViewById(R.id.username);
            title = findViewById(R.id.title);
            login = findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //put your value
                    editor.putString("name", username.getText().toString());
                    editor.putString("title", title.getText().toString());
                    //commits your edits
                    editor.commit();
                    if (Objects.equals(username.getText().toString(), "admin") && Objects.equals(title.getText().toString(), "admin123")) {
                        // Create object of SharedPreferences.
                        Intent locationview = new Intent(getApplicationContext(), LocationView.class);
                        startActivity(locationview);
                    } else {
                        //now get Editor

                        Intent locationreport = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(locationreport);
                    }

                    // Create object of SharedPreferences.



                }
            });
        }

    }

    public static boolean hasPermissions1(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

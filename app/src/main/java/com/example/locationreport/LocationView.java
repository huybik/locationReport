package com.example.locationreport;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class LocationView extends Activity {
    TextView textView;
    private String TAG;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);
        textView = (TextView)findViewById(R.id.location_list);
        textView.setMovementMethod(new ScrollingMovementMethod());
        ShowList();
    }

    public void ShowList(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .limitToLast(100)
                .orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> user = document.getData();
                                String name = user.get("name").toString();
                                String title = user.get("title").toString();
                                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss' '");
                                Timestamp time = (Timestamp) user.get("time");
                                String datetime = dateformat.format(time.toDate());

                                //String datetime = dateformat.format(new Date(time));;
                                String address = user.get("address").toString();

                                result = result + name + ", " + title + "\n" + datetime + "\n" +
                                       address +  "\n\n";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            textView.append(result);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}

package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView RecyclerViewrv;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ArrayList<notification_class> photoArray;
    private AdapterNoti adapterHistory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));


        RecyclerViewrv = findViewById(R.id.RecyclerViewNoti);





        loadinfo();


    }


    private void loadinfo() {
        photoArray = new ArrayList<>();

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("notification");
        //reff.orderByChild("accountType").equalTo("Provider")
        reff.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        photoArray.clear();
                        if(!snapshot.exists())
                        {
                            return;
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            //String T=dataSnapshot.child("accountType").getValue().toString();
                            notification_class userPhoto = dataSnapshot.getValue(notification_class.class);
                            //if(!T.equalsIgnoreCase("GeneralUser"));
                            // {

                            //photoArray.add(userPhoto);
                            if (userPhoto.getCount() == 0) {
                                photoArray.add(userPhoto);
                            }
                            //}

                        }

                        adapterHistory = new AdapterNoti(getApplicationContext(), photoArray);
                        RecyclerViewrv.setAdapter(adapterHistory);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.SignOutMenuId)
        {

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.ProfileMenuId)
        {
            Intent i=new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.HistoryMenuId)
        {
            Intent i=new Intent(getApplicationContext(),HistoryActivity.class);
            startActivity(i);

        }
        else if (item.getItemId()==R.id.HomeMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.SettingsMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.NotificationMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),NotificationActivity.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.CCGraphMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),GraphActivity1.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.AboutUsMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(NotificationActivity.this, UploadActivity.class);
        startActivity(intent);
        finish();
    }
}






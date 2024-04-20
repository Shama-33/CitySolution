package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailHistoryActivity extends AppCompatActivity {
    String photoid;
    TextView historyupdate,historyuptime,historystatus,htextaddress,htextlocality,htextcode,htextstate,htextdistrict,htextcountry,txtviewdescription,txtInternetStatusDetailHistory;
    ImageView histimgML,imgPending,imgSolved,imgAck;
    private NetworkChangeReceiver networkChangeReceiver;
    private BroadcastReceiver networkChangeBroadcastReceiver;
    Button btnmap;
  TextView historyAckDate,historySolveDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        photoid= getIntent().getStringExtra("PHOTOID");
        //Toast.makeText(this, photoid, Toast.LENGTH_SHORT).show();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        historyAckDate=findViewById(R.id.historyAckDate);
        historySolveDate=findViewById(R.id.historySolveDate);
        imgPending=findViewById(R.id.imgPending);
        imgAck=findViewById(R.id.imgAck);
        imgSolved=findViewById(R.id.imgSolved);
        historyupdate=findViewById(R.id.historyupdate);


        htextaddress=findViewById(R.id.htextaddress);
        htextlocality=findViewById(R.id.htextlocality);
        htextcode=findViewById(R.id.htextcode);
        htextstate=findViewById(R.id.htextstate);
        htextdistrict=findViewById(R.id.htextdistrict);
        htextcountry=findViewById(R.id.htextcountry);
        txtviewdescription=findViewById(R.id.txtviewdescription);
        txtInternetStatusDetailHistory=findViewById(R.id.txtInternetStatusDetailHistory);
        btnmap=findViewById(R.id.btnmap);
       // imgAck.setEnabled(false);
       // imgSolved.setEnabled(false);
        imgAck.setAlpha(0.3f);
       imgSolved.setAlpha(0.3f);

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = htextaddress.getText().toString();
                String locality = htextlocality.getText().toString();
                String code = htextcode.getText().toString();
                String state = htextstate.getText().toString();
                String district = htextdistrict.getText().toString();
                String country = htextcountry.getText().toString();


                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address + ", " + locality + ", " + code + ", " + state + ", " + district + ", " + country);


                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");


                //if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
                //}





            }
        });





        histimgML=findViewById(R.id.histimgML);




        if (!isNetworkConnected()) {
            showNoInternetUI();
        } else {
            hideNoInternetUI();
        }

        // Register the network change receiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);


        networkChangeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = intent.getBooleanExtra("isConnected", false);
                if (isConnected) {
                    // Network connected

                    showNoInternetUI();
                } else {
                    // Network disconnected
                    hideNoInternetUI();

                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(networkChangeBroadcastReceiver, new IntentFilter(NetworkChangeReceiver.NETWORK_CHANGE_ACTION));




        String UID= FirebaseAuth.getInstance().getUid();
       if(photoid != null && UID != null)
       {
           DatabaseReference dref= FirebaseDatabase.getInstance().getReference("UserPhotos").child(UID);
           dref.orderByChild("photoid").equalTo(photoid)
           //dref.child(photoid)
                   .addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists())
                   {
                       for(DataSnapshot ds : snapshot.getChildren())
                       {
                           String area,ct,div,dist,post,con,desc,status,date,time;
                           UserPhoto userPhoto=ds.getValue(UserPhoto.class);
                           if (userPhoto == null)
                           {
                               return;
                           }
                           area=userPhoto.getLocality();
                           ct=userPhoto.getCity();
                           div=userPhoto.getDivision();
                           date=userPhoto.getDate();
                           dist=userPhoto.getDistrict();
                           post=userPhoto.getPincode();
                           con=userPhoto.getCountry();
                           time=userPhoto.getTime();
                           desc=userPhoto.getDescription();
                           status=userPhoto.getStatus();

                           String AckDAte,SolDate;
                           AckDAte=userPhoto.getAcknowlegedDate();
                           SolDate=userPhoto.getSolvedDate();

                           if(AckDAte== null || AckDAte.isEmpty())
                           {

                               historyAckDate.setText("");
                           }
                           else
                           {

                               historyAckDate.setText(AckDAte);
                              //imgAck.setEnabled(true);
                               imgAck.setAlpha(1f);



                           }
                           if(SolDate== null || SolDate.isEmpty())
                           {
                               historySolveDate.setText("");
                           }
                           else
                           {
                               historySolveDate.setText(SolDate);
                               //imgAck.setEnabled(true);
                               //imgSolved.setEnabled(true);
                               imgAck.setAlpha(1f);
                               imgSolved.setAlpha(1f);
                           }



                           historyupdate.setText(date);


                           htextaddress.setText(area);
                           htextlocality.setText(ct);
                           htextcode.setText(post);
                           htextstate.setText(div);
                           htextdistrict.setText(dist);
                           htextcountry.setText(con);
                           txtviewdescription.setText(desc);

                           String  imagepath=userPhoto.getImagepath();
                           if (imagepath != null) {
                               try {
                                   Picasso.with(getApplicationContext()).load(imagepath).into(histimgML);
                               }catch  (Exception e){

                                   e.printStackTrace();
                                   Toast.makeText(DetailHistoryActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();

                               }

                           }






                       }
                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

       }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.content_menu,menu);
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


    private void showNoInternetUI() {
        txtInternetStatusDetailHistory.setVisibility(View.VISIBLE);
        //btnProfileUpdate.setEnabled(false); // Disable the upload button
    }

    private void hideNoInternetUI() {
        txtInternetStatusDetailHistory.setVisibility(View.GONE);
        // btnProfileUpdate.setEnabled(true); // Enable the upload button
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check the internet connection status again when the activity is started
        if (!isNetworkConnected()) {
            showNoInternetUI();
        } else {
            hideNoInternetUI();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if (!isNetworkConnected()) {
            showNoInternetUI();
        } else {
            hideNoInternetUI();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        }
       // unregisterReceiver(networkChangeReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkChangeBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(DetailHistoryActivity.this, HistoryActivity.class);
        startActivity(intent);
        finish();
    }
}
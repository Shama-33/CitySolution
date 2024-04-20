package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProblemDetailsActivity2 extends AppCompatActivity {
    String photoid,category,fct;
    TextView historyupdate,historyuptime,historystatus,htextaddress,htextlocality,htextcode,htextstate,htextdistrict,htextcountry,txtviewdescription,catSnipper,issue,statusSpinner;
    ImageView histimgML;
    String cityName,email;
    //Spinner statusSpinner,catSnipper;
    ArrayAdapter<CharSequence> adapter,adapter2;
    String status1,category1;
    Button btn,btnMap;

   // @SuppressLint({"MissingInflatedId", "WrongViewCast"})

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        setContentView(R.layout.activity_problem_details2);

        photoid = getIntent().getStringExtra("PHOTOID");
        category = getIntent().getStringExtra("CATEGORY");
        fct = getIntent().getStringExtra("CITY");
        //Toast.makeText(ProblemDetailsActivity2.this, "yes:" + fct, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, photoid, Toast.LENGTH_SHORT).show();
        btn = findViewById(R.id.btnn);
        btnMap= findViewById(R.id.btnmap);
        historyupdate = findViewById(R.id.historyupdate);
        historyuptime = findViewById(R.id.historyuptime);
        historystatus = findViewById(R.id.statusSpinner);
        htextaddress = findViewById(R.id.htextaddress);
        htextlocality = findViewById(R.id.htextlocality);
        htextcode = findViewById(R.id.htextcode);
        htextstate = findViewById(R.id.htextstate);
        htextdistrict = findViewById(R.id.htextdistrict);
        htextcountry = findViewById(R.id.htextcountry);
        txtviewdescription = findViewById(R.id.txtviewdescription);
        catSnipper = findViewById(R.id.catSpinner);
        issue = findViewById(R.id.issue);
        histimgML = findViewById(R.id.histimgML);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CategoryActivity2.class);
                startActivity(intent);
                finish();

            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = htextaddress.getText().toString();
                String locality = htextlocality.getText().toString();
                String code = htextcode.getText().toString();
                String state = htextstate.getText().toString();
                String district = htextdistrict.getText().toString();
                String country = htextcountry.getText().toString();

                // Create a URI for the location
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address + ", " + locality + ", " + code + ", " + state + ", " + district + ", " + country);

                // Create an intent to open Google Maps with the location
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                // Check if there's an app to handle the intent before starting it
                //if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
                //}





            }
        });

        //statusSpinner = findViewById(R.id.status);
        //Toast.makeText(ProblemDetailsActivity2.this, "yes:" + fct, Toast.LENGTH_SHORT).show();

           DatabaseReference dref= FirebaseDatabase.getInstance().getReference("PhotoLocation").child(fct).child(category);
            dref.orderByChild("photoid").equalTo(photoid)
                    //dref.child(photoid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                Toast.makeText(ProblemDetailsActivity2.this, "yes:"+fct, Toast.LENGTH_SHORT).show();
                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    String area,ct,div,dist,post,con,desc,date,time,c,i,status,cattt;
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
                                    c=userPhoto.getCategory();
                                    i=userPhoto.getUserid();
                                    cattt=userPhoto.getCategory();

                                    // Assuming you have a Firebase Database reference
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo"); // Replace with your database path

// Replace "userID" with the actual userID you have


                                    databaseReference.child(i).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // Retrieve the email associated with the userID
                                                String userEmailAddress = dataSnapshot.child("email").getValue(String.class);

// Create a clickable SpannableString
                                                SpannableString spannableString = new SpannableString(userEmailAddress);

// Set a ClickableSpan to handle the click event
                                                ClickableSpan clickableSpan = new ClickableSpan() {
                                                    @Override
                                                    public void onClick(View widget) {
                                                        // Create an Intent with the action ACTION_SENDTO
                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

                                                        // Set the data (email address) for the intent
                                                        emailIntent.setData(Uri.parse("mailto:" + userEmailAddress));

                                                        // Set the package for Gmail app
                                                        emailIntent.setPackage("com.google.android.gm");

                                                        // Set the subject for the email (optional)
                                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Your CitySolution App Issue");
                                                        startActivity(emailIntent);

                                                        // Check if the Gmail app is installed and can handle this intent
                                                    /*if (emailIntent.resolveActivity(getPackageManager()) != null) {
                                                        // Start the email activity with the Gmail app

                                                    } else {
                                                        List<ResolveInfo> packages = getPackageManager().queryIntentActivities(emailIntent, 0);
                                                        for (ResolveInfo resolveInfo : packages) {

                                                            Log.d("PackageInfo", resolveInfo.activityInfo.packageName);
                                                        }
                                                        // Handle the case where Gmail app is not available
                                                        Toast.makeText(ProblemDetailsActivity.this, "Gmail app not found", Toast.LENGTH_SHORT).show();
                                                    }*/
                                                    }
                                                };

// Set the ClickableSpan to the SpannableString
                                                spannableString.setSpan(clickableSpan, 0, userEmailAddress.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Make the text view clickable and set the SpannableString
                                                issue.setMovementMethod(LinkMovementMethod.getInstance());
                                                issue.setText(spannableString);// Assuming you have obtained the user's email address from the database



                                            } else {
                                                // Handle the case where the userID doesn't exist
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle any errors or exceptions here
                                        }
                                    });

                                    historyupdate.setText(date);
                                    historyuptime.setText(time);
                                    historystatus.setText(status);
                                    // int initialPosition = adapter.getPosition(status);
                                    //statusSpinner.setSelection(initialPosition);
                                    //int initialPosition2 = adapter2.getPosition(c);
                                    //catSnipper.setSelection(initialPosition2);
                                    htextaddress.setText(area);
                                    htextlocality.setText(ct);
                                    htextcode.setText(post);
                                    htextstate.setText(div);
                                    htextdistrict.setText(dist);
                                    htextcountry.setText(con);
                                    txtviewdescription.setText(desc);
                                    catSnipper.setText(cattt);

                                    //cat.setText(c);

                                    String  imagepath=userPhoto.getImagepath();
                                    if (imagepath != null) {
                                        try {
                                            Picasso.with(getApplicationContext()).load(imagepath).into(histimgML);
                                        }catch  (Exception e){

                                            e.printStackTrace(); // Log the exception for debugging
                                            Toast.makeText(ProblemDetailsActivity2.this, "Error Loading Image", Toast.LENGTH_SHORT).show();

                                        }

                                    }





                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





        //String UID= FirebaseAuth.getInstance().getUid();


                    // Handle the data you retrieved here.

    }



}

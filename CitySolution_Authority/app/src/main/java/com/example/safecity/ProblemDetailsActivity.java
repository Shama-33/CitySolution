package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ResolveInfo;
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
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProblemDetailsActivity extends AppCompatActivity {
    String photoid,category;
    TextView historyupdate,historyuptime,historystatus,htextaddress,htextlocality,htextcode,htextstate,htextdistrict,htextcountry,txtviewdescription,cat,issue;
    ImageView histimgML;
     String cityName,email;
    Spinner statusSpinner,catSnipper;
    ArrayAdapter<CharSequence> adapter,adapter2;
    String status1,category1;
    Button btn,btnMap;
    String userEmailAddress;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        setContentView(R.layout.activity_problem_details);

        photoid= getIntent().getStringExtra("PHOTOID");
        category = getIntent().getStringExtra("CATEGORY");
        //Toast.makeText(this, photoid, Toast.LENGTH_SHORT).show();
        btn=findViewById(R.id.btnn);
        btnMap=findViewById(R.id.btnmap);
        historyupdate=findViewById(R.id.historyupdate);
        historyuptime=findViewById(R.id.historyuptime);
        //historystatus=findViewById(R.id.historystatus);
        htextaddress=findViewById(R.id.htextaddress);
        htextlocality=findViewById(R.id.htextlocality);
        htextcode=findViewById(R.id.htextcode);
        htextstate=findViewById(R.id.htextstate);
        htextdistrict=findViewById(R.id.htextdistrict);
        htextcountry=findViewById(R.id.htextcountry);
        txtviewdescription=findViewById(R.id.txtviewdescription);
       // cat=findViewById(R.id.cat);
        issue=findViewById(R.id.issue);
        histimgML=findViewById(R.id.histimgML);

        statusSpinner = findViewById(R.id.statusSpinner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
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

// Create an ArrayAdapter using the string array with your 3 status options
        adapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the Spinner
        statusSpinner.setAdapter(adapter);

// Set a listener to handle item selection
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected option here
                String selectedStatus = parentView.getItemAtPosition(position).toString();
                fun2(selectedStatus);


                // Do something with the selected status
                // For example, you can display it or store it in a variable.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle nothing selected (if needed)
            }
        });

        catSnipper= findViewById(R.id.catSpinner);

// Create an ArrayAdapter using the string array with your 3 status options
        adapter2 = ArrayAdapter.createFromResource(this, R.array.category_options, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the Spinner
        catSnipper.setAdapter(adapter2);

// Set a listener to handle item selection
        catSnipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected option here
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                fun3(selectedCategory);



                // Do something with the selected status
                // For example, you can display it or store it in a variable.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle nothing selected (if needed)
            }
        });






        //String UID= FirebaseAuth.getInstance().getUid();
        String currentUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(currentUserUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cityName = dataSnapshot.child("city").getValue(String.class);
                    fun(cityName);
                    // Handle the data you retrieved here.
                } else {
                    // Handle the case where data doesn't exist.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors or exceptions here.
            }
        });










    }
    private void fun(String cityName)
    {

        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("PhotoLocation").child(cityName).child(category);
        dref.orderByChild("photoid").equalTo(photoid)
                //dref.child(photoid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot ds : snapshot.getChildren())
                            {
                                String area,ct,div,dist,post,con,desc,date,time,c,i,status;
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

                                // Assuming you have a Firebase Database reference
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo"); // Replace with your database path

// Replace "userID" with the actual userID you have


                                databaseReference.child(i).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Retrieve the email associated with the userID
                                            // Assuming you have obtained the user's email address from the database
                                            userEmailAddress = dataSnapshot.child("email").getValue(String.class);

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

                                                    // Set the body of the email (optional)
                                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Please provide details about your issue:");
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
                                //historystatus.setText(status);
                                int initialPosition = adapter.getPosition(status);
                                statusSpinner.setSelection(initialPosition);
                                int initialPosition2 = adapter2.getPosition(c);
                                catSnipper.setSelection(initialPosition2);
                                htextaddress.setText(area);
                                htextlocality.setText(ct);
                                htextcode.setText(post);
                                htextstate.setText(div);
                                htextdistrict.setText(dist);
                                htextcountry.setText(con);
                                txtviewdescription.setText(desc);

                                //cat.setText(c);

                                String  imagepath=userPhoto.getImagepath();
                                if (imagepath != null) {
                                    try {
                                        Picasso.with(getApplicationContext()).load(imagepath).into(histimgML);
                                    }catch  (Exception e){

                                        e.printStackTrace(); // Log the exception for debugging
                                        Toast.makeText(ProblemDetailsActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();

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
    private void fun2(String selectedStatus){
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("PhotoLocation").child(cityName).child(category);
        dref.orderByChild("photoid").equalTo(photoid)
                //dref.child(photoid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String area, ct, div, dist, post, con, desc, status, date, time, c, i;
                                UserPhoto userPhoto = ds.getValue(UserPhoto.class);
                                i=userPhoto.getUserid();
                                status1=userPhoto.getStatus();
                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference()
                                        .child("UserPhotos")
                                        .child(i)  // User's UID
                                        .child(photoid); // Photo's ID


// Step 3: Update the status
                                HashMap<String, Object> statusUpdate = new HashMap<>();
                                statusUpdate.put("status", selectedStatus);
                                DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("notification").child(i).child(photoid);
                                Map<String, Object> notificationData = new HashMap<>();
                                if ("Acknowledged".equals(selectedStatus)) {
                                    statusUpdate.put("acknowledgedDate", getCurrentDate());
                                    notificationData.put("feedback", "Your problem is under processing");
                                    notificationData.put("count", 0); // Initial count
                                    notificationData.put("pid", photoid);
                                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss", Locale.getDefault());
                                    String notiDate = dateFormat1.format(new Date());
                                    notificationData.put("date_time", notiDate);

                                    notificationReference.setValue(notificationData);

                                     // Replace with the actual feedback

                                } else if ("Solved".equals(selectedStatus)) {
                                    statusUpdate.put("solvedDate", getCurrentDate());
                                    notificationData.put("feedback", "Your problem has been solved");
                                    notificationData.put("count", 0); // Initial count
                                    notificationData.put("pid", photoid);
                                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss", Locale.getDefault());
                                    String notiDate = dateFormat1.format(new Date());
                                    notificationData.put("date_time", notiDate);

                                    notificationReference.setValue(notificationData);
                                }

                                // Update the "notification" node

                                //historyupdate.setText(selectedStatus);
                                databaseReference2.updateChildren(statusUpdate)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if (!status1.equals(selectedStatus)) {
                                                    // Status update successful
                                                    showToast("Update successful");
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors
                                                showToast("Update failed");
                                            }
                                        });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        String currentUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(currentUserUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cityName = dataSnapshot.child("city").getValue(String.class);
                    DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference()
                            .child("PhotoLocation")
                            .child(cityName)  // User's UID
                            .child(category)
                            .child(photoid); // Photo's ID

// Step 3: Update the status
                    HashMap<String, Object> statusUpdate = new HashMap<>();
                    statusUpdate.put("status", selectedStatus);
                    if ("Acknowledged".equals(selectedStatus)) {
                        statusUpdate.put("acknowledgedDate", getCurrentDate());
                    } else if ("Solved".equals(selectedStatus)) {
                        statusUpdate.put("solvedDate", getCurrentDate());
                    }
                    //historyupdate.setText(selectedStatus);
                    databaseReference3.updateChildren(statusUpdate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   // if (!status.equals(selectedStatus)) {
                                        // Status update successful
                                      //  showToast("Update successful");
                                   // }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors
                                    showToast("Update failed");
                                }
                            });

                    // Handle the data you retrieved here.
                } else {
                    // Handle the case where data doesn't exist.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors or exceptions here.
            }
        });


    }
    private void fun3(String selectedStatus) {

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("PhotoLocation").child(cityName).child(category);
        dref.orderByChild("photoid").equalTo(photoid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserPhoto userPhoto = ds.getValue(UserPhoto.class);
                                String i = userPhoto.getUserid();
                                category1 = userPhoto.getCategory();
                                String newCategory = selectedStatus;
                                //HashMap<String, Object> statusUpdate = new HashMap<>();
                                //statusUpdate.put("status", selectedStatus);
                                DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("notification").child(i).child(photoid);
                                Map<String, Object> notificationData = new HashMap<>();
                                if ("Fake_complain".equals(newCategory)) {

                                    notificationData.put("feedback", "Alert!!We found your problem to be a false claim");
                                    notificationData.put("count", 0); // Initial count
                                    notificationData.put("pid", photoid);
                                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss", Locale.getDefault());
                                    String notiDate = dateFormat1.format(new Date());
                                    notificationData.put("date_time", notiDate);

                                    notificationReference.setValue(notificationData);

                                    // Replace with the actual feedback

                                }
                                // historyupdate.setText(category1);
                               // historyuptime.setText(selectedStatus);


                                // Step 1: Update the "category" field in UserPhotos
                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference()
                                        .child("UserPhotos")
                                        .child(i)
                                        .child(photoid);

                                HashMap<String, Object> statusUpdate = new HashMap<>();
                                statusUpdate.put("category", selectedStatus);
                                databaseReference2.updateChildren(statusUpdate)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if (!category1.equals(selectedStatus)) {
                                                    // Status update successful
                                                    showToast("Update successful");
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors
                                                showToast("Update failed");
                                            }
                                        });
                                if(!category1.equals(selectedStatus)) {
                                    final String timestamp = userPhoto.getTimestamp();
                                    UserPhoto userPhoto2 = new UserPhoto();
                                    userPhoto2.setUserid(userPhoto.getUserid());
                                    userPhoto.setCategory(selectedStatus);
                                    userPhoto.setConfidence(userPhoto.getConfidence());
                                    userPhoto.setDate(userPhoto.getDate());
                                    userPhoto.setPhotoid(photoid);
                                    userPhoto.setTimestamp(timestamp);
                                    userPhoto.setDescription(userPhoto.getDescription());
                                    userPhoto.setImagepath(userPhoto.getImagepath());
                                    userPhoto.setTime(userPhoto.getTime());
                                    userPhoto.setCity(userPhoto.getCity());
                                    userPhoto.setCity_corporation(userPhoto.getCity());
                                    userPhoto.setDistrict(userPhoto.getDistrict());
                                    userPhoto.setDivision(userPhoto.getDivision());
                                    userPhoto.setLocality(userPhoto.getLocality());
                                    userPhoto.setPincode(userPhoto.getPincode());
                                    userPhoto.setCountry(userPhoto2.getCountry());
                                    userPhoto.setStatus(userPhoto.getStatus());


                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PhotoLocation");
                                    databaseReference.child(userPhoto.getCity()).child(selectedStatus).child(photoid).setValue(userPhoto).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProblemDetailsActivity.this, "Second Information Added", Toast.LENGTH_SHORT).show();


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProblemDetailsActivity.this, "Could Not Update Data", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(ProblemDetailsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            //uploadProgressBar.setVisibility(View.GONE);
                                            //
                                            // flag=1;
                                            return;
                                        }
                                    });


                                    DatabaseReference oldCategoryReference = FirebaseDatabase.getInstance().getReference()
                                            .child("PhotoLocation")
                                            .child(cityName)
                                            .child(category1)
                                            .child(photoid);

                                    oldCategoryReference.removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ProblemDetailsActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                                                    // Deletion successful
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle any errors
                                                }
                                            });
                                }



                                // Step 2: Move the data to the new category

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors
                    }
                });
    }



    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private String getCurrentDate() {
        // Get the current date in a specific format, e.g., "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
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
    String photoid, category;
    TextView historyupdate, historyuptime, historystatus, htextaddress, htextlocality, htextcode, htextstate, htextdistrict, htextcountry, txtviewdescription, cat, issue;
    ImageView histimgML;
    String cityName, email;
    Spinner statusSpinner, catSnipper;
    ArrayAdapter<CharSequence> adapter, adapter2;
    String status1, category1;
    Button btn, btnMap;
    String userEmailAddress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        setContentView(R.layout.activity_problem_details);

        photoid = getIntent().getStringExtra("PHOTOID");
        category = getIntent().getStringExtra("CATEGORY");
        btn = findViewById(R.id.btnn);
        btnMap = findViewById(R.id.btnmap);
        historyupdate = findViewById(R.id.historyupdate);
        historyuptime = findViewById(R.id.historyuptime);
        htextaddress = findViewById(R.id.htextaddress);
        htextlocality = findViewById(R.id.htextlocality);
        htextcode = findViewById(R.id.htextcode);
        htextstate = findViewById(R.id.htextstate);
        htextdistrict = findViewById(R.id.htextdistrict);
        htextcountry = findViewById(R.id.htextcountry);
        txtviewdescription = findViewById(R.id.txtviewdescription);
        issue = findViewById(R.id.issue);
        histimgML = findViewById(R.id.histimgML);
        statusSpinner = findViewById(R.id.statusSpinner);
        catSnipper = findViewById(R.id.catSpinner);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
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

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address + ", " + locality + ", " + code + ", " + state + ", " + district + ", " + country);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);
            }
        });

        adapter = ArrayAdapter.createFromResource(this, R.array.status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        catSnipper = findViewById(R.id.catSpinner);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.category_options, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSnipper.setAdapter(adapter2);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = parentView.getItemAtPosition(position).toString();
                if (cityName != null) {
                    fun2(selectedStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        catSnipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                if (cityName != null) {
                    fun3(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        String currentUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(currentUserUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cityName = dataSnapshot.child("city").getValue(String.class);
                    if (cityName != null) {
                        fun(cityName);
                    } else {
                        Toast.makeText(ProblemDetailsActivity.this, "City name is null", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProblemDetailsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fun(String cityName) {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("PhotoLocation").child(cityName).child(category);
        dref.orderByChild("photoid").equalTo(photoid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String area, ct, div, dist, post, con, desc, date, time, c, i, status;
                        UserPhoto userPhoto = ds.getValue(UserPhoto.class);
                        if (userPhoto == null) {
                            return;
                        }
                        area = userPhoto.getLocality();
                        ct = userPhoto.getCity();
                        div = userPhoto.getDivision();
                        date = userPhoto.getDate();
                        dist = userPhoto.getDistrict();
                        post = userPhoto.getPincode();
                        con = userPhoto.getCountry();
                        time = userPhoto.getTime();
                        desc = userPhoto.getDescription();
                        status = userPhoto.getStatus();
                        c = userPhoto.getCategory();
                        i = userPhoto.getUserid();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
                        databaseReference.child(i).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    userEmailAddress = dataSnapshot.child("email").getValue(String.class);
                                    SpannableString spannableString = new SpannableString(userEmailAddress);
                                    ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                            emailIntent.setData(Uri.parse("mailto:" + userEmailAddress));
                                            emailIntent.setPackage("com.google.android.gm");
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Your CitySolution App Issue");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please provide details about your issue:");
                                            startActivity(emailIntent);
                                        }
                                    };
                                    spannableString.setSpan(clickableSpan, 0, userEmailAddress.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    issue.setMovementMethod(LinkMovementMethod.getInstance());
                                    issue.setText(spannableString);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(ProblemDetailsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        historyupdate.setText(date);
                        historyuptime.setText(time);
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

                        String imagepath = userPhoto.getImagepath();
                        if (imagepath != null) {
                            try {
                                Picasso.with(getApplicationContext()).load(imagepath).into(histimgML);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ProblemDetailsActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProblemDetailsActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fun2(String selectedStatus) {
        if (cityName == null) return;
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("PhotoLocation").child(cityName).child(category);
        dref.orderByChild("photoid").equalTo(photoid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String i;
                        UserPhoto userPhoto = ds.getValue(UserPhoto.class);
                        if (userPhoto == null) {
                            return;
                        }
                        i = userPhoto.getUserid();
                        status1 = userPhoto.getStatus();
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference()
                                .child("UserPhotos")
                                .child(i)
                                .child(photoid);

                        HashMap<String, Object> statusUpdate = new HashMap<>();
                        statusUpdate.put("status", selectedStatus);
                        DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("notification").child(i).child(photoid);
                        Map<String, Object> notificationData = new HashMap<>();
                        if ("Acknowledged".equals(selectedStatus)) {
                            statusUpdate.put("acknowledgedDate", getCurrentDate());
                            notificationData.put("feedback", "Your problem is under processing");
                            notificationData.put("count", 0);
                            notificationData.put("pid", photoid);
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss", Locale.getDefault());
                            String notiDate = dateFormat1.format(new Date());
                            notificationData.put("date_time", notiDate);
                            notificationReference.setValue(notificationData);
                        } else if ("Solved".equals(selectedStatus)) {
                            statusUpdate.put("solvedDate", getCurrentDate());
                            notificationData.put("feedback", "Your problem has been solved");
                            notificationData.put("count", 0);
                            notificationData.put("pid", photoid);
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss", Locale.getDefault());
                            String notiDate = dateFormat1.format(new Date());
                            notificationData.put("date_time", notiDate);
                            notificationReference.setValue(notificationData);
                        }

                        databaseReference2.updateChildren(statusUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (!status1.equals(selectedStatus)) {
                                            showToast("Update successful");
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast("Update failed");
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProblemDetailsActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            .child(cityName)
                            .child(category)
                            .child(photoid);

                    HashMap<String, Object> statusUpdate = new HashMap<>();
                    statusUpdate.put("status", selectedStatus);
                    if ("Acknowledged".equals(selectedStatus)) {
                        statusUpdate.put("acknowledgedDate", getCurrentDate());
                    } else if ("Solved".equals(selectedStatus)) {
                        statusUpdate.put("solvedDate", getCurrentDate());
                    }
                    databaseReference3.updateChildren(statusUpdate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("Update failed");
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProblemDetailsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fun3(String selectedCategory) {
        if (cityName == null) return;
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("PhotoLocation").child(cityName).child(category);
        dref.orderByChild("photoid").equalTo(photoid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserPhoto userPhoto = ds.getValue(UserPhoto.class);
                        if (userPhoto == null) {
                            return;
                        }
                        String i = userPhoto.getUserid();
                        category1 = userPhoto.getCategory();
                        String newCategory = selectedCategory;
                        DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("notification").child(i).child(photoid);
                        Map<String, Object> notificationData = new HashMap<>();
                        if ("Fake_complain".equals(newCategory)) {
                            notificationData.put("feedback", "Alert!! We found your problem to be a false claim");
                            notificationData.put("count", 0);
                            notificationData.put("pid", photoid);
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss", Locale.getDefault());
                            String notiDate = dateFormat1.format(new Date());
                            notificationData.put("date_time", notiDate);
                            notificationReference.setValue(notificationData);
                        }

                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference()
                                .child("UserPhotos")
                                .child(i)
                                .child(photoid);

                        HashMap<String, Object> statusUpdate = new HashMap<>();
                        statusUpdate.put("category", selectedCategory);
                        databaseReference2.updateChildren(statusUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (!category1.equals(selectedCategory)) {
                                            showToast("Update successful");
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showToast("Update failed");
                                    }
                                });

                        if (!category1.equals(selectedCategory)) {
                            final String timestamp = userPhoto.getTimestamp();
                            UserPhoto userPhoto2 = new UserPhoto();
                            userPhoto2.setUserid(userPhoto.getUserid());
                            userPhoto.setCategory(selectedCategory);
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
                            databaseReference.child(userPhoto.getCity()).child(selectedCategory).child(photoid).setValue(userPhoto).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ProblemDetailsActivity.this, "Second Information Added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProblemDetailsActivity.this, "Could Not Update Data", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ProblemDetailsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle any errors
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProblemDetailsActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.SignOutAdminMenuId) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.HomeAdminMenuId) {
            Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.ProfileAdminMenuId) {
            Intent i = new Intent(getApplicationContext(), Profile.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}

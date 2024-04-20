package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CategoryActivity extends AppCompatActivity {
    Button road,flood,trash,home,others,fake;
    private int d=0,f=0,t=0,h=0,o=0,fk=0;
    String dd,ff,tt,hh,oo;
    Button btnGraphCC;
    LinearLayout roadL,floodL,trashL,homeL,othersL,fakeL,btnGraphCCL;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        setContentView(R.layout.activity_category);
        this.setTitle("Categories of the problems");
        road=findViewById(R.id.btnRoad);
        flood=findViewById(R.id.btnFlood);
        trash=findViewById(R.id.btnTrash);
        home=findViewById(R.id.btnHome);
        others=findViewById(R.id.btnOthers);
        btnGraphCC=findViewById(R.id.btnGraphCC);
        fake=findViewById(R.id.btnFake);
        roadL=findViewById(R.id.panel1);
        floodL=findViewById(R.id.panel2);
        trashL=findViewById(R.id.panel3);
        homeL=findViewById(R.id.panel4);
        othersL=findViewById(R.id.panel5);
        btnGraphCCL=findViewById(R.id.centerPanel);
        fakeL=findViewById(R.id.panel6);






            String currentUserUid = FirebaseAuth.getInstance().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(currentUserUid);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                       String cityName = dataSnapshot.child("city").getValue(String.class);
                       count(cityName);

                        btnGraphCCL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, GraphActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("GRAPH_CITY", cityName);
                                //intent.putExtra("CATEGORY", "Damaged_Road");
                                startActivity(intent);

                            }
                        });
                        btnGraphCC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, GraphActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("GRAPH_CITY", cityName);
                                //intent.putExtra("CATEGORY", "Damaged_Road");
                                startActivity(intent);
                            }
                        });


                        roadL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Damaged_Road");
                                startActivity(intent);
                            }
                        });
                        road.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Damaged_Road");
                                startActivity(intent);
                            }
                        });

                        floodL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Flood");
                                startActivity(intent);
                            }
                        });
                        flood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Flood");
                                startActivity(intent);
                            }
                        });

                        trashL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Trash");
                                startActivity(intent);
                            }
                        });
                        trash.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Trash");
                                startActivity(intent);
                            }
                        });

                        homeL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Homeless_people");
                                startActivity(intent);
                            }
                        });
                        home.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Homeless_people");
                                startActivity(intent);
                            }
                        });

                        othersL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Others");
                                startActivity(intent);
                            }
                        });
                        others.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Others");
                                startActivity(intent);
                            }
                        });
                        fakeL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Fake_complain");
                                startActivity(intent);
                            }
                        });
                        fake.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CategoryActivity.this, ProblemActivity1.class);
                                // Pass data to Problemactivity1
                                intent.putExtra("EMPLOYEE_CITY", cityName);
                                intent.putExtra("CATEGORY", "Fake_complain");
                                startActivity(intent);
                            }
                        });
                        // Now you have the user's city name (cityName) for the currently logged-in user.

                        // You can use cityName here or in other parts of the method
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors or exceptions here.
                }
            });



            // ...
        }
    private void count(String city) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("PhotoLocation").child(city);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Damaged_Road").exists()) {
                    d = (int) snapshot.child("Damaged_Road").getChildrenCount();
                }
                if (snapshot.child("Flood").exists()) {
                    f = (int) snapshot.child("Flood").getChildrenCount();
                }
                if (snapshot.child("Trash").exists()) {
                    t = (int) snapshot.child("Trash").getChildrenCount();
                }
                if (snapshot.child("Homeless_people").exists()) {
                    h = (int) snapshot.child("Homeless_people").getChildrenCount();
                }
                if (snapshot.child("Others").exists()) {
                    o = (int) snapshot.child("Others").getChildrenCount();
                }
                if (snapshot.child("Fake_complain").exists()) {
                    fk = (int) snapshot.child("Fake_complain").getChildrenCount();
                }

                // Update your UI elements with the counts here
                road.setText("Damaged road: " + d);
                flood.setText("Flood: " + f);
                trash.setText("Trash: " + t);
                home.setText("Homeless People: " + h);
                others.setText("Others: " + o);
                fake.setText("Fake Complains: " + fk);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.adminmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.SignOutAdminMenuId)
        {

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        else if (item.getItemId()==R.id.HomeAdminMenuId)
        {
            Intent i=new Intent(getApplicationContext(),CategoryActivity.class);
            startActivity(i);

        }
        else if (item.getItemId()==R.id.ProfileAdminMenuId)
        {
            Intent i=new Intent(getApplicationContext(),Profile.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }





}
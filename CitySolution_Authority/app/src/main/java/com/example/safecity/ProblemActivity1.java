package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ProblemActivity1 extends AppCompatActivity {
    private RelativeLayout rltvlayoutCat;

    private EditText edttxtsearch_bar;
    private Button btnFilter;
    private TextView txtviewfilter;
    private RecyclerView RecyclerViewrv;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    private ArrayList<UserPhoto> photoArray;
    private AdapterProblem adapterProblem;
    private String category,city;

    //private AdapterHistory adapterHistory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        setContentView(R.layout.activity_problem1);

        this.setTitle("Problems");

        rltvlayoutCat=findViewById(R.id.rltvlayoutCat);
        edttxtsearch_bar=findViewById(R.id.edttxtsearch_bar);
        btnFilter=findViewById(R.id.btnFilter);
        txtviewfilter=findViewById(R.id.txtviewfilter);
        RecyclerViewrv=findViewById(R.id.RecyclerViewrv);
        category = getIntent().getStringExtra("CATEGORY");
        city = getIntent().getStringExtra("EMPLOYEE_CITY");


        loadinfo();

        edttxtsearch_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterProblem.getFilter().filter(s);


                }catch(Exception e)
                {
                    e.printStackTrace();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(ProblemActivity1.this);
                builder.setTitle("Choose Sorting Criteria")
                        .setItems(SortTypes.types1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selected= SortTypes.types1[which];
                                txtviewfilter.setText(selected);
                                //spec=C;
                                if(selected.equals("No Sorting"))
                                {
                                    loadinfo();

                                }
                                else
                                {
                                    loadFildinfo(selected);
                                }


                            }
                        }).show();
            }
        });
    }
    private void loadFildinfo(String selected) {
        photoArray=new ArrayList<>();

        DatabaseReference reff=FirebaseDatabase.getInstance().getReference("PhotoLocation");
        reff.child(city).child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photoArray.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserPhoto dox = ds.getValue(UserPhoto.class);
                    photoArray.add(dox);
                }

                if ("Date Ascending".equals(selected)) {
                    Collections.sort(photoArray, new Comparator<UserPhoto>() {
                        @Override
                        public int compare(UserPhoto photo1, UserPhoto photo2) {
                            if (photo1.getDate() != null && photo2.getDate() != null) {
                                // Parse the dates and compare
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date date1 = sdf.parse(photo1.getDate());
                                    Date date2 = sdf.parse(photo2.getDate());
                                    return date1.compareTo(date2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0; // Handle date parsing error
                                }
                            } else if (photo1.getDate() != null) {
                                return -1; // Non-null date comes before null date
                            } else if (photo2.getDate() != null) {
                                return 1; // Null date comes after non-null date
                            }
                            return 0; // Both dates are null
                        }
                    });
                } else if ("Date Descending".equals(selected)) {
                    Collections.sort(photoArray, new Comparator<UserPhoto>() {
                        @Override
                        public int compare(UserPhoto photo1, UserPhoto photo2) {
                            // Parse the dates and compare in descending order
                            if (photo1.getDate() != null && photo2.getDate() != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date date1 = sdf.parse(photo1.getDate());
                                    Date date2 = sdf.parse(photo2.getDate());
                                    return date2.compareTo(date1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0; // Handle date parsing error
                                }
                            } else if (photo1.getDate() != null) {
                                return -1; // Non-null date comes before null date
                            } else if (photo2.getDate() != null) {
                                return 1; // Null date comes after non-null date
                            }
                            return 0; // Both dates are null
                        }
                    });
                } else if ("Address Ascending".equals(selected)) {
                    Collections.sort(photoArray, new Comparator<UserPhoto>() {
                        @Override
                        public int compare(UserPhoto photo1, UserPhoto photo2) {
                            // Handle null city
                            if (photo1.getCity() == null && photo2.getCity() == null) {
                                return 0; // Both cities are null
                            } else if (photo1.getCity() == null) {
                                return -1; // Null city comes before non-null city
                            } else if (photo2.getCity() == null) {
                                return 1; // Non-null city comes after null city
                            }

                            int cityComparison = photo1.getCity().compareTo(photo2.getCity());
                            if (cityComparison != 0) {
                                return cityComparison;
                            }

                            // Handle null locality
                            if (photo1.getLocality() == null && photo2.getLocality() == null) {
                                return 0; // Both localities are null
                            } else if (photo1.getLocality() == null) {
                                return -1; // Null locality comes before non-null locality
                            } else if (photo2.getLocality() == null) {
                                return 1; // Non-null locality comes after null locality
                            }

                            return photo1.getLocality().compareTo(photo2.getLocality());
                        }
                    });
                } else if ("Address Descending".equals(selected)) {
                    Collections.sort(photoArray, new Comparator<UserPhoto>() {
                        @Override
                        public int compare(UserPhoto photo1, UserPhoto photo2) {
                            // Handle null city
                            if (photo1.getCity() == null && photo2.getCity() == null) {
                                return 0; // Both cities are null
                            } else if (photo1.getCity() == null) {
                                return 1; // Null city comes after non-null city
                            } else if (photo2.getCity() == null) {
                                return -1; // Non-null city comes before null city
                            }

                            int cityComparison = photo2.getCity().compareTo(photo1.getCity());
                            if (cityComparison != 0) {
                                return cityComparison;
                            }

                            // Handle null locality
                            if (photo1.getLocality() == null && photo2.getLocality() == null) {
                                return 0; // Both localities are null
                            } else if (photo1.getLocality() == null) {
                                return 1; // Null locality comes after non-null locality
                            } else if (photo2.getLocality() == null) {
                                return -1; // Non-null locality comes before null locality
                            }

                            return photo2.getLocality().compareTo(photo1.getLocality());
                        }
                    });
                }
                adapterProblem= new AdapterProblem(getApplicationContext(),photoArray);
                RecyclerViewrv.setAdapter(adapterProblem);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadinfo() {
        photoArray=new ArrayList<>();

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("PhotoLocation");
        reff.child(city).child(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        photoArray.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            //String T=dataSnapshot.child("accountType").getValue().toString();
                            UserPhoto userPhoto = dataSnapshot.getValue(UserPhoto.class);
                            //if(!T.equalsIgnoreCase("GeneralUser"));
                            // {

                            photoArray.add(userPhoto);
                            //}

                        }

                        adapterProblem= new AdapterProblem(getApplicationContext(),photoArray);
                        RecyclerViewrv.setAdapter(adapterProblem);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}
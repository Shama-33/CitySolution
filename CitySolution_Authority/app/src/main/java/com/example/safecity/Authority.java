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

public class Authority extends AppCompatActivity {
    private RelativeLayout rltvlayoutCat;

    private EditText edttxtsearch_bar;
    private Button btnFilter;
    private TextView txtviewfilter;
    private RecyclerView RecyclerViewrv;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    private ArrayList<AuthorityClass> authArray;
    private AdapterAuthority adapterAuth;
    private String category,city;

    //private AdapterHistory adapterHistory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        setContentView(R.layout.activity_authority);

        this.setTitle("Authority");

        rltvlayoutCat=findViewById(R.id.rltvlayoutCat);
        edttxtsearch_bar=findViewById(R.id.edttxtsearch_bar);
        btnFilter=findViewById(R.id.btnFilter);
        txtviewfilter=findViewById(R.id.txtviewfilter);
        RecyclerViewrv=findViewById(R.id.RecyclerViewrv);



        loadinfo();

        edttxtsearch_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterAuth.getFilter().filter(s);


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
                AlertDialog.Builder builder= new AlertDialog.Builder(Authority.this);
                builder.setTitle("Choose Sorting Criteria")
                        .setItems(SortTypes2.types2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selected= SortTypes2.types2[which];
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
        authArray=new ArrayList<>();

        DatabaseReference reff=FirebaseDatabase.getInstance().getReference("AuthorityInfo");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authArray.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    AuthorityClass dox = ds.getValue(AuthorityClass.class);
                    authArray.add(dox);
                }

                if ("Name Ascending".equals(selected)) {
                    Collections.sort(authArray, new Comparator<AuthorityClass>() {
                        @SuppressLint("SuspiciousIndentation")
                        @Override
                        public int compare(AuthorityClass authority1, AuthorityClass authority2) {
                            String name1 = authority1.getName();
                            String name2 = authority2.getName();

                            // Handle null names
                            if (name1 == null && name2 == null) {
                                return 0; // Both names are null
                            } else if (name1 == null) {
                                return -1; // Null name comes before non-null name
                            } else if (name2 == null) {
                                return 1;
                            }// Non-null name comes after null name

                                // Compare names in ascending order
                                return name1.compareTo(name2);

                        }
                        });
                    }
                else if ("Name Descending".equals(selected)) {
                        Collections.sort(authArray, new Comparator<AuthorityClass>() {
                            @Override
                            public int compare(AuthorityClass authority1, AuthorityClass authority2) {
                                String name1 = authority1.getName();
                                String name2 = authority2.getName();

                                // Handle null names
                                if (name1 == null && name2 == null) {
                                    return 0; // Both names are null
                                } else if (name1 == null) {
                                    return 1; // Null name comes after non-null name
                                } else if (name2 == null) {
                                    return -1;} // Non-null name comes before null name

                                    // Compare names in descending order
                                    return name2.compareTo(name1);
                                }
                            });
                        }

 else if ("Address Ascending".equals(selected)) {
                    Collections.sort(authArray, new Comparator<AuthorityClass>() {
                        @Override
                        public int compare(AuthorityClass photo1, AuthorityClass photo2) {
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
                            return photo1.getCity().compareTo(photo2.getCity());



                        }
                    });
                } else if ("Address Descending".equals(selected)) {
                    Collections.sort(authArray, new Comparator<AuthorityClass>() {
                        @Override
                        public int compare(AuthorityClass photo1, AuthorityClass photo2) {
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
                            return photo2.getCity().compareTo(photo1.getCity());


                        }
                    });
                }
                adapterAuth= new AdapterAuthority(getApplicationContext(),authArray);
                RecyclerViewrv.setAdapter(adapterAuth);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadinfo() {
        authArray=new ArrayList<>();

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("AuthorityInfo");
        reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        authArray.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            //String T=dataSnapshot.child("accountType").getValue().toString();
                            AuthorityClass authPhoto = dataSnapshot.getValue(AuthorityClass.class);
                            //if(!T.equalsIgnoreCase("GeneralUser"));
                            // {

                            authArray.add(authPhoto);
                            //}

                        }

                        adapterAuth= new AdapterAuthority(getApplicationContext(),authArray);
                        RecyclerViewrv.setAdapter(adapterAuth);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}
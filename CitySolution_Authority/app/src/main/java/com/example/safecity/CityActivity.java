package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {
    private RelativeLayout rltvlayoutCat;

    private EditText edttxtsearch_bar;
    private Button btnFilter;
    private TextView txtviewfilter;
    private RecyclerView RecyclerViewrv;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    UserPhoto userPhoto ;

    //private ArrayList<UserPhoto> photoArray;
    private ArrayList<String> photoArray;
    private AdapterCity adapterProblem;
    private String category,city;
    Button btn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        this.setTitle("Problems");

        rltvlayoutCat=findViewById(R.id.rltvlayoutCat);
        edttxtsearch_bar=findViewById(R.id.edttxtsearch_bar);
        btnFilter=findViewById(R.id.btnFilter);
        txtviewfilter=findViewById(R.id.txtviewfilter);
        RecyclerViewrv=findViewById(R.id.RecyclerViewrv);
        //category = getIntent().getStringExtra("CATEGORY");
       // city = getIntent().getStringExtra("EMPLOYEE_CITY");
        //btn=findViewById(R.id.button);

        loadinfo();
    }
    private void loadinfo() {
        photoArray=new ArrayList<>();
        //Toast.makeText(this, "Found", Toast.LENGTH_SHORT).show();
//reff.equalTo("PhotoLocation")
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("PhotoLocation");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        photoArray.clear();

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            String cityName = dataSnapshot.getKey();
                            //Toast.makeText(CityActivity.this, "Foundddd"+cityName, Toast.LENGTH_SHORT).show();

                            // Check if the city name is not null before adding it to the list
                            if (cityName != null) {
                                photoArray.add(cityName);
                            }
                            //String T=dataSnapshot.child("accountType").getValue().toString();

                            //userPhoto =dataSnapshot.getValue(UserPhoto.class);
                           // userPhoto.setCity(dataSnapshot.toString().trim());
                            //Toast.makeText(CityActivity.this, "Value: "+userPhoto, Toast.LENGTH_SHORT).show();
                            //if(!T.equalsIgnoreCase("GeneralUser"));
                            // {

                           // photoArray.add(userPhoto);
                            //}

                        }

                        adapterProblem= new AdapterCity(getApplicationContext(),photoArray);
                        RecyclerViewrv.setAdapter(adapterProblem);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
   /* private void loadinfo() {
        photoArray = new ArrayList<>();

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
        reff.equalTo("PhotoLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photoArray.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Assuming "userPhoto" is a string value, use String.class
                    String userPhoto = dataSnapshot.getValue(String.class);
                    Toast.makeText(CityActivity.this, userPhoto, Toast.LENGTH_SHORT).show();

                    // Check if the value is not null before adding it to the list
                    if (userPhoto != null) {
                        photoArray.add(userPhoto);
                    }
                }

                adapterProblem = new AdapterCity(getApplicationContext(), photoArray);
                RecyclerViewrv.setAdapter(adapterProblem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }*/

}
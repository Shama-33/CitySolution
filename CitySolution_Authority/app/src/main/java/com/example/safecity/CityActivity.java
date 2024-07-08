package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CityActivity extends AppCompatActivity {
    private EditText edttxtsearch_bar;
    private Button btnFilter;
    private TextView txtviewfilter;
    private RecyclerView RecyclerViewrv;
    private ArrayList<String> photoArray;
    private ArrayList<String> filteredArray;
    private AdapterCity adapterProblem;

    private List<String> bangladeshiCities = Arrays.asList(
            "Bandarban", "Barguna", "Barisal", "Bhola", "Bogra", "Brahmanbaria", "Chandpur", "Chittagong",
            "Chuadanga", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni",
            "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jashore", "Jhalokathi", "Jhenaidah",
            "Joypurhat", "Khagrachari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur",
            "Lalmonirhat", "Madaripur", "Magura", "Manikganj", "Meherpur", "Moulvibazar", "Munshiganj",
            "Mymensingh", "Naogaon", "Narail", "Narayanganj", "Narsingdi", "Natore", "Netrokona",
            "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari",
            "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajganj",
            "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        this.setTitle("Cities");

        edttxtsearch_bar = findViewById(R.id.edttxtsearch_bar);
        btnFilter = findViewById(R.id.btnFilter);
        txtviewfilter = findViewById(R.id.txtviewfilter);
        RecyclerViewrv = findViewById(R.id.RecyclerViewrv);

        RecyclerViewrv.setLayoutManager(new LinearLayoutManager(this));

        loadinfo();

        edttxtsearch_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
    }

    private void loadinfo() {
        photoArray = new ArrayList<>();
        filteredArray = new ArrayList<>();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("PhotoLocation");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photoArray.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String cityName = dataSnapshot.getKey();
                    if (cityName != null && bangladeshiCities.contains(cityName) ) {
                        //&& bangladeshiCities.contains(cityName)
                        photoArray.add(cityName);
                    }
                }
                filteredArray.addAll(photoArray);
                adapterProblem = new AdapterCity(getApplicationContext(), filteredArray);
                RecyclerViewrv.setAdapter(adapterProblem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void filter(String text) {
        filteredArray.clear();
        for (String item : photoArray) {
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filteredArray.add(item);
            }
        }
        adapterProblem.notifyDataSetChanged();
    }

    private void showSortDialog() {
        final String[] sortOptions = {"Address Ascending", "Address Descending", "No Sort"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setItems(sortOptions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Collections.sort(filteredArray, String::compareTo);
                            txtviewfilter.setText("Sorted by Address Ascending");
                            break;
                        case 1:
                            Collections.sort(filteredArray, Collections.reverseOrder());
                            txtviewfilter.setText("Sorted by Address Descending");
                            break;
                        case 2:
                            txtviewfilter.setText("No Filtering");
                            filteredArray.clear();
                            filteredArray.addAll(photoArray);
                            break;
                    }
                    adapterProblem.notifyDataSetChanged();
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.camenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.SignOutCAMenuId)
        {

            //FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        else if (item.getItemId()==R.id.CACityMenuId)
        {
            Intent i=new Intent(getApplicationContext(),AuthComplainActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }


}

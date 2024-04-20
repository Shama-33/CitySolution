package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelfGraphActivity extends AppCompatActivity {
    String UID;
    int f=0,d=0,h=0;


    //graphCC
    private LineChart lineChart;
    private BarChart barChart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_graph);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));


        lineChart = findViewById(R.id.graphCC4);
        barChart = findViewById(R.id.bargraphCC4);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        UID= FirebaseAuth.getInstance().getUid();

        count(UID);





    }
    private void count(String uid) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("UserPhotos").child(uid);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    Toast.makeText(SelfGraphActivity.this, "Information Not Found ", Toast.LENGTH_SHORT).show();
                    return;
                }


                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if (dataSnapshot.child("status").exists()) {
                        UserPhoto up=new UserPhoto();
                        up=dataSnapshot.getValue(UserPhoto.class);
                        if(up.getStatus().equalsIgnoreCase("pending"))
                        {
                            d++;

                        }
                        else if(up.getStatus().equalsIgnoreCase("acknowledged"))
                        {
                               f++;
                        }
                        else if(up.getStatus().equalsIgnoreCase("solved"))
                        {
                            h++;

                        }
                       // Toast.makeText(SelfGraphActivity.this, "status", Toast.LENGTH_SHORT).show();
                        //d = (int) snapshot.child("Damaged_Road").getChildrenCount();
                    }
                    else
                    {
                        return;
                    }

                }


                setupLineChart2();
                setupBarChart2();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }








    private void setupLineChart2() {
        // Create a list of entries for the chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, d));
        entries.add(new Entry(2, f));
        entries.add(new Entry(3, h));


        // Create a data set with the entries
        LineDataSet dataSet = new LineDataSet(entries, "Status");

        // Customize the appearance of the data set (color, etc.)
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a line data object with the data set
        LineData lineData = new LineData(dataSet);

        // Set the data to the chart
        lineChart.setData(lineData);

        // Customize X-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new SelfGraphActivity.XAxisValueFormatter2());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Customize Y-axis
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        // Refresh the chart
        lineChart.invalidate();
    }



    private void setupBarChart2() {
        // Create a list of entries for the chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, d));
        entries.add(new BarEntry(2, f));
        entries.add(new BarEntry(3, h));



        BarDataSet dataSet = new BarDataSet(entries, "Status");


        dataSet.setColor(getResources().getColor(R.color.light_blue_400));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a bar data object with the data set
        BarData barData = new BarData(dataSet);

        // Set the data to the chart
        barChart.setData(barData);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new SelfGraphActivity.XAxisValueFormatter2());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);


        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);


        barChart.invalidate();
    }

    private class XAxisValueFormatter2 extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Customize the labels on the X-axis
            switch ((int) value) {
                case 1:
                    return "   Pending";
                case 2:
                    return "Processing" ;
                case 3:
                    return "Solved";

                default:
                    return "";
            }
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
        else if (item.getItemId()==R.id.NotificationMenuId)
        {
            Intent i=new Intent(getApplicationContext(),NotificationActivity.class);
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
        else if (item.getItemId()==R.id.AboutUsMenuId)
        {
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
            finish();


        }
        else if (item.getItemId()==R.id.SettingsMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(i);




        }
        else if (item.getItemId()==R.id.CCGraphMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),GraphActivity1.class);
            startActivity(i);



        }
        return super.onOptionsItemSelected(item);
    }








}
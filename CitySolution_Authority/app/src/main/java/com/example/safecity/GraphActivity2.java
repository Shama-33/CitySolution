package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

public class GraphActivity2 extends AppCompatActivity {
    String GRAPH_CITY;
    int d,f,t;
    //graphCC
    private LineChart lineChart;
    private BarChart barChart;
    private EditText edttxtcityg1;
    private Button btnEnterCityg1;
    private TextView txtEnterCityg1;
    private BottomNavigationView bot_nav_view;
    // private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        this.setTitle("Progress Wise Graph");



// Inside onCreate or onCreateView
        //pieChart = findViewById(R.id.piegraphCC2);

// Call the setupPieChart2 method in your count method
        //setupPieChart2();

        edttxtcityg1=findViewById(R.id.edttxtcityg2);
        btnEnterCityg1=findViewById(R.id.btnEnterCityg2);

        lineChart = findViewById(R.id.graphCC2);
        barChart = findViewById(R.id.bargraphCC2);
        txtEnterCityg1=findViewById(R.id.txtEnterCityg2);
        bot_nav_view=findViewById(R.id.bot_nav_view_hos2);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        bot_nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homemenu:
                        Intent intent = new Intent(getApplicationContext(),GraphActivity3.class);
                        startActivity(intent);
                        return true;


                    case R.id.hospitalmenu:
                        //getSupportFragmentManager().beginTransaction().replace(androidx.fragment.R.id.fragment_container_view_tag,HospitalFragment).commit();

                        return true;


                }
                return false;
            }
        });
        btnEnterCityg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GRAPH_CITY=edttxtcityg1.getText().toString().trim();
                if (!GRAPH_CITY.isEmpty()) {

                    GRAPH_CITY = GRAPH_CITY.substring(0, 1).toUpperCase() + GRAPH_CITY.substring(1).toLowerCase();

                }
                else
                {
                    Toast.makeText(GraphActivity2.this, "Enter a City!!!", Toast.LENGTH_SHORT).show();
                }
                count(GRAPH_CITY);

            }
        });

    }
    private void count(String city) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Count").child(city);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    Toast.makeText(GraphActivity2.this, "City does not exist in our database ", Toast.LENGTH_SHORT).show();
                    clearGraphViews();
                    return;
                }
                //  txtEnterCityg1.setText("Progress of : "+GRAPH_CITY);
                if (snapshot.child("num_pending").exists()) {
                    d = snapshot.child("num_pending").getValue(Integer.class);
                }
                if (snapshot.child("num_ack").exists()) {
                    f = (int) snapshot.child("num_ack").getValue(Integer.class);
                }
                if (snapshot.child("num_solved").exists()) {
                    t = (int) snapshot.child("num_solved").getValue(Integer.class);
                }

                setupLineChart2();
                setupBarChart2();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });
    }




    private void setupLineChart2() {
        // Create a list of entries for the chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, d));
        entries.add(new Entry(2, f));
        entries.add(new Entry(3, t));


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
        xAxis.setValueFormatter(new GraphActivity2.XAxisValueFormatter2());
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
        entries.add(new BarEntry(3, t));

        // Create a data set with the entries
        BarDataSet dataSet = new BarDataSet(entries, "Status");

        // Customize the appearance of the data set (color, etc.)
        dataSet.setColor(getResources().getColor(R.color.light_blue_400));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a bar data object with the data set
        BarData barData = new BarData(dataSet);

        // Set the data to the chart
        barChart.setData(barData);

        // Customize X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new GraphActivity2.XAxisValueFormatter2());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);  // Adjust the granularity to ensure all labels are visible

        // Customize Y-axis
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        // Refresh the chart
        barChart.invalidate();
    }

    private class XAxisValueFormatter2 extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Customize the labels on the X-axis
            switch ((int) value) {
                case 1:
                    return "   pending";
                case 2:
                    return "processing" ;
                case 3:
                    return "solved";

                default:
                    return "";
            }
        }
    }

    /*
    private void setupPieChart2() {
        // Calculate the total value
        int total = d + f + t;

        // Create a list of entries for the chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) d / total * 100, "Pending"));
        entries.add(new PieEntry((float) f / total * 100, "Processing"));
        entries.add(new PieEntry((float) t / total * 100, "Solved"));

        // Create a data set with the entries
        PieDataSet dataSet = new PieDataSet(entries, "Categories");

        // Customize the appearance of the data set (colors, etc.)
        dataSet.setColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.light_blue_400),
                getResources().getColor(R.color.colorAccent));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a pie data object with the data set
        PieData pieData = new PieData(dataSet);

        // Set the data to the chart
        pieChart.setData(pieData);

        // Customize chart settings
        pieChart.getDescription().setEnabled(false);
       // pieChart.setCenterText("Distribution");
        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleRadius(25f);

        // Refresh the chart
        pieChart.invalidate();
    }


    private void setupPieChart() {
        // Create a list of entries for the chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(d, "Pending"));
        entries.add(new PieEntry(f, "Processing"));
        entries.add(new PieEntry(t, "Solved"));

        // Create a data set with the entries
        PieDataSet dataSet = new PieDataSet(entries, "Categories");

        // Customize the appearance of the data set (colors, etc.)
        dataSet.setColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.light_blue_400),
                getResources().getColor(R.color.colorAccent));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a pie data object with the data set
        PieData pieData = new PieData(dataSet);

        // Set the data to the chart
        pieChart.setData(pieData);

        // Customize chart settings if needed
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);

        // Refresh the chart
        pieChart.invalidate();
    }*/


    /*
     <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_below="@+id/ll2g2"
                android:layout_marginTop="30dp"
                android:id="@+id/ll3g2">

                <com.github.mikephil.charting.charts.PieChart
                    android:id="@+id/piegraphCC2"
                    android:layout_width="364dp"
                    android:layout_height="229dp"
                    android:layout_marginStart="20dp"
                    android:layout_marginTop="20dp"
                    android:layout_marginEnd="20dp"
                    android:layout_marginBottom="20dp"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintEnd_toEndOf="parent"
                    app:layout_constraintHorizontal_bias="0.709"
                    app:layout_constraintStart_toStartOf="parent"
                    app:layout_constraintTop_toTopOf="parent"
                    app:layout_constraintVertical_bias="0.752" />




            </LinearLayout>

     */



    private void clearGraphViews() {
        // Set empty or null data to clear the graph views
        lineChart.setData(null);
        barChart.setData(null);

        // Refresh the charts
        lineChart.invalidate();
        barChart.invalidate();
    }



}

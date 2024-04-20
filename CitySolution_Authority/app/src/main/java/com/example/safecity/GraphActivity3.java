package com.example.safecity;

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

public class GraphActivity3 extends AppCompatActivity {
    String GRAPH_CITY;
    int f, d, h, t, o;
    private float x1, y1, x2, y2;
    private static final int MIN_SWIPE_DISTANCE = 150;
    //graphCC
    private LineChart lineChart;
    private BarChart barChart;
    private EditText edttxtcityg1;
    private Button btnEnterCityg1;
    BottomNavigationView bot_nav_view;
    private TextView txtEnterCityg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph3);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        this.setTitle("Category Wise Graph");

        edttxtcityg1 = findViewById(R.id.edttxtcityg1);
        btnEnterCityg1 = findViewById(R.id.btnEnterCityg1);

        lineChart = findViewById(R.id.graphCC);
        barChart = findViewById(R.id.bargraphCC);
        txtEnterCityg1 = findViewById(R.id.txtEnterCityg1);
        bot_nav_view = findViewById(R.id.bot_nav_view_hos);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        bot_nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homemenu:

                        return true;
                    case R.id.hospitalmenu:
                        //getSupportFragmentManager().beginTransaction().replace(androidx.fragment.R.id.fragment_container_view_tag,HospitalFragment).commit();

                        Intent intent = new Intent(getApplicationContext(), GraphActivity2.class);
                        startActivity(intent);
                        return true;


                }
                return false;
            }
        });
        btnEnterCityg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GRAPH_CITY = edttxtcityg1.getText().toString().trim();
                if (!GRAPH_CITY.isEmpty()) {

                    GRAPH_CITY = GRAPH_CITY.substring(0, 1).toUpperCase() + GRAPH_CITY.substring(1).toLowerCase();
                } else {
                    Toast.makeText(GraphActivity3.this, "Enter a City!!!", Toast.LENGTH_SHORT).show();
                }
                count(GRAPH_CITY);

            }
        });

    }

    private void count(String city) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("PhotoLocation").child(city);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(GraphActivity3.this, "City does not exist in our database ", Toast.LENGTH_SHORT).show();
                    clearGraphViews();
                    return;
                }
                // txtEnterCityg1.setText("Showing Different Categories of Problem of : "+GRAPH_CITY);
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

                // Update your UI elements with the counts here
                setupLineChart2();
                setupBarChart2();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });
    }

    private void setupLineChart() {
        // Create a list of entries for the chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, d));
        entries.add(new Entry(2, f));
        entries.add(new Entry(3, h));
        entries.add(new Entry(4, t));
        entries.add(new Entry(5, o));

        // Create a data set with the entries
        LineDataSet dataSet = new LineDataSet(entries, "Categories");

        // Customize the appearance of the data set (color, etc.)
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a line data object with the data set
        LineData lineData = new LineData(dataSet);

        // Set the data to the chart
        lineChart.setData(lineData);

        // Customize X-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        // Customize Y-axis
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        // Refresh the chart
        lineChart.invalidate();
    }

    private class XAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Customize the labels on the X-axis
            switch ((int) value) {
                case 0:
                    return "D_road";
                case 1:
                    return "Flood";
                case 2:
                    return "Homeless";
                case 3:
                    return "Trash";
                case 4:
                    return "other";
                default:
                    return "";
            }
        }
    }


    private void setupLineChart2() {
        // Create a list of entries for the chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, d));
        entries.add(new Entry(2, f));
        entries.add(new Entry(3, h));
        entries.add(new Entry(4, t));
        entries.add(new Entry(5, o));

        // Create a data set with the entries
        LineDataSet dataSet = new LineDataSet(entries, "Categories");

        // Customize the appearance of the data set (color, etc.)
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a line data object with the data set
        LineData lineData = new LineData(dataSet);

        // Set the data to the chart
        lineChart.setData(lineData);

        // Customize X-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter2());
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
        entries.add(new BarEntry(4, t));
        entries.add(new BarEntry(5, o));

        // Create a data set with the entries
        BarDataSet dataSet = new BarDataSet(entries, "Categories");

        // Customize the appearance of the data set (color, etc.)
        dataSet.setColor(getResources().getColor(R.color.light_blue_400));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Create a bar data object with the data set
        BarData barData = new BarData(dataSet);

        // Set the data to the chart
        barChart.setData(barData);

        // Customize X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter2());
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
                    return "   Road";
                case 2:
                    return "Flood";
                case 3:
                    return "Homeless";
                case 4:
                    return "Trash";
                case 5:
                    return "Others";
                default:
                    return "";
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_SWIPE_DISTANCE) {
                    if (x1 < x2) {
                        // Left to Right swipe
                        Intent i = new Intent(getApplicationContext(), GraphActivity2.class);
                        startActivity(i);

                    }
                    // Add handling for Right to Left swipe if needed
                }
                break;
        }
        return super.onTouchEvent(touchEvent);
    }



    private void clearGraphViews() {
        // Set empty or null data to clear the graph views
        lineChart.setData(null);
        barChart.setData(null);

        // Refresh the charts
        lineChart.invalidate();
        barChart.invalidate();
    }
}











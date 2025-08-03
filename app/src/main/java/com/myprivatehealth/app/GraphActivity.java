package com.myprivatehealth.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerUser;
    private LineChart chartBloodPressure;
    private LineChart chartWeight;
    private LineChart chartBodyComposition;
    private LineChart chartBmi;
    private android.widget.Button btnBackToMain;

    private DatabaseHelper databaseHelper;
    private List<User> userList;
    private List<HealthData> healthDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        initializeViews();

        // Load users
        loadUsers();

        // Set up user selection listener
        setupUserSelectionListener();

        // Set up back to main menu button
        btnBackToMain.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });
    }

    private void initializeViews() {
        spinnerUser = findViewById(R.id.spinner_user);
        chartBloodPressure = findViewById(R.id.chart_blood_pressure);
        chartWeight = findViewById(R.id.chart_weight);
        chartBodyComposition = findViewById(R.id.chart_body_composition);
        chartBmi = findViewById(R.id.chart_bmi);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        // Configure charts
        configureCharts();
    }

    private void configureCharts() {
        // Configure Blood Pressure Chart
        configureChart(chartBloodPressure, "Blood Pressure (mmHg)");
        
        // Configure Weight Chart
        configureChart(chartWeight, "Weight (kg)");
        
        // Configure Body Composition Chart
        configureChart(chartBodyComposition, "Body Composition (%)");
        
        // Configure BMI Chart
        configureChart(chartBmi, "BMI");
    }

    private void configureChart(LineChart chart, String label) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);
    }

    private void loadUsers() {
        userList = databaseHelper.getAllUsers();
        if (userList.isEmpty()) {
            Toast.makeText(this, "Please add users first", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_dropdown_item_1line, userList);
        spinnerUser.setAdapter(adapter);
    }

    private void setupUserSelectionListener() {
        spinnerUser.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    User selectedUser = userList.get(position);
                    loadHealthDataForUser(selectedUser.getId());
                }
            }
        });
    }

    private void loadHealthDataForUser(int userId) {
        healthDataList = databaseHelper.getHealthDataByUser(userId);
        
        if (healthDataList.isEmpty()) {
            Toast.makeText(this, "No health data available for this user", Toast.LENGTH_SHORT).show();
            clearCharts();
            return;
        }

        // Sort data by date
        Collections.sort(healthDataList, new Comparator<HealthData>() {
            @Override
            public int compare(HealthData o1, HealthData o2) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    return sdf.parse(o1.getDate()).compareTo(sdf.parse(o2.getDate()));
                } catch (ParseException e) {
                    return 0;
                }
            }
        });

        updateCharts();
    }

    private void updateCharts() {
        updateBloodPressureChart();
        updateWeightChart();
        updateBodyCompositionChart();
        updateBmiChart();
    }

    private void updateBloodPressureChart() {
        ArrayList<Entry> systolicEntries = new ArrayList<>();
        ArrayList<Entry> diastolicEntries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < healthDataList.size(); i++) {
            HealthData data = healthDataList.get(i);
            systolicEntries.add(new Entry(i, data.getSystolicPressure()));
            diastolicEntries.add(new Entry(i, data.getDiastolicPressure()));
            dates.add(data.getDate());
        }

        LineDataSet systolicDataSet = new LineDataSet(systolicEntries, "Systolic");
        systolicDataSet.setColor(Color.RED);
        systolicDataSet.setCircleColor(Color.RED);
        systolicDataSet.setLineWidth(2f);
        systolicDataSet.setCircleRadius(4f);

        LineDataSet diastolicDataSet = new LineDataSet(diastolicEntries, "Diastolic");
        diastolicDataSet.setColor(Color.BLUE);
        diastolicDataSet.setCircleColor(Color.BLUE);
        diastolicDataSet.setLineWidth(2f);
        diastolicDataSet.setCircleRadius(4f);

        LineData lineData = new LineData(systolicDataSet, diastolicDataSet);
        chartBloodPressure.setData(lineData);

        // Set X-axis labels
        XAxis xAxis = chartBloodPressure.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.size(), true);

        chartBloodPressure.invalidate();
    }

    private void updateWeightChart() {
        ArrayList<Entry> weightEntries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < healthDataList.size(); i++) {
            HealthData data = healthDataList.get(i);
            weightEntries.add(new Entry(i, (float) data.getWeight()));
            dates.add(data.getDate());
        }

        LineDataSet weightDataSet = new LineDataSet(weightEntries, "Weight");
        weightDataSet.setColor(Color.GREEN);
        weightDataSet.setCircleColor(Color.GREEN);
        weightDataSet.setLineWidth(2f);
        weightDataSet.setCircleRadius(4f);

        LineData lineData = new LineData(weightDataSet);
        chartWeight.setData(lineData);

        // Set X-axis labels
        XAxis xAxis = chartWeight.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.size(), true);

        chartWeight.invalidate();
    }

    private void updateBodyCompositionChart() {
        ArrayList<Entry> fatEntries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < healthDataList.size(); i++) {
            HealthData data = healthDataList.get(i);
            fatEntries.add(new Entry(i, (float) data.getFatPercentage()));
            dates.add(data.getDate());
        }

        LineDataSet fatDataSet = new LineDataSet(fatEntries, "Fat Percentage");
        fatDataSet.setColor(Color.MAGENTA);
        fatDataSet.setCircleColor(Color.MAGENTA);
        fatDataSet.setLineWidth(2f);
        fatDataSet.setCircleRadius(4f);

        LineData lineData = new LineData(fatDataSet);
        chartBodyComposition.setData(lineData);

        // Set X-axis labels
        XAxis xAxis = chartBodyComposition.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.size(), true);

        chartBodyComposition.invalidate();
    }

    private void updateBmiChart() {
        ArrayList<Entry> bmiEntries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < healthDataList.size(); i++) {
            HealthData data = healthDataList.get(i);
            bmiEntries.add(new Entry(i, (float) data.getBmi()));
            dates.add(data.getDate());
        }

        LineDataSet bmiDataSet = new LineDataSet(bmiEntries, "BMI");
        bmiDataSet.setColor(Color.CYAN);
        bmiDataSet.setCircleColor(Color.CYAN);
        bmiDataSet.setLineWidth(2f);
        bmiDataSet.setCircleRadius(4f);

        LineData lineData = new LineData(bmiDataSet);
        chartBmi.setData(lineData);

        // Set X-axis labels
        XAxis xAxis = chartBmi.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setLabelCount(dates.size(), true);

        chartBmi.invalidate();
    }

    private void clearCharts() {
        chartBloodPressure.clear();
        chartWeight.clear();
        chartBodyComposition.clear();
        chartBmi.clear();
        
        chartBloodPressure.invalidate();
        chartWeight.invalidate();
        chartBodyComposition.invalidate();
        chartBmi.invalidate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 
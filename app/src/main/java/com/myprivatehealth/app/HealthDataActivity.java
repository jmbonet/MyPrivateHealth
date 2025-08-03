package com.myprivatehealth.app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HealthDataActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerUser;
    private TextView tvSelectedDate;
    private EditText etSystolicPressure;
    private EditText etDiastolicPressure;
    private EditText etWeight;
    private EditText etFatPercentage;
    private TextView tvBmi;
    private Button btnSelectDate;
    private Button btnSaveHealthData;
    private Button btnCalculateBmi;
    private Button btnBackToMain;

    private DatabaseHelper databaseHelper;
    private Calendar selectedDate;
    private SimpleDateFormat dateFormat;
    private List<User> userList;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_data);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = Calendar.getInstance();

        // Initialize views
        initializeViews();

        // Load users
        loadUsers();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        spinnerUser = findViewById(R.id.spinner_user);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        etSystolicPressure = findViewById(R.id.et_systolic_pressure);
        etDiastolicPressure = findViewById(R.id.et_diastolic_pressure);
        etWeight = findViewById(R.id.et_weight);
        etFatPercentage = findViewById(R.id.et_fat_percentage);
        tvBmi = findViewById(R.id.tv_bmi);
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSaveHealthData = findViewById(R.id.btn_save_health_data);
        btnCalculateBmi = findViewById(R.id.btn_calculate_bmi);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        // Set current date
        updateDateDisplay();
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
        
        // Set up selection listener
        spinnerUser.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    selectedUser = userList.get(position);
                    checkExistingHealthData();
                }
            }
        });
        
        // Set default selection
        if (!userList.isEmpty()) {
            selectedUser = userList.get(0);
        }
    }

    private void setupClickListeners() {
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnCalculateBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        btnSaveHealthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHealthData();
            }
        });

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateDisplay();
                        
                        // Check if health data exists for this user and date
                        checkExistingHealthData();
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        tvSelectedDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void checkExistingHealthData() {
        if (selectedUser != null) {
            String dateStr = dateFormat.format(selectedDate.getTime());
            HealthData existingData = databaseHelper.getHealthDataByUserAndDate(selectedUser.getId(), dateStr);
            
            if (existingData != null) {
                // Load existing data
                etSystolicPressure.setText(String.valueOf(existingData.getSystolicPressure()));
                etDiastolicPressure.setText(String.valueOf(existingData.getDiastolicPressure()));
                etWeight.setText(String.valueOf(existingData.getWeight()));
                etFatPercentage.setText(String.valueOf(existingData.getFatPercentage()));
                tvBmi.setText(String.format("BMI: %.2f", existingData.getBmi()));
                btnSaveHealthData.setText("Update Health Data");
            } else {
                // Clear fields for new data
                etSystolicPressure.setText("");
                etDiastolicPressure.setText("");
                etWeight.setText("");
                etFatPercentage.setText("");
                tvBmi.setText("BMI: --");
                btnSaveHealthData.setText("Save Health Data");
            }
        }
    }

    private void calculateBMI() {
        String weightStr = etWeight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            Toast.makeText(this, "Please enter weight first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedUser == null) {
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double bmi = HealthData.calculateBMI(weight, selectedUser.getHeight());
            tvBmi.setText(String.format("BMI: %.2f", bmi));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveHealthData() {
        if (selectedUser == null) {
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
            return;
        }

        String systolicStr = etSystolicPressure.getText().toString().trim();
        String diastolicStr = etDiastolicPressure.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String fatStr = etFatPercentage.getText().toString().trim();

        if (systolicStr.isEmpty() || diastolicStr.isEmpty() || weightStr.isEmpty() || fatStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int systolic = Integer.parseInt(systolicStr);
            int diastolic = Integer.parseInt(diastolicStr);
            double weight = Double.parseDouble(weightStr);
            double fatPercentage = Double.parseDouble(fatStr);

            if (systolic <= 0 || diastolic <= 0 || weight <= 0 || fatPercentage < 0) {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                return;
            }

            String dateStr = dateFormat.format(selectedDate.getTime());
            double bmi = HealthData.calculateBMI(weight, selectedUser.getHeight());

            HealthData healthData = new HealthData(selectedUser.getId(), dateStr, 
                    systolic, diastolic, weight, fatPercentage, bmi);

            // Check if data already exists
            HealthData existingData = databaseHelper.getHealthDataByUserAndDate(selectedUser.getId(), dateStr);
            if (existingData != null) {
                healthData.setId(existingData.getId());
                int result = databaseHelper.updateHealthData(healthData);
                if (result > 0) {
                    Toast.makeText(this, "Health data updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update health data", Toast.LENGTH_SHORT).show();
                }
            } else {
                long result = databaseHelper.addHealthData(healthData);
                if (result != -1) {
                    Toast.makeText(this, "Health data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to save health data", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 
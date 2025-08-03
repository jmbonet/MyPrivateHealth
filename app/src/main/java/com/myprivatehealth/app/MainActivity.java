package com.myprivatehealth.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cardManageUsers;
    private CardView cardAddHealthData;
    private CardView cardViewGraphs;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        cardManageUsers = findViewById(R.id.card_manage_users);
        cardAddHealthData = findViewById(R.id.card_add_health_data);
        cardViewGraphs = findViewById(R.id.card_view_graphs);

        // Set click listeners
        cardManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });

        cardAddHealthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HealthDataActivity.class);
                startActivity(intent);
            }
        });

        cardViewGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if there are users in the database
        if (databaseHelper.getAllUsers().isEmpty()) {
            Toast.makeText(this, "Please add users first", Toast.LENGTH_LONG).show();
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
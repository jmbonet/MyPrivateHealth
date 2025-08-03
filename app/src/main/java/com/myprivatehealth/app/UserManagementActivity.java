package com.myprivatehealth.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAddUser;
    private android.widget.Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_users);
        fabAddUser = findViewById(R.id.fab_add_user);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        // Initialize user list
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        // Set up floating action button
        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });

        // Set up back to main menu button
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load users
        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(databaseHelper.getAllUsers());
        userAdapter.notifyDataSetChanged();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        
        final EditText etName = dialogView.findViewById(R.id.et_user_name);
        final EditText etAge = dialogView.findViewById(R.id.et_user_age);
        final EditText etHeight = dialogView.findViewById(R.id.et_user_height);

        builder.setView(dialogView)
                .setTitle("Add New User")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString().trim();
                        String ageStr = etAge.getText().toString().trim();
                        String heightStr = etHeight.getText().toString().trim();

                        if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty()) {
                            Toast.makeText(UserManagementActivity.this, 
                                    "Please fill all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            int age = Integer.parseInt(ageStr);
                            double height = Double.parseDouble(heightStr);

                            if (age <= 0 || height <= 0) {
                                Toast.makeText(UserManagementActivity.this, 
                                        "Please enter valid values", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            User user = new User(name, age, height);
                            long result = databaseHelper.addUser(user);

                            if (result != -1) {
                                Toast.makeText(UserManagementActivity.this, 
                                        "User added successfully", Toast.LENGTH_SHORT).show();
                                loadUsers();
                            } else {
                                Toast.makeText(UserManagementActivity.this, 
                                        "Failed to add user", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(UserManagementActivity.this, 
                                    "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public void showEditUserDialog(final User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        
        final EditText etName = dialogView.findViewById(R.id.et_user_name);
        final EditText etAge = dialogView.findViewById(R.id.et_user_age);
        final EditText etHeight = dialogView.findViewById(R.id.et_user_height);

        // Pre-fill the fields
        etName.setText(user.getName());
        etAge.setText(String.valueOf(user.getAge()));
        etHeight.setText(String.valueOf(user.getHeight()));

        builder.setView(dialogView)
                .setTitle("Edit User")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString().trim();
                        String ageStr = etAge.getText().toString().trim();
                        String heightStr = etHeight.getText().toString().trim();

                        if (name.isEmpty() || ageStr.isEmpty() || heightStr.isEmpty()) {
                            Toast.makeText(UserManagementActivity.this, 
                                    "Please fill all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            int age = Integer.parseInt(ageStr);
                            double height = Double.parseDouble(heightStr);

                            if (age <= 0 || height <= 0) {
                                Toast.makeText(UserManagementActivity.this, 
                                        "Please enter valid values", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            user.setName(name);
                            user.setAge(age);
                            user.setHeight(height);

                            int result = databaseHelper.updateUser(user);

                            if (result > 0) {
                                Toast.makeText(UserManagementActivity.this, 
                                        "User updated successfully", Toast.LENGTH_SHORT).show();
                                loadUsers();
                            } else {
                                Toast.makeText(UserManagementActivity.this, 
                                        "Failed to update user", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(UserManagementActivity.this, 
                                    "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    public void showDeleteUserDialog(final User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getName() + "? This will also delete all their health data.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int result = databaseHelper.deleteUser(user.getId());
                        if (result > 0) {
                            Toast.makeText(UserManagementActivity.this, 
                                    "User deleted successfully", Toast.LENGTH_SHORT).show();
                            loadUsers();
                        } else {
                            Toast.makeText(UserManagementActivity.this, 
                                    "Failed to delete user", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 
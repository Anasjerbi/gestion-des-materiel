package com.example.gestionmateriel;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Mainpage extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    dbtable dbHelper;
    ArrayList<String> materialIds, materialName, materialDescription, serialNumber;
    MaterialAdapter adapter;
    ImageView emptyImageView;
    TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        emptyImageView = findViewById(R.id.empty_imageview);
        noDataTextView = findViewById(R.id.no_data);

        // Initialize database helper and data lists
        dbHelper = new dbtable(this);
        materialIds = new ArrayList<>();
        materialName = new ArrayList<>();
        materialDescription = new ArrayList<>();
        serialNumber = new ArrayList<>();

        // Fetch and display data
        storeDataInArrays();

        // Set up the RecyclerView adapter
        adapter = new MaterialAdapter(Mainpage.this, this, materialIds, materialName, materialDescription, serialNumber);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add button listener
        add_button.setOnClickListener(view -> {
            Intent intent = new Intent(Mainpage.this, AddActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    // Fetch data from the database and store it in ArrayLists
    void storeDataInArrays() {
        Cursor cursor = dbHelper.readAllMaterials();
        if (cursor.getCount() == 0) {
            // Show "no data" UI if the database is empty
            emptyImageView.setVisibility(View.VISIBLE);
            noDataTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            while (cursor.moveToNext()) {
                materialIds.add(cursor.getString(0)); // Add ID
                materialName.add(cursor.getString(1)); // Add Name
                materialDescription.add(cursor.getString(2)); // Add Description
                serialNumber.add(cursor.getString(3)); // Add Serial Number
            }
            // Hide "no data" UI
            emptyImageView.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // Ensure the activity result is successful
            materialIds.clear();
            materialName.clear();
            materialDescription.clear();
            serialNumber.clear();
            storeDataInArrays(); // Re-fetch the data from the database
            adapter.notifyDataSetChanged(); // Notify the adapter of the data change
        }
    }
}

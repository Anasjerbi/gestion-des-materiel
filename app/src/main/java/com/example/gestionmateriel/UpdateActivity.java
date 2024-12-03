package com.example.gestionmateriel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText nameInput, descriptionInput, serialNumberInput;
    Button updateButton, deleteButton;
    String id, name, description, serialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize UI elements
        nameInput = findViewById(R.id.name_input1);
        descriptionInput = findViewById(R.id.description_input1);
        serialNumberInput = findViewById(R.id.serialnumber_input1);
        updateButton = findViewById(R.id.add_button1);
        deleteButton = findViewById(R.id.delete_button);

        // Get and set data passed from the intent
        getAndSetIntentData();

        // Set the action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }

        // Update button click listener
        updateButton.setOnClickListener(view -> {
            name = nameInput.getText().toString().trim();
            description = descriptionInput.getText().toString().trim();
            serialNumber = serialNumberInput.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || serialNumber.isEmpty()) {
                Toast.makeText(UpdateActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update material in the database
            dbtable myDB = new dbtable(UpdateActivity.this);
            boolean isUpdated = myDB.updateMaterial(id, name, description, serialNumber);

            if (isUpdated) {
                Toast.makeText(UpdateActivity.this, "Material updated successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Notify Mainpage of the update
                finish(); // Close this activity
            } else {
                Toast.makeText(UpdateActivity.this, "Failed to update material.", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button click listener
        deleteButton.setOnClickListener(view -> confirmDeleteDialog());
    }

    // Get data from intent and populate the fields
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("serialNumber")) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            serialNumber = getIntent().getStringExtra("serialNumber");

            // Set the fields with the received data
            nameInput.setText(name);
            descriptionInput.setText(description);
            serialNumberInput.setText(serialNumber);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    // Confirm deletion dialog
    void confirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Material");
        builder.setMessage("Are you sure you want to delete this material?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            dbtable myDB = new dbtable(UpdateActivity.this);
            boolean isDeleted = myDB.deleteMaterial(id);

            if (isDeleted) {
                Toast.makeText(UpdateActivity.this, "Material deleted successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Notify Mainpage of the deletion
                finish(); // Close this activity
            } else {
                Toast.makeText(UpdateActivity.this, "Failed to delete material.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}

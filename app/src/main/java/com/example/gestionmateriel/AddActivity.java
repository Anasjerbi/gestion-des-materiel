package com.example.gestionmateriel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private EditText name_input, description_input, serialnumber_input;
    private Button add_button;
    private dbtable dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        // Initialize UI components
        name_input = findViewById(R.id.name_input);
        description_input = findViewById(R.id.description_input);
        serialnumber_input = findViewById(R.id.serialnumber_input);
        add_button = findViewById(R.id.add_button);

        dbHelper = new dbtable(this);

        // Add button click listener
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = name_input.getText().toString().trim();
                String description = description_input.getText().toString().trim();
                String serialNumber = serialnumber_input.getText().toString().trim();

                // Validate input
                if (name.isEmpty() || description.isEmpty() || serialNumber.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert material into the database
                if (dbHelper.insertMaterial(name, description, serialNumber)) {
                    Toast.makeText(AddActivity.this, "Material added successfully!", Toast.LENGTH_SHORT).show();

                    // Notify parent activity of success
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the activity
                } else {
                    Toast.makeText(AddActivity.this, "Failed to add material.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

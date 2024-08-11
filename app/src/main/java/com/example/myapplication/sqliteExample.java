package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class sqliteExample extends AppCompatActivity {
    private EditText inputId, inputFullName;
    private Button btnAdd;
    private TextView data;
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_example);

        // Initialize DatabaseHelper
        dbHelper = new MyDbHelper(this);

        inputId = findViewById(R.id.inputId);
        inputFullName = findViewById(R.id.inputFullName);
        btnAdd = findViewById(R.id.btnAdd);
        data = findViewById(R.id.data);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get input values
                String idText = inputId.getText().toString();
                String name = inputFullName.getText().toString();

                if (idText.isEmpty() || name.isEmpty()) {
                    Toast.makeText(sqliteExample.this, "Please enter both ID and Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                int id = Integer.parseInt(idText);

                // Add to database
                dbHelper.addUser(id, name);

                // Show success message
                Toast.makeText(sqliteExample.this, "User added successfully", Toast.LENGTH_SHORT).show();

                // Clear input fields
                inputId.setText("");
                inputFullName.setText("");

                // Update TextView with data from database
                updateDataDisplay();
            }
        });

        // Initial load of data
        updateDataDisplay();
    }

    private void updateDataDisplay() {
        Cursor cursor = dbHelper.getAllUsers();
        if (cursor == null) {
            data.setText("No data available");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int idColumnIndex = cursor.getColumnIndexOrThrow("id");
        int nameColumnIndex = cursor.getColumnIndexOrThrow("name");

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            sb.append("ID: ").append(id).append(", Name: ").append(name).append("\n");
        }
        cursor.close();

        if (sb.length() == 0) {
            data.setText("No data available");
        } else {
            data.setText(sb.toString());
        }
    }

}

package com.example.classscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.classscheduler.models.Class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddToDoActivity extends AppCompatActivity {

    private Spinner spinnerItemType, spinnerAssociatedClass;
    private EditText editTextTitle, editTextDetails;
    private Button btnSubmit, btnCancel, btnOpenDatePicker;
    private TextView textViewDueDate;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        // Initialize UI elements
        spinnerItemType = findViewById(R.id.spinnerItemType);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDetails = findViewById(R.id.editTextDetails);
        spinnerAssociatedClass = findViewById(R.id.spinnerAssociatedClass);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        btnOpenDatePicker = findViewById(R.id.btnOpenDatePicker);
        textViewDueDate = findViewById(R.id.textViewDueDate);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#673AB7"));

        // Set up spinners
        setUpSpinners();

        // Set up Submit button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle submit action
                submitToDoItem();
            }
        });

        // Set up Cancel button click listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cancel action
                finish(); // Close the activity
            }
        });

        // Set up DatePicker button click listener
        btnOpenDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show DatePicker dialog when the button is clicked
                showDatePickerDialog();
            }
        });
    }

    private void setUpSpinners() {
        // Set up Spinner for ToDoItem type
        List<String> itemTypes = Arrays.asList("Assignment", "Exam");
        ArrayAdapter<String> itemTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                itemTypes
        );
        itemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(itemTypeAdapter);

        // Set up Spinner for associated class
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item
        );
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociatedClass.setAdapter(classAdapter);

        // Load classes dynamically for the associated class spinner
        classAdapter.addAll(loadClassesFromPreferences());
    }

    private List<String> loadClassesFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyClasses", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("classes", "");

        if (!json.isEmpty()) {
            // Convert the JSON string to a List<Class>
            List<Class> classList = gson.fromJson(json, new TypeToken<List<Class>>() {}.getType());

            // Extract class names from the list
            List<String> classNamesList = new ArrayList<>();
            for (Class classObj : classList) {
                classNamesList.add(classObj.getClassName());
            }

            return classNamesList;
        } else {
            return new ArrayList<>(); // Return an empty list if no classes are stored yet
        }
    }

    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the TextView with the selected due date
                        String formattedDate = String.format("%02d/%02d/%d", month + 1, dayOfMonth, year);
                        textViewDueDate.setText("Due Date: " + formattedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void submitToDoItem() {
        // Implement the logic to gather user input and store ToDoItem
        // You can use the values from spinnerItemType.getSelectedItem(),
        // editTextTitle.getText().toString(), editTextDetails.getText().toString(),
        // spinnerAssociatedClass.getSelectedItem(), and textViewDueDate.getText().toString()
        // to create and store the ToDoItem.
        // ...

        // Once the ToDoItem is stored, you can use an Intent to pass it back to ToDoFragment
        // Example:
        // Intent resultIntent = new Intent();
        // resultIntent.putExtra("newToDoItem", createdToDoItem);
        // setResult(RESULT_OK, resultIntent);
        // finish();
    }
}

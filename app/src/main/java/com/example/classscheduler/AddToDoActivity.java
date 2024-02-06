package com.example.classscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classscheduler.models.Assignment;
import com.example.classscheduler.models.Exam;
import com.example.classscheduler.models.ToDoItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.example.classscheduler.models.Class;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity for adding a new to-do item (Assignment or Exam).
 */
public class AddToDoActivity extends AppCompatActivity {

    private Spinner spinnerItemType, spinnerAssociatedClass;
    private EditText editTextTitle, editTextDetails;
    private Button btnSubmit, btnCancel, btnOpenDatePicker;
    private TextView textViewDueDate;
    private DatePickerDialog datePickerDialog;
    private String dueDateString; // Added variable to store the actual date string

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

    /**
     * Set up Spinners for item type and associated class.
     */
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

    /**
     * Load class names from SharedPreferences to populate the associated class spinner.
     *
     * @return List of class names.
     */
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

    /**
     * Show the DatePicker dialog.
     */
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

                        // Store the actual date string
                        dueDateString = formattedDate;
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    /**
     * Save an Assignment to SharedPreferences.
     *
     * @param assignment The Assignment to be saved.
     */
    private void saveAssignmentToPreferences(Assignment assignment) {
        // Retrieve existing Assignments from SharedPreferences
        List<Assignment> existingAssignments = loadAssignmentsFromPreferences();

        // Add the new Assignment
        existingAssignments.add(assignment);

        // Save the updated list back to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(existingAssignments);
        editor.putString("assignments", json);
        editor.apply();
    }

    /**
     * Save an Exam to SharedPreferences.
     *
     * @param exam The Exam to be saved.
     */
    private void saveExamToPreferences(Exam exam) {
        // Retrieve existing Exams from SharedPreferences
        List<Exam> existingExams = loadExamsFromPreferences();

        // Add the new Exam
        existingExams.add(exam);

        // Save the updated list back to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(existingExams);
        editor.putString("exams", json);
        editor.apply();
    }

    /**
     * Submit the new to-do item based on user input.
     */
    private void submitToDoItem() {
        // Implement the logic to gather user input and store ToDoItem
        String itemType = spinnerItemType.getSelectedItem().toString();
        String title = editTextTitle.getText().toString();
        String details = editTextDetails.getText().toString();
        String associatedClass = spinnerAssociatedClass.getSelectedItem().toString();
        // Use the stored date string
        String dueDateStr = dueDateString;

        // Convert the formatted date string to a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date dueDate;

        try {
            dueDate = dateFormat.parse(dueDateStr);
        } catch (Exception e) {
            showErrorToast();
            return;
        }

        if (title.length() * itemType.length() * details.length() * associatedClass.length() * dueDateStr.length() == 0) {
            showErrorToast();
            return;
        }

        ToDoItem newToDoItem;

        if ("Assignment".equals(itemType)) {
            newToDoItem = new Assignment(title, details, associatedClass, dueDate, false);
            // Save the new Assignment to SharedPreferences
            saveAssignmentToPreferences((Assignment) newToDoItem);
        } else if ("Exam".equals(itemType)) {
            newToDoItem = new Exam(title, details, associatedClass, dueDate, false);
            // Save the new Exam to SharedPreferences
            saveExamToPreferences((Exam) newToDoItem);
        } else {
            // Handle other types if needed
            return;
        }

        // Close the activity
        finish();
    }

    /**
     * Show an error toast for incomplete input.
     */
    private void showErrorToast() {
        Context context = getApplicationContext();
        CharSequence text = "Please fill all fields!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Load Exams from SharedPreferences.
     *
     * @return List of loaded Exams.
     */
    private List<Exam> loadExamsFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = preferences.getString("exams", "");

        if (!json.isEmpty()) {
            try {
                // Convert the JSON string to a List<Exam>
                return gson.fromJson(json, new TypeToken<List<Exam>>() {}.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
                System.out.println("DEBUG: JsonSyntaxException during exam deserialization.");
            }
        }

        return new ArrayList<>(); // Return an empty list if no exams are stored yet
    }

    /**
     * Load Assignments from SharedPreferences.
     *
     * @return List of loaded Assignments.
     */
    private List<Assignment> loadAssignmentsFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = preferences.getString("assignments", "");

        if (!json.isEmpty()) {
            try {
                // Convert the JSON string to a List<Assignment>
                return gson.fromJson(json, new TypeToken<List<Assignment>>() {}.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
                System.out.println("DEBUG: JsonSyntaxException during assignment deserialization.");
            }
        }

        return new ArrayList<>(); // Return an empty list if no assignments are stored yet
    }
}

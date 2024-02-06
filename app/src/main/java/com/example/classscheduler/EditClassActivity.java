package com.example.classscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.classscheduler.listeners.TimePickerListener;
import com.example.classscheduler.models.Class;
import com.example.classscheduler.ui.TimePickerFragment;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Activity for editing details of an existing class.
 */
public class EditClassActivity extends AppCompatActivity implements TimePickerListener {

    private EditText editTextClassName;
    private EditText editTextInstructor;
    private EditText editTextClassSection;
    private EditText editTextClassLocation;
    private EditText editTextClassTime;

    private CheckBox checkBoxMonday;
    private CheckBox checkBoxTuesday;
    private CheckBox checkBoxWednesday;
    private CheckBox checkBoxThursday;
    private CheckBox checkBoxFriday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        // Initialize views
        editTextClassName = findViewById(R.id.editTextClassName);
        editTextInstructor = findViewById(R.id.editTextInstructor);
        editTextClassSection = findViewById(R.id.editTextClassSection);
        editTextClassLocation = findViewById(R.id.editTextClassLocation);
        editTextClassTime = findViewById(R.id.editTextClassTime);

        checkBoxMonday = findViewById(R.id.checkBoxMonday);
        checkBoxTuesday = findViewById(R.id.checkBoxTuesday);
        checkBoxWednesday = findViewById(R.id.checkBoxWednesday);
        checkBoxThursday = findViewById(R.id.checkBoxThursday);
        checkBoxFriday = findViewById(R.id.checkBoxFriday);

        // Retrieve the Class object from the intent
        Class selectedClass = (Class) getIntent().getSerializableExtra("classDetails");
        int classPosition = getIntent().getIntExtra("classPosition", -1);

        // Check if the Class object is not null
        if (selectedClass != null && classPosition != -1) {
            // Update UI elements with the details of the selected class
            editTextClassName.setText(selectedClass.getClassName());
            editTextInstructor.setText(selectedClass.getInstructor());
            editTextClassSection.setText(selectedClass.getClassSection());
            editTextClassLocation.setText(selectedClass.getClassLocation());
            editTextClassTime.setText(selectedClass.getClassTime());

            // Update the checkboxes based on the selected days
            ArrayList<String> selectedDays = selectedClass.getDaysOfWeek();
            checkBoxMonday.setChecked(selectedDays.contains("Monday"));
            checkBoxTuesday.setChecked(selectedDays.contains("Tuesday"));
            checkBoxWednesday.setChecked(selectedDays.contains("Wednesday"));
            checkBoxThursday.setChecked(selectedDays.contains("Thursday"));
            checkBoxFriday.setChecked(selectedDays.contains("Friday"));
        }

        Button buttonSaveClass = findViewById(R.id.buttonSaveClass);
        buttonSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the logic for saving class details
                saveClassDetails(classPosition);
            }
        });

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#673AB7"));
    }

    /**
     * Method to handle "Cancel" button click.
     *
     * @param v The View that was clicked.
     */
    public void cancel(View v) {
        // Handle the action to perform when the "Cancel" button is clicked
        finish(); // This will close the activity and go back
    }

    /**
     * Method to show the Time Picker dialog.
     *
     * @param v The View that was clicked.
     */
    public void showTimePickerDialog(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment(this);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Callback from TimePickerFragment.
     *
     * @param view      The TimePicker view.
     * @param hourOfDay The selected hour of the day.
     * @param minute    The selected minute.
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Format the selected time and set it to the EditText
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(this);
        String selectedTime = timeFormat.format(calendar.getTime());

        editTextClassTime.setText(selectedTime);
    }

    /**
     * Save the updated class details to SharedPreferences.
     *
     * @param position The position of the class in the list.
     */
    private void saveClassDetails(int position) {
        // Get entered class details
        String className = editTextClassName.getText().toString();
        String instructor = editTextInstructor.getText().toString();
        String classSection = editTextClassSection.getText().toString();
        String classLocation = editTextClassLocation.getText().toString();
        String classTime = editTextClassTime.getText().toString();

        if (className.length() * instructor.length() * classSection.length() * classLocation.length() * classTime.length() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Please fill all fields!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        // Convert the set of selected days to an ArrayList
        ArrayList<String> daysOfWeek = new ArrayList<>();
        if (checkBoxMonday.isChecked()) daysOfWeek.add("Monday");
        if (checkBoxTuesday.isChecked()) daysOfWeek.add("Tuesday");
        if (checkBoxWednesday.isChecked()) daysOfWeek.add("Wednesday");
        if (checkBoxThursday.isChecked()) daysOfWeek.add("Thursday");
        if (checkBoxFriday.isChecked()) daysOfWeek.add("Friday");

        // Create a new Class object with updated details
        Class updatedClass = new Class(className, instructor, classSection, classLocation, classTime, daysOfWeek);

        // Update the existing class in the list
        updateClassInSharedPreferences(position, updatedClass);

        // Finish the activity
        finish();
    }

    /**
     * Update the class at the specified position in SharedPreferences.
     *
     * @param position     The position of the class in the list.
     * @param updatedClass The updated Class object.
     */
    private void updateClassInSharedPreferences(int position, Class updatedClass) {
        // Get the existing classes from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyClasses", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("classes", null);

        // If no classes exist, create a new ArrayList
        ArrayList<Class> classList = new ArrayList<>();
        if (json != null) {
            // If classes exist, retrieve them
            Class[] existingClasses = gson.fromJson(json, Class[].class);
            classList = new ArrayList<>(Arrays.asList(existingClasses));
        }

        // Update the class at the specified position
        classList.set(position, updatedClass);

        // Convert the updated list to JSON
        json = gson.toJson(classList);

        // Save the updated list back to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("classes", json);
        editor.apply();
    }
}

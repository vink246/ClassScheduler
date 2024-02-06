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
import com.example.classscheduler.ui.TimePickerFragment;
import com.example.classscheduler.models.Class;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Activity for adding a new class.
 */
public class AddClassActivity extends AppCompatActivity implements TimePickerListener {

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
        setContentView(R.layout.activity_add_class);

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

        Button buttonSaveClass = findViewById(R.id.buttonSaveClass);
        buttonSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the logic for saving class details
                saveClassDetails();
            }
        });

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#673AB7"));
    }

    /**
     * Handles "Cancel" button click.
     *
     * @param v The view that was clicked.
     */
    public void cancel(View v) {
        // Handle the action to perform when the "Cancel" button is clicked
        finish(); // This will close the activity and go back
    }

    /**
     * Shows the Time Picker dialog.
     *
     * @param v The view that was clicked.
     */
    public void showTimePickerDialog(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment(this);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Callback from TimePickerFragment.
     *
     * @param view      The TimePicker view.
     * @param hourOfDay The selected hour.
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
     * Saves the class details to SharedPreferences.
     */
    private void saveClassDetails() {
        // Get entered class details
        String className = editTextClassName.getText().toString();
        String instructor = editTextInstructor.getText().toString();
        String classSection = editTextClassSection.getText().toString();
        String classLocation = editTextClassLocation.getText().toString();
        String classTime = editTextClassTime.getText().toString();

        // Convert the set of selected days to an ArrayList
        ArrayList<String> daysOfWeek = new ArrayList<>();
        if (checkBoxMonday.isChecked()) daysOfWeek.add("Monday");
        if (checkBoxTuesday.isChecked()) daysOfWeek.add("Tuesday");
        if (checkBoxWednesday.isChecked()) daysOfWeek.add("Wednesday");
        if (checkBoxThursday.isChecked()) daysOfWeek.add("Thursday");
        if (checkBoxFriday.isChecked()) daysOfWeek.add("Friday");

        // Validate input fields
        if (className.length() * instructor.length() * classSection.length() * classLocation.length() * classTime.length() * daysOfWeek.size() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Please fill all fields!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        // Create a Class object
        Class newClass = new Class(className, instructor, classSection, classLocation, classTime, daysOfWeek);

        // Save the new class using SharedPreferences
        saveClassToSharedPreferences(newClass);

        // For now, you can finish the activity
        finish();
    }

    /**
     * Saves the class to SharedPreferences.
     *
     * @param newClass The new class to be saved.
     */
    private void saveClassToSharedPreferences(Class newClass) {
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

        // Add the new class
        classList.add(newClass);

        // Convert the updated list to JSON
        json = gson.toJson(classList);

        // Save the updated list back to SharedPreferences
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("classes", json);
        editor.apply();
    }
}

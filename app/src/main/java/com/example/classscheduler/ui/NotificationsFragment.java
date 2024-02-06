package com.example.classscheduler.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.fragment.app.Fragment;
import com.example.classscheduler.R;
import java.util.Calendar;
import java.util.Date;

/**
 * A fragment for managing notifications settings.
 */
public class NotificationsFragment extends Fragment {

    private Switch classNotificationSwitch;
    private Switch assignmentNotificationSwitch;
    private Switch examNotificationSwitch;

    private Spinner classNotificationSpinner;
    private Spinner assignmentNotificationSpinner;
    private Spinner examNotificationSpinner;

    private SharedPreferences classPreferences;
    private SharedPreferences assignmentsAndExamsPreferences;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        classNotificationSwitch = view.findViewById(R.id.switchClassNotifications);
        assignmentNotificationSwitch = view.findViewById(R.id.switchAssignmentNotifications);
        examNotificationSwitch = view.findViewById(R.id.switchExamNotifications);

        classNotificationSpinner = view.findViewById(R.id.spinnerClassNotification);
        assignmentNotificationSpinner = view.findViewById(R.id.spinnerAssignmentNotification);
        examNotificationSpinner = view.findViewById(R.id.spinnerExamNotification);

        classPreferences = requireContext().getSharedPreferences("MyClasses", Context.MODE_PRIVATE);
        assignmentsAndExamsPreferences = requireContext().getSharedPreferences("MyToDoItems", Context.MODE_PRIVATE);

        setupSwitches();
        setupSpinners();

        return view;
    }

    /**
     * Set up switch listeners.
     */
    private void setupSwitches() {
        classNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleNotificationSwitchChange(isChecked, classNotificationSpinner, classPreferences);
            }
        });

        assignmentNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleNotificationSwitchChange(isChecked, assignmentNotificationSpinner, assignmentsAndExamsPreferences);
            }
        });

        examNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                handleNotificationSwitchChange(isChecked, examNotificationSpinner, assignmentsAndExamsPreferences);
            }
        });
    }

    /**
     * Set up spinner adapters.
     */
    private void setupSpinners() {
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.notification_times,
                android.R.layout.simple_spinner_item
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classNotificationSpinner.setAdapter(timeAdapter);
        assignmentNotificationSpinner.setAdapter(timeAdapter);
        examNotificationSpinner.setAdapter(timeAdapter);
    }

    /**
     * Handle notification switch change.
     *
     * @param isChecked  Whether the switch is checked.
     * @param spinner    The associated spinner.
     * @param preferences The SharedPreferences for the switch.
     */
    private void handleNotificationSwitchChange(boolean isChecked, Spinner spinner, SharedPreferences preferences) {
        if (isChecked) {
            int selectedTime = spinner.getSelectedItemPosition();
            scheduleNotification(selectedTime, preferences);
        } else {
            cancelNotification(preferences);
        }
    }

    /**
     * Schedule a notification based on the selected time.
     *
     * @param selectedTime The selected time index.
     * @param preferences  The SharedPreferences for the switch.
     */
    private void scheduleNotification(int selectedTime, SharedPreferences preferences) {
        long dueDateTimestamp = preferences.getLong("due_date", 0);
        Date dueDate = new Date(dueDateTimestamp);

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.setTime(dueDate);

        switch (selectedTime) {
            case 0:
                notificationTime.add(Calendar.MINUTE, -5);
                break;
            case 1:
                notificationTime.add(Calendar.MINUTE, -15);
                break;
            case 2:
                notificationTime.add(Calendar.MINUTE, -30);
                break;
            case 3:
                notificationTime.add(Calendar.HOUR_OF_DAY, -1);
                break;
            case 4:
                notificationTime.add(Calendar.HOUR_OF_DAY, -2);
                break;
            case 5:
                notificationTime.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case 6:
                notificationTime.add(Calendar.DAY_OF_YEAR, -2);
                break;
            // Add more cases as needed
        }

        // Implement the logic to schedule the notification using 'notificationTime'
        // Save the selected time in SharedPreferences if needed
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notification_time", selectedTime);
        editor.apply();
    }

    /**
     * Cancel a scheduled notification.
     *
     * @param preferences The SharedPreferences for the switch.
     */
    private void cancelNotification(SharedPreferences preferences) {
        // Implement the logic to cancel or remove the scheduled notification
        // Remove the saved notification time from SharedPreferences if needed
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("notification_time");
        editor.apply();
    }
}

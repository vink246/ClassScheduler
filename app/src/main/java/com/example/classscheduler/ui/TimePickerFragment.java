package com.example.classscheduler.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.classscheduler.listeners.TimePickerListener;

import java.util.Calendar;

/**
 * A DialogFragment for displaying a time picker dialog.
 * Implements the TimePickerDialog.OnTimeSetListener to handle time selection.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerListener timePickerListener;

    /**
     * Constructs a TimePickerFragment with the given TimePickerListener.
     *
     * @param timePickerListener The listener for time selection.
     */
    public TimePickerFragment(TimePickerListener timePickerListener) {
        this.timePickerListener = timePickerListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(
                requireActivity(),
                this,
                hour,
                minute,
                DateFormat.is24HourFormat(requireContext())
        );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Notify the hosting activity (either AddClassActivity or EditClassActivity)
        if (timePickerListener != null) {
            timePickerListener.onTimeSet(view, hourOfDay, minute);
        }
    }
}

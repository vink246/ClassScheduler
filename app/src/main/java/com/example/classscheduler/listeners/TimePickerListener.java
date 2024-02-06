package com.example.classscheduler.listeners;

import android.widget.TimePicker;

/**
 * Interface for handling time selection events from a TimePicker.
 */
public interface TimePickerListener {
    /**
     * Called when a time is set in the TimePicker.
     *
     * @param view      The TimePicker view.
     * @param hourOfDay The selected hour of the day (in 24-hour format).
     * @param minute    The selected minute.
     */
    void onTimeSet(TimePicker view, int hourOfDay, int minute);
}

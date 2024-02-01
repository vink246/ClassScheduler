package com.example.classscheduler.listeners;

import android.widget.TimePicker;

public interface TimePickerListener {
    void onTimeSet(TimePicker view, int hourOfDay, int minute);
}
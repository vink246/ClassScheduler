package com.example.classscheduler.models;

import java.util.Date;

public interface ToDoItem {
    // Getters
    String getTitle();
    String getDetails();
    String getAssociatedClass();
    Date getDueDate();
    boolean isCompleted();

    // Setters
    void setTitle(String title);
    void setDetails(String details);
    void setDueDate(Date dueDate);
    void setCompleted(boolean completed);
    void setAssociatedClass(String associatedClass);
}
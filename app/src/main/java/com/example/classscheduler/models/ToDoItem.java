package com.example.classscheduler.models;

import java.io.Serializable;
import java.util.Date;

public interface ToDoItem extends Serializable {
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

    String getType();
}
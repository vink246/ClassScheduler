package com.example.classscheduler.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents an assignment as a ToDoItem.
 */
public class Assignment implements ToDoItem, Serializable {

    private String title;
    private String details;
    private String associatedClass;
    private Date dueDate;
    private boolean completed;

    // Default constructor
    public Assignment() {
    }

    /**
     * Constructs an Assignment with specified details.
     *
     * @param title           The title of the assignment.
     * @param details         Additional details of the assignment.
     * @param associatedClass The class associated with the assignment.
     * @param dueDate         The due date of the assignment.
     * @param completed       Indicates whether the assignment is completed.
     */
    public Assignment(String title, String details, String associatedClass, Date dueDate, boolean completed) {
        this.title = title;
        this.details = details;
        this.associatedClass = associatedClass;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getAssociatedClass() {
        return associatedClass;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public void setAssociatedClass(String associatedClass) {
        this.associatedClass = associatedClass;
    }

    @Override
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String getType() {
        return "Assignment";
    }
}

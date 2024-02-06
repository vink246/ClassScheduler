package com.example.classscheduler.models;

import java.io.Serializable;
import java.util.Date;

/**
 * An interface representing a to-do item that is Serializable.
 */
public interface ToDoItem extends Serializable {

    // Getters

    /**
     * Gets the title of the to-do item.
     *
     * @return The title of the to-do item.
     */
    String getTitle();

    /**
     * Gets the details or description of the to-do item.
     *
     * @return Details or description of the to-do item.
     */
    String getDetails();

    /**
     * Gets the associated class for the to-do item.
     *
     * @return The associated class for the to-do item.
     */
    String getAssociatedClass();

    /**
     * Gets the due date of the to-do item.
     *
     * @return The due date of the to-do item.
     */
    Date getDueDate();

    /**
     * Checks if the to-do item is completed.
     *
     * @return True if the to-do item is completed, false otherwise.
     */
    boolean isCompleted();

    // Setters

    /**
     * Sets the title of the to-do item.
     *
     * @param title The title to be set.
     */
    void setTitle(String title);

    /**
     * Sets the details or description of the to-do item.
     *
     * @param details The details to be set.
     */
    void setDetails(String details);

    /**
     * Sets the due date of the to-do item.
     *
     * @param dueDate The due date to be set.
     */
    void setDueDate(Date dueDate);

    /**
     * Sets the completion status of the to-do item.
     *
     * @param completed True if the to-do item is completed, false otherwise.
     */
    void setCompleted(boolean completed);

    /**
     * Sets the associated class for the to-do item.
     *
     * @param associatedClass The associated class to be set.
     */
    void setAssociatedClass(String associatedClass);

    /**
     * Gets the type of the to-do item.
     *
     * @return The type of the to-do item.
     */
    String getType();
}

package com.example.classscheduler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classscheduler.EditToDoActivity;
import com.example.classscheduler.R;
import com.example.classscheduler.adapters.ClassAdapter;
import com.example.classscheduler.adapters.ToDoAdapter;
import com.example.classscheduler.models.Assignment;
import com.example.classscheduler.models.Exam;
import com.example.classscheduler.models.ToDoItem;
import com.example.classscheduler.models.Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ToDoFragment extends Fragment implements ToDoAdapter.ToDoItemListener {

    private Spinner spinnerCompletionFilter;
    private Spinner spinnerTypeFilter;
    private Spinner spinnerClassFilter;
    private RecyclerView recyclerViewToDo;
    private ToDoAdapter toDoAdapter;
    private ArrayList<ToDoItem> toDoList = new ArrayList<>();
    private int tempCompletion, tempType, tempClass = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        spinnerCompletionFilter = view.findViewById(R.id.spinnerCompletionFilter);
        spinnerTypeFilter = view.findViewById(R.id.spinnerTypeFilter);
        spinnerClassFilter = view.findViewById(R.id.spinnerClassFilter);
        recyclerViewToDo = view.findViewById(R.id.recyclerViewToDo);
        recyclerViewToDo.addItemDecoration(new ClassAdapter.DividerItemDecoration(requireContext()));

        // Initialize RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewToDo.setLayoutManager(layoutManager);

        // Initialize and set up the Spinners
        setUpFilterSpinners();

        // Set the default selection for the completion filter
        spinnerCompletionFilter.setSelection(getIndex(spinnerCompletionFilter, "Unfinished"));

        // Initialize FAB
        FloatingActionButton fabAddToDo = view.findViewById(R.id.fabAddToDo);

        // Set up FAB click listener
        fabAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AddToDoActivity when FAB is clicked
                Navigation.findNavController(view).navigate(R.id.action_toDoFragment_to_addToDoActivity);
            }
        });

        // Initialize ToDoAdapter and set it to RecyclerView
        toDoAdapter = new ToDoAdapter(getContext(), loadFilteredToDoItems(), this);
        recyclerViewToDo.setAdapter(toDoAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load toDoItems from SharedPreferences
        toDoList = new ArrayList<>(loadFilteredToDoItems());

        // Update RecyclerView
        toDoAdapter.setToDoList(toDoList);
        toDoAdapter.notifyDataSetChanged();
    }

    private void setUpFilterSpinners() {
        // Set up completion filter
        String[] completionFilterOptions = {"All", "Completed", "Unfinished"};
        ArrayAdapter<String> completionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, completionFilterOptions);
        completionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompletionFilter.setAdapter(completionAdapter);
        spinnerCompletionFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateToDoList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Set up type filter
        String[] typeFilterOptions = {"All", "Assignment", "Exam"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, typeFilterOptions);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeFilter.setAdapter(typeAdapter);
        spinnerTypeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateToDoList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Load associated classes from SharedPreferences
        List<String> classesList = loadClassesFromPreferences();
        classesList.add(0, "All"); // Add an option for filtering all classes
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, classesList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClassFilter.setAdapter(classAdapter);
        spinnerClassFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateToDoList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private List<String> loadClassesFromPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("MyClasses", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("classes", "");

        if (!json.isEmpty()) {
            try {
                // Convert the JSON string to a List<Class>
                List<Class> classList = gson.fromJson(json, new TypeToken<List<Class>>() {}.getType());

                // Extract class names from the list
                List<String> classNamesList = new ArrayList<>();
                for (Class classObj : classList) {
                    classNamesList.add(classObj.getClassName());
                }

                return classNamesList;
            } catch (JsonSyntaxException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
            }
        }
        return new ArrayList<>(); // Return an empty list if no classes are stored yet
    }

    private void updateToDoList() {
        // Load ToDoItems based on selected filters
        List<ToDoItem> filteredToDoItems = loadFilteredToDoItems();

        toDoList = new ArrayList<>(filteredToDoItems);

        // Update the ToDoAdapter with the filtered list
        toDoAdapter.setToDoList(filteredToDoItems);
        toDoAdapter.notifyDataSetChanged();
    }

    private List<Exam> loadExamsFromPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = preferences.getString("exams", "");

        if (!json.isEmpty()) {
            try {
                // Convert the JSON string to a List<Exam>
                return gson.fromJson(json, new TypeToken<List<Exam>>() {}.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
                System.out.println("DEBUG: JsonSyntaxException during exam deserialization.");
            }
        }

        return new ArrayList<>(); // Return an empty list if no exams are stored yet
    }

    private List<Assignment> loadAssignmentsFromPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = preferences.getString("assignments", "");

        if (!json.isEmpty()) {
            try {
                // Convert the JSON string to a List<Assignment>
                return gson.fromJson(json, new TypeToken<List<Assignment>>() {}.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace(); // Log or handle the exception appropriately
                System.out.println("DEBUG: JsonSyntaxException during assignment deserialization.");
            }
        }

        return new ArrayList<>(); // Return an empty list if no assignments are stored yet
    }

    private List<ToDoItem> loadFilteredToDoItems() {
        // Load all Exams and Assignments from SharedPreferences
        List<Exam> allExams = loadExamsFromPreferences();
        List<Assignment> allAssignments = loadAssignmentsFromPreferences();

        // Apply filters based on spinner selections
        String completionFilter = spinnerCompletionFilter.getSelectedItem().toString();
        String typeFilter = spinnerTypeFilter.getSelectedItem().toString();
        String classFilter = spinnerClassFilter.getSelectedItem().toString();

        List<ToDoItem> filteredToDoItems = new ArrayList<>();

        // Add exams to filteredToDoItems based on filters
        for (Exam exam : allExams) {
            if (("All".equals(completionFilter) || (exam.isCompleted() && "Completed".equals(completionFilter)) ||
                    (!exam.isCompleted() && "Unfinished".equals(completionFilter))) &&
                    ("All".equals(typeFilter) || "Exam".equals(typeFilter)) &&
                    ("All".equals(classFilter) || exam.getAssociatedClass().equals(classFilter))) {
                filteredToDoItems.add(exam);
            }
        }

        // Add assignments to filteredToDoItems based on filters
        for (Assignment assignment : allAssignments) {
            if (("All".equals(completionFilter) || (assignment.isCompleted() && "Completed".equals(completionFilter)) ||
                    (!assignment.isCompleted() && "Unfinished".equals(completionFilter))) &&
                    ("All".equals(typeFilter) || "Assignment".equals(typeFilter)) &&
                    ("All".equals(classFilter) || assignment.getAssociatedClass().equals(classFilter))) {
                filteredToDoItems.add(assignment);
            }
        }

        return filteredToDoItems;
    }

    // Helper method to get the index of an item in a Spinner
    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }

    // Implement the ToDoAdapter.ToDoItemListener methods
    @Override
    public void onDeleteClicked(int position, String type) {
        // Handle the delete action
        showDeleteConfirmationDialog(position, type);
    }

    @Override
    public void onEditClicked(int position) {
        // Handle the edit action
        editToDoItem(position);
    }

    @Override
    public void onMarkFinishedClicked(int position) {
        // Handle the mark finished action
        markFinishedToDoItem(position);
    }

    private void deleteToDoItem(int position) {
        saveCurrentState();
        // Handle the action to delete the class at the given position
        toDoList.remove(position);
        // Update SharedPreferences after removing the class
        saveToDoItemsToPreferences();
        toDoAdapter.notifyDataSetChanged();
        restoreState();
    }

    private void editToDoItem(int position) {
        saveCurrentState();
        ToDoItem selectedItem = toDoList.get(position);

        // Create an Intent to launch the EditToDoActivity
        Intent intent = new Intent(getContext(), EditToDoActivity.class);

        // Pass the details of the selected class and its position to the EditToDoActivity
        intent.putExtra("toDoDetails", selectedItem);
        intent.putExtra("toDoPosition", position);
        intent.putExtra("toDoList", (Serializable) toDoList);

        // Start the EditClassActivity
        getContext().startActivity(intent);
        restoreState();
    }

    private void markFinishedToDoItem(int position) {
        saveCurrentState();
        toDoList.get(position).setCompleted(true);
        saveToDoItemsToPreferences();
        restoreState();
    }

    private void saveToDoItemsToPreferences() {
        ArrayList<Exam> exams = new ArrayList<>();
        ArrayList<Assignment> assignments = new ArrayList<>();
        for (ToDoItem item: toDoList) {
            if (item.getType().equals("Exam")) {
                exams.add((Exam) item);
            } else {
                assignments.add((Assignment) item);
            }
        }

        SharedPreferences preferences = getContext().getSharedPreferences("MyToDoItems", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String examJson = gson.toJson(exams);
        String assignmentJson = gson.toJson(assignments);
        editor.putString("exams", examJson);
        editor.putString("assignments", assignmentJson);
        editor.apply();
    }

    private void showDeleteConfirmationDialog(int position, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(String.format("Are you sure you want to delete this %s?", type))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes, proceed with deletion
                        deleteToDoItem(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No, do nothing
                    }
                });
        builder.create().show();
    }

    private void saveCurrentState() {
        tempClass = spinnerClassFilter.getSelectedItemPosition();
        tempCompletion = spinnerCompletionFilter.getSelectedItemPosition();
        tempType = spinnerTypeFilter.getSelectedItemPosition();
        spinnerClassFilter.setSelection(0);
        spinnerCompletionFilter.setSelection(0);
        spinnerTypeFilter.setSelection(0);
        updateToDoList();
    }

    private void restoreState() {
        spinnerClassFilter.setSelection(tempClass);
        spinnerCompletionFilter.setSelection(tempCompletion);
        spinnerTypeFilter.setSelection(tempType);
        updateToDoList();
    }
}


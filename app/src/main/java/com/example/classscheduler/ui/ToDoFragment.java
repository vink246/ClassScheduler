package com.example.classscheduler.ui;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classscheduler.R;
import com.example.classscheduler.adapters.ClassAdapter;
import com.example.classscheduler.adapters.ToDoAdapter;
import com.example.classscheduler.models.Assignment;
import com.example.classscheduler.models.Exam;
import com.example.classscheduler.models.ToDoItem;
import com.example.classscheduler.models.Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class ToDoFragment extends Fragment {

    private Spinner spinnerCompletionFilter;
    private Spinner spinnerTypeFilter;
    private Spinner spinnerClassFilter;
    private RecyclerView recyclerViewToDo;
    private ToDoAdapter toDoAdapter;

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

        // Initialize ToDoAdapter and set it to RecyclerView
        toDoAdapter = new ToDoAdapter(getContext(), getSampleToDoList());
        recyclerViewToDo.setAdapter(toDoAdapter);

        // Initialize and set up the Spinners
        setUpFilterSpinners();

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

        return view;
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
            // Convert the JSON string to a List<Class>
            List<Class> classList = gson.fromJson(json, new TypeToken<List<Class>>() {}.getType());

            // Extract class names from the list
            List<String> classNamesList = new ArrayList<>();
            for (Class classObj : classList) {
                classNamesList.add(classObj.getClassName());
            }

            return classNamesList;
        } else {
            return new ArrayList<>(); // Return an empty list if no classes are stored yet
        }
    }

    private List<ToDoItem> getSampleToDoList() {
        // TODO: Replace this with your actual data retrieval logic
        List<ToDoItem> toDoList = new ArrayList<>();

        // Add sample ToDo items
        toDoList.add(new Assignment("Assignment 1", "Details 1", "Class A", null, false));
        toDoList.add(new Exam("Exam 1", "Details 2", "Class B", null, true));
        // Add more items as needed

        return toDoList;
    }

    private void updateToDoList() {
        // TODO: Update ToDoAdapter based on the selected filters
        // You may want to filter the ToDo items list and update the adapter accordingly
        // Use spinnerCompletionFilter.getSelectedItem().toString() to get the selected completion filter
        // Use spinnerTypeFilter.getSelectedItem().toString() to get the selected type filter
        // Use spinnerClassFilter.getSelectedItem().toString() to get the selected class filter
    }
}

package com.example.classscheduler.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.classscheduler.AddClassActivity;
import com.example.classscheduler.R;
import com.example.classscheduler.adapters.ClassAdapter;
import com.example.classscheduler.models.Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassesFragment} factory method to
 * create an instance of this fragment.
 */
public class ClassesFragment extends Fragment {

    private ArrayList<Class> classList = new ArrayList<>();
    private ClassAdapter classAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classes, container, false);

        // Set up RecyclerView
        RecyclerView recyclerViewClasses = rootView.findViewById(R.id.recyclerViewClasses);
        recyclerViewClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        classAdapter = new ClassAdapter(requireContext(), classList);
        recyclerViewClasses.setAdapter(classAdapter);
        recyclerViewClasses.addItemDecoration(new ClassAdapter.DividerItemDecoration(requireContext()));

        // Load classes from SharedPreferences
        loadClassesFromSharedPreferences();

        // Find the Floating Action Button (FAB)
        FloatingActionButton fabAddClass = rootView.findViewById(R.id.fabAddClass);

        // Set OnClickListener for the FAB
        fabAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle FAB click, for example, navigate to a new fragment
                navigateToAddClassFragment();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load classes from SharedPreferences
        loadClassesFromSharedPreferences();

        // Update RecyclerView
        classAdapter.setClassList(classList);
        classAdapter.notifyDataSetChanged();
    }

    // Custom method to navigate to a new fragment for adding a class
    private void navigateToAddClassFragment() {
        // Use NavController to navigate to a new fragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_classesFragment_to_addClassActivity);
    }


    private void loadClassesFromSharedPreferences() {
        // Load existing classes from SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyClasses", requireActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("classes", null);

        // If classes exist, retrieve them
        if (json != null) {
            Class[] existingClasses = gson.fromJson(json, Class[].class);
            classList = new ArrayList<>(Arrays.asList(existingClasses));
            // Update the adapter dataset
            classAdapter.setClassList(classList);
            classAdapter.notifyDataSetChanged();
        }
    }
}

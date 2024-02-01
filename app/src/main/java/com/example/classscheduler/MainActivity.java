package com.example.classscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Use NavHostFragment.findNavController() to get the NavController
        NavController navController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));

        // Set up Action bar with a custom AppBarConfiguration
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_classes,
                R.id.nav_add_class,
                R.id.nav_todo,
                R.id.nav_notifications
        ).build();

        // Set up Action bar with the custom AppBarConfiguration
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Setup BottomNavigationView with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        getWindow().setStatusBarColor(android.graphics.Color.parseColor("#673AB7"));
    }

    // Override onSupportNavigateUp to handle Up navigation with the bottom navigation
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }
}

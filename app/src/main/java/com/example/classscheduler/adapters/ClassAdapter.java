package com.example.classscheduler.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classscheduler.EditClassActivity;
import com.example.classscheduler.R;
import com.example.classscheduler.models.Class;
import com.google.gson.Gson;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<Class> classList;
    private Context context;

    public ClassAdapter(Context context, List<Class> classList) {
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class currentClass = classList.get(position);

        // Bind data to the ViewHolder
        holder.textViewClassName.setText("Class: " + currentClass.getClassName());
        holder.textViewInstructor.setText("Instructor: " + currentClass.getInstructor());
        holder.textViewSection.setText("Section: " + currentClass.getClassSection());
        holder.textViewClassLocation.setText("Location: " + currentClass.getClassLocation());
        holder.textViewClassTime.setText("Time: " + currentClass.getClassTime());
        holder.textViewDaysOfWeek.setText("Days: " + currentClass.getDaysOfWeekAsString());

        holder.imageViewDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the delete button click using getAdapterPosition()
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(adapterPosition);
                }
            }
        });

        holder.imageViewEditClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the edit button click using getAdapterPosition()
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    editClass(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setClassList(List<Class> updatedList) {
        // Update the dataset and notify the adapter
        classList = updatedList;
        notifyDataSetChanged();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClassName;
        TextView textViewInstructor;
        TextView textViewSection;
        TextView textViewClassLocation;
        TextView textViewClassTime;
        TextView textViewDaysOfWeek;
        ImageView imageViewDeleteClass;
        ImageView imageViewEditClass;
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClassName = itemView.findViewById(R.id.textViewClassName);
            textViewInstructor = itemView.findViewById(R.id.textViewInstructor);
            textViewSection = itemView.findViewById(R.id.textViewSection);
            textViewClassLocation = itemView.findViewById(R.id.textViewClassLocation);
            textViewClassTime = itemView.findViewById(R.id.textViewClassTime);
            textViewDaysOfWeek = itemView.findViewById(R.id.textViewDaysOfWeek);
            imageViewDeleteClass = itemView.findViewById(R.id.imageViewDeleteClass);
            imageViewEditClass = itemView.findViewById(R.id.imageViewEditClass);
        }
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable divider;

        public DividerItemDecoration(Context context) {
            divider = ContextCompat.getDrawable(context, R.drawable.divider);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    // Method to handle delete button click
    private void deleteClass(int position) {
        // Handle the action to delete the class at the given position
        classList.remove(position);
        // Update SharedPreferences after removing the class
        saveClassesToSharedPreferences();
        notifyDataSetChanged();
    }

    private void editClass(int position) {
        Class selectedClass = classList.get(position);

        // Create an Intent to launch the EditClassActivity
        Intent intent = new Intent(context, EditClassActivity.class);

        // Pass the details of the selected class and its position to the EditClassActivity
        intent.putExtra("classDetails", selectedClass);
        intent.putExtra("classPosition", position);

        // Start the EditClassActivity
        context.startActivity(intent);
    }

    private void saveClassesToSharedPreferences() {
        // Convert the updated list to JSON
        Gson gson = new Gson();
        String json = gson.toJson(classList);

        // Save the updated list back to SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("MyClasses", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("classes", json);
        editor.apply();
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this class?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes, proceed with deletion
                        deleteClass(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No, do nothing
                    }
                });
        builder.create().show();
    }

}
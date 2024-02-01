package com.example.classscheduler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classscheduler.R;
import com.example.classscheduler.models.Assignment;
import com.example.classscheduler.models.Exam;
import com.example.classscheduler.models.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDoItem> toDoList;
    private Context context;

    // Add any additional properties or methods as needed

    public ToDoAdapter(Context context, List<ToDoItem> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoItem currentItem = toDoList.get(position);

        // Bind data to the ViewHolder
        holder.textViewTitle.setText(currentItem.getTitle());
        holder.textViewDueDate.setText("Due Date: " + currentItem.getDueDate());

        String itemType = currentItem.getClass().getSimpleName();
        holder.textViewType.setText("Type: " + itemType);

        // Handle different types of ToDoItems
        if (currentItem instanceof Assignment) {
            // Handle assignment-specific logic
            // For example, you can cast it to Assignment and get assignment-specific properties
            Assignment assignment = (Assignment) currentItem;
            // Perform actions specific to assignments
        } else if (currentItem instanceof Exam) {
            // Handle exam-specific logic
            // For example, you can cast it to Exam and get exam-specific properties
            Exam exam = (Exam) currentItem;
            // Perform actions specific to exams
        }
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void setToDoList(List<ToDoItem> updatedList) {
        // Update the dataset and notify the adapter
        toDoList = updatedList;
        notifyDataSetChanged();
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDueDate;
        TextView textViewType;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewType = itemView.findViewById(R.id.textViewType);
        }
    }
}

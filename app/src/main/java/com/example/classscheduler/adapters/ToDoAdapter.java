package com.example.classscheduler.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classscheduler.R;
import com.example.classscheduler.models.Assignment;
import com.example.classscheduler.models.Exam;
import com.example.classscheduler.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying ToDoItems in a RecyclerView.
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDoItem> toDoList;
    private Context context;
    private ToDoItemListener listener; // New listener variable

    /**
     * Constructor for ToDoAdapter.
     *
     * @param context  The context in which the adapter is created.
     * @param toDoList The list of ToDoItems to be displayed.
     * @param listener The listener for handling actions on ToDoItems.
     */
    public ToDoAdapter(Context context, List<ToDoItem> toDoList, ToDoItemListener listener) {
        this.context = context;
        this.toDoList = toDoList;
        this.listener = listener;
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

        // Check if currentItem is not null before accessing its properties
        if (currentItem != null) {
            holder.textViewTitle.setText(currentItem.getTitle());

            String itemType = currentItem.getClass().getSimpleName();
            holder.textViewType.setText("Type: " + itemType);

            if (itemType.equals("Exam")) {
                // If the item is an exam, use "On" instead of "Due Date"
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String formattedDueDate = dateFormat.format(currentItem.getDueDate());
                holder.textViewDueDate.setText("On: " + formattedDueDate);
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String formattedDueDate = dateFormat.format(currentItem.getDueDate());
                holder.textViewDueDate.setText("Due Date: " + formattedDueDate);
            }

            if (currentItem.isCompleted()) {
                holder.imageViewMarkFinished.setVisibility(View.GONE);
                holder.textViewTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.imageViewMarkFinished.setVisibility(View.VISIBLE);
                holder.textViewTitle.setPaintFlags(0);
            }

            // Additional information for each ToDoItem type
            if (currentItem instanceof Assignment) {
                Assignment assignment = (Assignment) currentItem;
                holder.textViewAssociatedClass.setText("Class: " + assignment.getAssociatedClass());
                holder.textViewDetails.setText("Details: " + assignment.getDetails());
            } else if (currentItem instanceof Exam) {
                Exam exam = (Exam) currentItem;
                holder.textViewAssociatedClass.setText("Class: " + exam.getAssociatedClass());
                holder.textViewDetails.setText("Details: " + exam.getDetails());
            }
        } else {
            // Handle the case where currentItem is null (optional)
            holder.textViewTitle.setText("N/A");
            holder.textViewType.setText("Type: N/A");
            holder.textViewDueDate.setText("Due Date: N/A");
            holder.textViewAssociatedClass.setText("Class: N/A");
            holder.textViewDetails.setText("Details: N/A");
        }

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the listener to handle the delete action
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDeleteClicked(adapterPosition, holder.textViewType.getText().toString());
                }
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the listener to handle the edit action
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onEditClicked(adapterPosition);
                }
            }
        });

        holder.imageViewMarkFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the listener to handle the mark finished action
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMarkFinishedClicked(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    /**
     * Sets the updated list of ToDoItems and notifies the adapter.
     *
     * @param updatedList The updated list of ToDoItems.
     */
    public void setToDoList(List<ToDoItem> updatedList) {
        // Update the dataset and notify the adapter
        toDoList = updatedList;

        notifyDataSetChanged();
    }

    /**
     * Interface for handling actions on ToDoItems.
     */
    public interface ToDoItemListener {
        void onDeleteClicked(int position, String type);

        void onEditClicked(int position);

        void onMarkFinishedClicked(int position);
    }

    /**
     * ViewHolder for holding views of individual ToDoItems in the RecyclerView.
     */
    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDueDate;
        TextView textViewType;
        TextView textViewAssociatedClass;
        TextView textViewDetails;
        ImageView imageViewDelete;
        ImageView imageViewEdit;
        ImageView imageViewMarkFinished;

        /**
         * Constructor for ToDoViewHolder.
         *
         * @param itemView The view representing an individual ToDoItem.
         */
        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewAssociatedClass = itemView.findViewById(R.id.textViewAssociatedClass);
            textViewDetails = itemView.findViewById(R.id.textViewDetails);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewMarkFinished = itemView.findViewById(R.id.imageViewMarkFinished);
        }
    }
}

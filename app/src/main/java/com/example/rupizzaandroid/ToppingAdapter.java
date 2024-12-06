package com.example.rupizzaandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {
    private final OnToppingImageChangeListener imageListener;
    private List<Topping> toppings;
    private OnToppingClickListener listener;

    public interface OnToppingClickListener {
        void onToppingClick(Topping topping);
    }

    public interface OnToppingImageChangeListener {
        void onToppingImageChange(int imageResId);
    }

    public ToppingAdapter(List<Topping> toppings, OnToppingClickListener listener, OnToppingImageChangeListener imageListener) {
        this.toppings = new ArrayList<>(toppings);
        this.listener = listener;
        this.imageListener = imageListener;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the updated layout with both ImageView and TextView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topping, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppings.get(position);

        // Bind topping name to the TextView
        holder.textView.setText(topping.toString());

        // Bind topping image to the ImageView
        holder.imageView.setImageResource(topping.getImageResId());

        // Highlight the background color if selected
        if (topping.isSelected()) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_blue_light)); // Highlight color
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.transparent)); // Default color
        }

        // Notify the image listener and handle item clicks
        holder.itemView.setOnClickListener(v -> {
            listener.onToppingClick(topping);

            if (topping.isSelected()) {
                imageListener.onToppingImageChange(topping.getImageResId()); // Show topping image
            } else {
                // Check if all toppings are deselected
                boolean allDeselected = true;
                for (Topping t : toppings) {
                    if (t.isSelected()) {
                        allDeselected = false;
                        break;
                    }
                }
                if (allDeselected) {
                    imageListener.onToppingImageChange(R.drawable.buildyourown); // Default image
                }
            }
            notifyItemChanged(position); // Update view for selection state
        });
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    public void addTopping(Topping topping) {
        toppings.add(topping);
        notifyItemInserted(toppings.size() - 1);
    }

    public void removeTopping(Topping topping) {
        int index = toppings.indexOf(topping);
        if (index != -1) {
            toppings.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearToppings() {
        toppings.clear();
        notifyDataSetChanged();
    }

    public void addAllToppings(List<Topping> newToppings) {
        int startPosition = toppings.size();
        toppings.addAll(newToppings);
        notifyItemRangeInserted(startPosition, newToppings.size());
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView; // New ImageView for topping image

        ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.toppingTextView); // Use updated layout ID
            imageView = itemView.findViewById(R.id.imageView); // Use updated layout ID
        }
    }
}

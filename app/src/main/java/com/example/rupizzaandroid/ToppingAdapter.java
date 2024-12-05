package com.example.rupizzaandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {
    private final OnToppingImageChangeListener imageListener;
    private List<Topping> toppings;
    private OnToppingClickListener listener;
    private boolean isBuildYourOwn;

    public interface OnToppingClickListener {
        void onToppingClick(Topping topping);
    }

    public ToppingAdapter(List<Topping> toppings, OnToppingClickListener listener, OnToppingImageChangeListener imageListener) {
        this.toppings = new ArrayList<>(toppings);
        this.listener = listener;
        this.imageListener = imageListener;
    }

    public interface OnToppingImageChangeListener {
        void onToppingImageChange(int imageResId);
    }


    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppings.get(position);
        holder.textView.setText(topping.toString());

        // Set the background color based on selection
        if (topping.isSelected()) {
            holder.textView.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_blue_light)); // Highlight color
        } else {
            holder.textView.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.transparent)); // Default color
        }

        // Notify the image listener when the topping is clicked
        holder.itemView.setOnClickListener(v -> {
            listener.onToppingClick(topping);
            if (topping.isSelected()) {
                imageListener.onToppingImageChange(topping.getImageResId()); // Change image to topping image
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
            notifyItemChanged(position); // Update the view for selection state change
        });
    }


    @Override
    public int getItemCount() {
        return toppings.size();
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
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
}
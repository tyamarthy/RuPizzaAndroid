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
    private List<Topping> toppings;
    private OnToppingClickListener listener;

    public interface OnToppingClickListener {
        void onToppingClick(Topping topping);
    }

    public ToppingAdapter(List<Topping> toppings, OnToppingClickListener listener) {
        this.toppings = new ArrayList<>(toppings);
        this.listener = listener;
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

        // Highlight selected toppings
        if (topping.isSelected()) {
            holder.textView.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_blue_light)); // Highlight color
        } else {
            holder.textView.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.transparent)); // Default color
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onToppingClick(topping);
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
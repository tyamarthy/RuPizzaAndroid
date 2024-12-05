package com.example.rupizzaandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

public class PizzaAdapter extends ArrayAdapter<Pizza> {
    private Context context;
    private List<Pizza> pizzas;
    private int selectedPosition = -1;

    public PizzaAdapter(Context context, List<Pizza> pizzas) {
        super(context, -1, pizzas);
        this.context = context;
        this.pizzas = pizzas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = rowView.findViewById(android.R.id.text1);


        Pizza pizza = pizzas.get(position);
        String pizzaDetails = String.format(
                "%s Pizza - %s, %s Crust\nToppings: %s\nPrice: $%.2f",
                pizza.getClass().getSimpleName(),
                pizza.getSize(),
                pizza.getCrust(),
                pizza.getToppings().stream()
                        .map(Topping::toString)
                        .collect(Collectors.joining(", ")),
                pizza.price()
        );

        textView.setText(pizzaDetails);

        if (position == selectedPosition) {
            rowView.setBackgroundColor(Color.LTGRAY);  // Use a light gray for selection
        } else {
            rowView.setBackgroundColor(Color.TRANSPARENT);
        }

        return rowView;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }
}
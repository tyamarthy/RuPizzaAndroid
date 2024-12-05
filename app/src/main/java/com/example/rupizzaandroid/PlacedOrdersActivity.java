package com.example.rupizzaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlacedOrdersActivity extends AppCompatActivity {

    private static final double TAX_RATE = 0.06625;
    private Spinner orderSpinner;
    private ListView orderDetailsListView;
    private TextView orderTotalTextView;
    private Button backButton;
    private Button cancelOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_orders_activity); // Make sure to use your XML layout

        // Initialize views
        orderSpinner = findViewById(R.id.spinner);
        orderDetailsListView = findViewById(R.id.orderList);
        orderTotalTextView = findViewById(R.id.orderAmount);
        backButton = findViewById(R.id.backButton);
        cancelOrderButton = findViewById(R.id.btnCancelOrder);

        // Set up back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlacedOrdersActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Populate the spinner with order numbers
        populateOrderSpinner();

        // Set up spinner selection listener
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int selectedOrderNumber = Integer.parseInt(orderSpinner.getSelectedItem().toString());
                Order selectedOrder = findOrderByNumber(selectedOrderNumber);
                if (selectedOrder != null) {
                    displayOrderDetails(selectedOrder);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });

        // Set up cancel order button
        cancelOrderButton.setOnClickListener(v -> cancelSelectedOrder());
    }

    private void populateOrderSpinner() {
        ArrayList<Order> allOrders = Order.getAllOrders();
        ArrayList<String> orderNumbers = new ArrayList<>();

        for (Order order : allOrders) {
            orderNumbers.add(String.valueOf(order.getOrderNum()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(adapter);
    }

    private Order findOrderByNumber(int orderNumber) {
        ArrayList<Order> allOrders = Order.getAllOrders();
        for (Order order : allOrders) {
            if (order.getOrderNum() == orderNumber) {
                return order;
            }
        }
        return null;
    }

    private void displayOrderDetails(Order order) {
        PizzaAdapter pizzaAdapter = new PizzaAdapter(this, order.getPizzas());
        orderDetailsListView.setAdapter(pizzaAdapter);

        // Calculate and display the total with tax
        double subtotal = 0;
        for (Pizza pizza : order.getPizzas()) {
            subtotal += pizza.price();
        }

        double salesTax = subtotal * TAX_RATE;
        double total = subtotal + salesTax;
        orderTotalTextView.setText(String.format("$%.2f", total));
    }

    private void cancelSelectedOrder() {
        int selectedOrderNumber = Integer.parseInt(orderSpinner.getSelectedItem().toString());
        Order selectedOrder = findOrderByNumber(selectedOrderNumber);

        if (selectedOrder != null) {
            // Remove the selected order from the list
            Order.getAllOrders().remove(selectedOrder);

            // Update the spinner
            populateOrderSpinner();

            // Clear order details from the UI
            orderDetailsListView.setAdapter(null);
            orderTotalTextView.setText("$0.00");

            // Show confirmation
            showSuccessAlert("Order Cancelled", "The selected order has been cancelled.");
        } else {
            showAlert("Error", "No order found to cancel.");
        }
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showSuccessAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}

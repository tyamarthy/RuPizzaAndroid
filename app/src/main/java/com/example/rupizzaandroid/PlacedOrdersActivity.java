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
import android.widget.Toast;

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


        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlacedOrdersActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        populateOrderSpinner();

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

            }
        });

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
        double subtotal = 0;
        for (Pizza pizza : order.getPizzas()) {
            subtotal += pizza.price();
        }

        double salesTax = subtotal * TAX_RATE;
        double total = subtotal + salesTax;
        orderTotalTextView.setText(String.format("$%.2f", total));
    }

    private void cancelSelectedOrder() {

        if(orderSpinner.getCount()==0||orderSpinner.getAdapter()==null){
            showAlert("Error","At the time there are no orders available to be cancelled");
            return;
        }

        String orderString=orderSpinner.getSelectedItem().toString();
        if(orderString==null || orderString.isEmpty()){
            showAlert("Error","Please select an order to cancel");
        }

        int selectedOrderNumber = Integer.parseInt(orderSpinner.getSelectedItem().toString());
        Order selectedOrder = findOrderByNumber(selectedOrderNumber);

        if (selectedOrder != null) {
            Order.getAllOrders().remove(selectedOrder);
            populateOrderSpinner();
            orderDetailsListView.setAdapter(null);
            orderTotalTextView.setText("$0.00");

            // Show confirmation
            Toast.makeText(this, "Order " + selectedOrderNumber + "is now cancelled!", Toast.LENGTH_SHORT).show();
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

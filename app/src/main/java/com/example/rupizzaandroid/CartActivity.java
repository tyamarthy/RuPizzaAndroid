package com.example.rupizzaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private static final double TAX_RATE = 0.06625;

    private ListView orderListView;
    private TextView orderNumberTextView;
    private TextView subtotalTextView;
    private TextView salesTaxTextView;
    private TextView orderTotalTextView;

    private Button backButton;
    private Button removePizzaButton;
    private Button clearOrderButton;
    private Button placeOrderButton;

    private OrderManager sharedOrder;
    private PizzaAdapter pizzaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        Button backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sharedOrder=OrderManager.getInstance();
        initializeViews();
        orderListViewSetUp();
        buttonListenerSetUp();
        displayOrderInfo();
    }

    private void initializeViews() {
        orderListView = findViewById(R.id.orderList);
        orderNumberTextView = findViewById(R.id.number);
        subtotalTextView = findViewById(R.id.subAmount);
        salesTaxTextView = findViewById(R.id.salesAmount);
        orderTotalTextView = findViewById(R.id.orderAmount);

        backButton = findViewById(R.id.backButton);
        removePizzaButton = findViewById(R.id.btnRemovePizza);
        clearOrderButton = findViewById(R.id.btnClearOrder);
        placeOrderButton = findViewById(R.id.btnPlaceOrder);
    }

    private void orderListViewSetUp() {
        pizzaAdapter = new PizzaAdapter(this,
                sharedOrder.getCurrentOrder().getPizzas());
        orderListView.setAdapter(pizzaAdapter);
        orderListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    private void buttonListenerSetUp() {

        removePizzaButton.setOnClickListener(v -> deletePizzaSelected());

        clearOrderButton.setOnClickListener(v -> {
            sharedOrder.clearOrder();
            displayOrderInfo();
        });

        placeOrderButton.setOnClickListener(v -> placeOrder());

        orderListView.setOnItemClickListener((parent, view, position, id) -> {
           PizzaAdapter adapter=(PizzaAdapter)orderListView.getAdapter();
           adapter.setSelectedPosition(position);
           orderListView.setItemChecked(position, true);
        });
    }

    private void deletePizzaSelected() {
        int selectedPosition = orderListView.getCheckedItemPosition();
        if (selectedPosition != ListView.INVALID_POSITION) {
            // Remove pizza at selected position
            sharedOrder.getCurrentOrder().getPizzas().remove(selectedPosition);
            displayOrderInfo();
        } else {
            showAlert("Error", "No pizza selected to remove.");
        }
    }

    private void placeOrder() {
        if (sharedOrder.getTotalPizzasInOrder() == 0) {
            showAlert("Error", "No pizzas in order to place.");
            return;
        }

        sharedOrder.getCurrentOrder().placeOrder();
        showSuccessAlert("Order Placed", "Your order has been placed successfully!");
        sharedOrder.clearOrder();
        displayOrderInfo();
    }

    private void displayOrderInfo() {
        ArrayList<Pizza> pizzas = sharedOrder.getCurrentOrder().getPizzas();
        pizzaAdapter = new PizzaAdapter(this,pizzas);
        orderListView.setAdapter(pizzaAdapter);
        double subtotal = pizzas.stream()
                .mapToDouble(Pizza::price)
                .sum();

        double salesTax = subtotal * TAX_RATE;
        double total = subtotal + salesTax;
        orderNumberTextView.setText(String.valueOf(sharedOrder.getCurrentOrder().getOrderNum()));
        subtotalTextView.setText(String.format("$%.2f", subtotal));
        salesTaxTextView.setText(String.format("$%.2f", salesTax));
        orderTotalTextView.setText(String.format("$%.2f", total));
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



















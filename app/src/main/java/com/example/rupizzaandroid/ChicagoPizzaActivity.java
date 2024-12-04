package com.example.rupizzaandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ChicagoPizzaActivity extends AppCompatActivity {

    private Button backButton, orderButton;
    private TextView titlePizza, crustTextView, priceTextView, priceLabel;
    private ImageView pizzaImageView;
    private Spinner spinnerPizzaSpecialty, spinnerPizzaType;
    private RadioGroup radioGroupSize;
    private RadioButton radioSmall, radioMedium, radioLarge;
    private ChipGroup chipGroupToppings;

    private Pizza currPizza;
    private ChicagoPizza chicagoPizzaFactory = new ChicagoPizza();
    private Order currentOrder;
    private List<Topping> availableToppings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chicago_activity);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        orderButton = findViewById(R.id.orderButton);
        titlePizza = findViewById(R.id.titlePizza);
        crustTextView = findViewById(R.id.crustTextView);
        priceTextView = findViewById(R.id.priceTextView);
        pizzaImageView = findViewById(R.id.pizzaImageView);
        spinnerPizzaSpecialty = findViewById(R.id.spinnerPizzaSpecialty);
        spinnerPizzaType = findViewById(R.id.spinnerPizzaType);
        radioGroupSize = findViewById(R.id.radioGroupSize);
        radioSmall = findViewById(R.id.radioSmall);
        radioMedium = findViewById(R.id.radioMedium);
        radioLarge = findViewById(R.id.radioLarge);
        chipGroupToppings = findViewById(R.id.chipGroupToppings);

        // Initialize data
        availableToppings = new ArrayList<>();
        availableToppings.addAll(List.of(
                Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER,
                Topping.ONION, Topping.MUSHROOM, Topping.BBQ_CHICKEN,
                Topping.CHEDDAR, Topping.PROVOLONE, Topping.BEEF, Topping.HAM,
                Topping.OLIVES, Topping.PINEAPPLES, Topping.JALAPENOS
        ));
        currentOrder = new Order(); // Example initialization

        setupUI();
    }

    private void setupUI() {
        // Setup pizza specialty spinner
        ArrayAdapter<String> specialtyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Select Specialty...", "Deluxe", "BBQ Chicken", "Meatzza", "Build your own"});
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaSpecialty.setAdapter(specialtyAdapter);

        spinnerPizzaSpecialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                updatePizzaSelection(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Radio group size change listener
        radioGroupSize.setOnCheckedChangeListener((group, checkedId) -> updatePrice());

        // Setup back button
        backButton.setOnClickListener(v -> finish());

        // Setup order button
        orderButton.setOnClickListener(v -> addPizzaToOrder());

        // Initialize toppings
        loadAvailableToppings();
        resetFields();
    }

    private void updatePizzaSelection(String pizzaType) {
        if ("Select Specialty...".equals(pizzaType)) {
            resetFields();
            return;
        }

        switch (pizzaType) {
            case "Deluxe":
                currPizza = chicagoPizzaFactory.createDeluxe();
                chipGroupToppings.setEnabled(false);
                break;
            case "BBQ Chicken":
                currPizza = chicagoPizzaFactory.createBBQChicken();
                chipGroupToppings.setEnabled(false);
                break;
            case "Meatzza":
                currPizza = chicagoPizzaFactory.createMeatzza();
                chipGroupToppings.setEnabled(false);
                break;
            case "Build your own":
                currPizza = chicagoPizzaFactory.createBuildYourOwn();
                chipGroupToppings.setEnabled(true);
                break;
        }

        crustTextView.setText(currPizza.getCrust().toString());
        loadSelectedToppings();
        updatePrice();
    }

    private void loadAvailableToppings() {
        chipGroupToppings.removeAllViews();
        for (Topping topping : availableToppings) {
            Chip chip = new Chip(this);
            chip.setText(topping.toString());
            chip.setOnClickListener(v -> addTopping(topping));
            chipGroupToppings.addView(chip);
        }
    }

    private void loadSelectedToppings() {
        chipGroupToppings.removeAllViews();
        if (currPizza != null) {
            for (Topping topping : currPizza.getToppings()) {
                Chip chip = new Chip(this);
                chip.setText(topping.toString());
                chip.setOnClickListener(v -> removeTopping(topping));
                chipGroupToppings.addView(chip);
            }
        }
    }

    private void addTopping(Topping topping) {
        if (currPizza == null || !"BuildYourOwn".equals(currPizza.getClass().getSimpleName())) {
            showAlert("Error", "Toppings can only be added to Build Your Own pizzas.");
            return;
        }

        if (currPizza.getToppings().size() >= 7) {
            showAlert("Toppings Limit", "You can only select up to 7 toppings.");
            return;
        }

        currPizza.addTopping(topping);
        availableToppings.remove(topping);
        loadSelectedToppings();
        loadAvailableToppings();
        updatePrice();
    }

    private void removeTopping(Topping topping) {
        if (currPizza != null) {
            currPizza.removeTopping(topping);
            availableToppings.add(topping);
            loadSelectedToppings();
            loadAvailableToppings();
            updatePrice();
        }
    }

    private void updatePrice() {
        if (currPizza == null) {
            priceTextView.setText("$0.00");
            return;
        }

        Size selectedSize = null;
        if (radioSmall.isChecked()) {
            selectedSize = Size.SMALL;
        } else if (radioMedium.isChecked()) {
            selectedSize = Size.MEDIUM;
        } else if (radioLarge.isChecked()) {
            selectedSize = Size.LARGE;
        }

        if (selectedSize != null) {
            currPizza.setSize(selectedSize);
            priceTextView.setText(String.format("$%.2f", currPizza.price()));
        }
    }

    private void addPizzaToOrder() {
        if (currPizza == null) {
            showAlert("Error", "No pizza selected.");
            return;
        }

        if (radioGroupSize.getCheckedRadioButtonId() == -1) {
            showAlert("Error", "Please select a size.");
            return;
        }

        currentOrder.addAPizza(currPizza);
        showAlert("Success", "Pizza added to order!");
        resetFields();
    }

    private void resetFields() {
        spinnerPizzaSpecialty.setSelection(0);
        crustTextView.setText("");
        priceTextView.setText("$0.00");
        radioGroupSize.clearCheck();
        chipGroupToppings.removeAllViews();
        currPizza = null;
        loadAvailableToppings();
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}

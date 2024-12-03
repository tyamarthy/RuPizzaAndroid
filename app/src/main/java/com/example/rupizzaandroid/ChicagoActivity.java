package com.example.rupizzaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class ChicagoActivity extends AppCompatActivity {

    // Declare UI components
    private Spinner spinnerPizzaSpecialty;
    private Spinner spinnerPizzaType;
    private RadioGroup radioGroupPizzaSize;
    private ChipGroup chipGroupToppings;
    private TextView crustTextView;
    private TextView priceTextView;

    // Enum for toppings
    public enum Topping {
        SAUSAGE,
        PEPPERONI,
        GREEN_PEPPER,
        ONION,
        MUSHROOM,
        BBQ_CHICKEN,
        CHEDDAR,
        PROVOLONE,
        BEEF,
        HAM,
        OLIVES,
        PINEAPPLES,
        JALAPENOS
    }

    private String selectedPizzaType = "";
    private String selectedPizzaSize = "";
    private double pizzaPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chicago_activity);

        // Initialize UI components
        spinnerPizzaSpecialty = findViewById(R.id.spinnerPizzaSpecialty);
        spinnerPizzaType = findViewById(R.id.spinnerPizzaType);
        radioGroupPizzaSize = findViewById(R.id.radioGroupSize);
        chipGroupToppings = findViewById(R.id.chipGroupToppings);
        crustTextView = findViewById(R.id.crustTextView);
        priceTextView = findViewById(R.id.priceTextView);

        // Set up Pizza Specialty Spinner
        setupPizzaSpecialtySpinner();

        // Set up Pizza Type Spinner
        setupPizzaTypeSpinner();

        // Set up Pizza Size Radio Buttons
        setupPizzaSizeRadioButtons();

        // Add toppings to ChipGroup
        setupToppingChips();

        // Back Button functionality
        setupBackButton();

        // Order Button functionality
        setupOrderButton();
    }

    private void setupPizzaSpecialtySpinner() {
        ArrayAdapter<CharSequence> specialtyAdapter = ArrayAdapter.createFromResource(
                this, R.array.pizza_specialties, android.R.layout.simple_spinner_item);
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaSpecialty.setAdapter(specialtyAdapter);

        spinnerPizzaSpecialty.setPrompt("Choose pizza specialty");
        spinnerPizzaSpecialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle pizza specialty selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection (if needed)
            }
        });
    }

    private void setupPizzaTypeSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.pizza_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPizzaType.setAdapter(typeAdapter);

        spinnerPizzaType.setPrompt("Choose pizza type");
        spinnerPizzaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPizzaType = parentView.getItemAtPosition(position).toString();
                updateCrustAndPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection (if needed)
            }
        });
    }

    private void setupPizzaSizeRadioButtons() {
        radioGroupPizzaSize.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = group.findViewById(checkedId);
            selectedPizzaSize = selectedButton.getText().toString();
            updateCrustAndPrice();
        });
    }

    private void setupToppingChips() {
        for (final Topping topping : Topping.values()) {
            Chip chip = new Chip(this);
            chip.setText(topping.name());
            chip.setCheckable(true);
            chip.setChipIconResource(android.R.drawable.checkbox_on_background);
            chip.setChipIconTintResource(android.R.color.darker_gray);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Handle the topping selection change
            });

            chipGroupToppings.addView(chip);
        }
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Redirect to MainActivity
            Intent intent = new Intent(ChicagoActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Closes the current activity
        });
    }

    private void setupOrderButton() {
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> {
            // Handle order placement
        });
    }

    private void updateCrustAndPrice() {
        // Update crust text
        if (selectedPizzaType.equals("New York Pizza")) {
            crustTextView.setText("Crust: Thin");
        } else {
            crustTextView.setText("Crust: Hand-Tossed");
        }

        // Update price
        calculateAndDisplayPrice();
    }

    private void calculateAndDisplayPrice() {
        // Calculate the pizza price based on the selected type and size
        if (selectedPizzaType.equals("New York Pizza")) {
            if (selectedPizzaSize.equals("Small")) {
                pizzaPrice = 12.99;
            } else if (selectedPizzaSize.equals("Medium")) {
                pizzaPrice = 14.99;
            } else {
                pizzaPrice = 16.99;
            }
        } else {
            if (selectedPizzaSize.equals("Small")) {
                pizzaPrice = 14.99;
            } else if (selectedPizzaSize.equals("Medium")) {
                pizzaPrice = 16.99;
            } else {
                pizzaPrice = 18.99;
            }
        }

        // Display the price
        priceTextView.setText(String.format("$%.2f", pizzaPrice));
    }
}
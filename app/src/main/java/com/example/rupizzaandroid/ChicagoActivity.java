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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
public class ChicagoActivity extends AppCompatActivity {

    // Declare UI components
    private Spinner spinnerPizzaSpecialty;
    private Spinner spinnerPizzaType;
    private RadioGroup radioGroupPizzaSize;
    private ChipGroup chipGroupToppings;
    private TextView crustTextView;
    private TextView priceTextView;


    private String selectedPizzaType = "";
    private String selectedPizzaSize = "";
    private String selectedPizzaSpecialty = "";
    private double pizzaPrice = 0.0;
    private boolean isBuildYourOwn = false; // Track if "Build Your Own" is selected
    private int selectedToppingCount = 0; // Track the number of selected toppings
    private ArrayList<Pizza> orders; // List to store pizzas in the order
    private ArrayList<String> selectedToppings; // Store selected toppings

    private ChicagoPizza chicagoPizzaFactory = new ChicagoPizza();
    private Pizza currPizza;

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
        orders = new ArrayList<>(); // Initialize the orders list
        selectedToppings = new ArrayList<>(); // Initialize toppings list
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
                selectedPizzaSpecialty = parentView.getItemAtPosition(position).toString();
                updateCrustAndPrice();  // Update crust and price when specialty changes
                setDefaultToppings(selectedPizzaSpecialty); // Set default toppings based on selected specialty
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection (if needed)
            }
        });

        // Trigger update for default selection
        selectedPizzaSpecialty = spinnerPizzaSpecialty.getSelectedItem().toString();
        updateCrustAndPrice();
        setDefaultToppings(selectedPizzaSpecialty); // Set default toppings initially
    }
    private boolean shouldPreserveToppings = false;

    private void clearToppings() {
        // Only clear the toppings if toppings are not being preserved
        if (!shouldPreserveToppings) {
            selectedToppings.clear();
            selectedToppingCount = 0;
            for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupToppings.getChildAt(i);
                chip.setChecked(false);
                chip.setEnabled(true); // Re-enable the chips so they are editable
            }
        }
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
                isBuildYourOwn = selectedPizzaType.equals("Build Your Own"); // Set to true if "Build Your Own" is selected
                selectedToppingCount = 0; // Reset topping count when pizza type changes
                updateCrustAndPrice();
                toggleToppingChips(isBuildYourOwn); // Show/hide toppings based on selection
                String selectedType = parentView.getItemAtPosition(position).toString();
                setDefaultToppings(selectedType);  // Set toppings based on the selected pizza type
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection (if needed)
            }
        });

        // Trigger update for default selection
        selectedPizzaType = spinnerPizzaType.getSelectedItem().toString();
        isBuildYourOwn = selectedPizzaType.equals("Build Your Own");
        updateCrustAndPrice();
    }

    private void resetToppingChips() {
        // Disable all topping chips if not "Build Your Own"
        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            chip.setChecked(false);  // Uncheck all chips
            chip.setEnabled(isBuildYourOwn);  // Enable only if "Build Your Own" is selected
        }
        selectedToppingCount = 0; // Reset topping count when switching pizza types
        selectedToppings.clear(); // Clear the topping list
    }



    private void setupPizzaSizeRadioButtons() {
        radioGroupPizzaSize.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = group.findViewById(checkedId);
            selectedPizzaSize = selectedButton.getText().toString();

            // Reset toppings when size changes
            selectedToppingCount = 0;
            selectedToppings.clear();
            for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupToppings.getChildAt(i);
                chip.setChecked(false);
            }

            updateCrustAndPrice();
        });
    }

    private void setupToppingChips() {
        // Clear any previous chips
        chipGroupToppings.removeAllViews();

        // Set up the chips for toppings
        for (final Topping topping : Topping.values()) {
            Chip chip = new Chip(this);
            chip.setText(topping.name());
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedToppingCount < 7) {
                        selectedToppings.add(topping.name());
                        selectedToppingCount++;
                    } else {
                        Toast.makeText(ChicagoActivity.this, "No more than 7 toppings allowed.", Toast.LENGTH_SHORT).show();
                        chip.setChecked(false); // Revert the selection
                    }
                } else {
                    selectedToppings.remove(topping.name());
                    selectedToppingCount--;
                }
                if (isBuildYourOwn) {
                    if (isChecked) {
                        if (selectedToppingCount < 7) {
                            selectedToppings.add(topping.name());
                            selectedToppingCount++;
                        } else {
                            Toast.makeText(ChicagoActivity.this, "You can select up to 7 toppings only.", Toast.LENGTH_SHORT).show();
                            chip.setChecked(false);
                        }
                    } else {
                        selectedToppings.remove(topping.name());
                        selectedToppingCount--;
                    }
                    updateCrustAndPrice();
                    toggleToppingChips(selectedToppingCount < 7);
                }
            });

            chipGroupToppings.addView(chip);
        }
    }

    private void toggleToppingChips(boolean isEnabled) {
        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            chip.setEnabled(isEnabled);
        }
    }

    private void setDefaultToppings(String type) {
        // Determine if we need to reset the toppings
        boolean shouldResetToppings = !selectedPizzaSpecialty.equals(type);

        // Set the preserve toppings flag based on specialty/type changes
        shouldPreserveToppings = !shouldResetToppings;  // Keep toppings if not changing specialty

        // Clear previous selections only when switching to a new specialty
        if (shouldResetToppings) {
            clearToppings();
        }

        selectedPizzaSpecialty = type;

        // Set toppings based on the selected specialty
        switch (type) {
            case "Deluxe":
                selectAndLockToppings(new Topping[] {
                        Topping.BBQ_CHICKEN,
                        Topping.CHEDDAR,
                        Topping.PROVOLONE,
                        Topping.BEEF,
                        Topping.HAM,
                        Topping.OLIVES,
                        Topping.PINEAPPLES,
                        Topping.JALAPENOS
                });
                break;
            case "BBQ":
                selectAndLockToppings(new Topping[] {
                        Topping.SAUSAGE,
                        Topping.PEPPERONI,
                        Topping.ONION,
                        Topping.MUSHROOM,
                        Topping.BEEF,
                        Topping.HAM,
                        Topping.OLIVES,
                        Topping.PINEAPPLES,
                        Topping.JALAPENOS
                });
                break;
            case "Meatzza":
                selectAndLockToppings(new Topping[] {
                        Topping.GREEN_PEPPER,
                        Topping.ONION,
                        Topping.MUSHROOM,
                        Topping.BBQ_CHICKEN,
                        Topping.CHEDDAR,
                        Topping.PROVOLONE,
                        Topping.OLIVES,
                        Topping.PINEAPPLES,
                        Topping.JALAPENOS
                });
                break;
            case "Build Your Own":
                enableAllToppingsForCustom();
                break;
            default:
                clearToppings();  // In case of an unknown type, clear toppings
                break;
        }
    }

    private void selectAndLockToppings(Topping[] toppings) {
        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            String toppingName = chip.getText().toString();
            Topping topping = Topping.valueOf(toppingName.replace(" ", "_").toUpperCase()); // Convert chip text to Topping enum

            // Check if the topping is part of the pre-selected list
            boolean shouldSelectTopping = false;
            for (Topping preselectedTopping : toppings) {
                if (topping == preselectedTopping) {
                    shouldSelectTopping = true;
                    break;
                }
            }

            // Pre-select and lock the topping if it is part of the pre-selected list
            chip.setChecked(shouldSelectTopping);
            chip.setEnabled(!shouldSelectTopping);  // Disable (lock) the topping if it is pre-selected
        }
    }

    private void enableAllToppingsForCustom() {
        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            chip.setEnabled(true);  // Enable all toppings
            chip.setChecked(false);  // Deselect all toppings initially
        }

        selectedToppings.clear();  // Clear any previous selections
        selectedToppingCount = 0;
    }

    private void toggleToppingChips(boolean isEnabled) {
        for (int i = 0; i < chipGroupToppings.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupToppings.getChildAt(i);
            if (!isEnabled) {
                chip.setEnabled(false); // Disable all chips if pizza is not "Build Your Own"
            } else {
                chip.setEnabled(true); // Enable chips only if "Build Your Own" is selected
            }
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
        // Update crust text based on pizza type and specialty
        if (selectedPizzaSpecialty.equals("Chicago Pizza")) {
            switch (selectedPizzaType) {
                case "Deluxe":
                    crustTextView.setText("Crust: Deep Dish");
                    setDefaultToppings("Deluxe");
                    break;
                case "BBQ Chicken":
                    crustTextView.setText("Crust: Pan");
                    setDefaultToppings("BBQ");
                    break;
                case "Meatzza":
                    crustTextView.setText("Crust: Stuffed");
                    setDefaultToppings("Meatzza");
                    break;
                case "Build Your Own":
                    crustTextView.setText("Crust: Pan");
                    setDefaultToppings("Build Your Own");
                    break;
                default:
                    crustTextView.setText("Crust: Unknown");
                    break;
            }
        } else if (selectedPizzaSpecialty.equals("New York Pizza")) {
            switch (selectedPizzaType) {
                case "Deluxe":
                    crustTextView.setText("Crust: Brooklyn");
                    break;
                case "BBQ Chicken":
                    crustTextView.setText("Crust: Thin");
                    break;
                case "Meatzza":
                    crustTextView.setText("Crust: Hand Tossed");
                    break;
                case "Build Your Own":
                    crustTextView.setText("Crust: Hand Tossed");
                    break;
                default:
                    crustTextView.setText("Crust: Unknown");
                    break;
            }
        }

        // Update price
        calculateAndDisplayPrice();
    }

    private void calculateAndDisplayPrice() {
        // Default base price
        pizzaPrice = 0.0;

        // Get the selected size
        String selectedSize = selectedPizzaSize;

        // Pricing based on pizza type and size
        switch (selectedPizzaType) {
            case "Deluxe":
                if (selectedSize.equals("Small")) {
                    pizzaPrice = 16.99;
                } else if (selectedSize.equals("Medium")) {
                    pizzaPrice = 18.99;
                } else if (selectedSize.equals("Large")) {
                    pizzaPrice = 20.99;
                }
                break;
            case "BBQ Chicken":
                if (selectedSize.equals("Small")) {
                    pizzaPrice = 14.99;
                } else if (selectedSize.equals("Medium")) {
                    pizzaPrice = 16.99;
                } else if (selectedSize.equals("Large")) {
                    pizzaPrice = 19.99;
                }
                break;
            case "Meatzza":
                if (selectedSize.equals("Small")) {
                    pizzaPrice = 17.99;
                } else if (selectedSize.equals("Medium")) {
                    pizzaPrice = 19.99;
                } else if (selectedSize.equals("Large")) {
                    pizzaPrice = 21.99;
                }
                break;
            case "Build Your Own":
                if (selectedSize.equals("Small")) {
                    pizzaPrice = 8.99;
                } else if (selectedSize.equals("Medium")) {
                    pizzaPrice = 10.99;
                } else if (selectedSize.equals("Large")) {
                    pizzaPrice = 12.99;
                }

                // Only add topping prices if toppings are selected
                if (selectedToppingCount > 0) {
                    pizzaPrice += selectedToppingCount * 1.69;
                }
                break;
        }

        // Update the price TextView
        priceTextView.setText(String.format("$%.2f", pizzaPrice));
    }
    private void setupOrderButton() {
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> {
            if (selectedPizzaSize.isEmpty()) {
                Toast.makeText(ChicagoActivity.this, "Please select a pizza size.", Toast.LENGTH_SHORT).show();
                return;
            }

            Pizza pizza = createPizza();
            if (pizza != null) {
                orders.add(pizza);
                Toast.makeText(ChicagoActivity.this, "Pizza added to order!", Toast.LENGTH_SHORT).show();
                resetSelections();
            } else {
                Toast.makeText(ChicagoActivity.this, "Failed to create pizza.", Toast.LENGTH_SHORT).show();
            }
        }

    private Pizza createPizza() {
        Pizza pizza;
        switch (selectedPizzaType ) {
            case "Deluxe":
                currPizza = chicagoPizzaFactory.createDeluxe();
                break;
            case "BBQ Chicken":
                currPizza = chicagoPizzaFactory.createBBQChicken();
                break;
            case "Meatzza":
                currPizza = chicagoPizzaFactory.createMeatzza();
                break;
            case "Build Your Own":
                currPizza = chicagoPizzaFactory.createBuildYourOwn();
                break;
            default:
                pizza = null;
        }
        return currPizza;
    }

    private void resetSelections() {
        // Reset all selections after adding a pizza
        selectedPizzaSize = "";
        radioGroupPizzaSize.clearCheck();
        selectedToppingCount = 0;
        selectedToppings.clear();
        spinnerPizzaSpecialty.setSelection(0);
        spinnerPizzaType.setSelection(0);
        toggleToppingChips(false);
        priceTextView.setText("$0.00");
    }
}
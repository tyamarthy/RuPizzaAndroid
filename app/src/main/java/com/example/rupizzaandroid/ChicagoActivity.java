package com.example.rupizzaandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class ChicagoActivity extends AppCompatActivity {

    private Spinner pizzaTypeSpinner;
    private RecyclerView availableToppingsRecyclerView;
    private RecyclerView selectedToppingsRecyclerView;
    private TextView crustTextView;
    private TextView priceTextView;
    private RadioButton smallRadioButton;
    private RadioButton mediumRadioButton;
    private RadioButton largeRadioButton;
    private ImageView pizzaImageView;
    private Button backButton;
    private Button orderButton;
    private Button addToppingButton;
    private Button removeToppingButton;

    private static final int MAX_TOPPINGS_LIMIT = 6;

    private Pizza currPizza;
    private ChicagoPizza chicagoPizzaFactory = new ChicagoPizza();
    private Order currentOrder;

    private List<Topping> availableToppings = List.of(
            Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER,
            Topping.ONION, Topping.MUSHROOM, Topping.BBQ_CHICKEN,
            Topping.CHEDDAR, Topping.PROVOLONE, Topping.BEEF, Topping.HAM,
            Topping.OLIVES, Topping.PINEAPPLES, Topping.JALAPENOS
    );

    private ToppingAdapter availableToppingsAdapter;
    private ToppingAdapter selectedToppingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chicago_activity);

        initializeViews();
        setupSpinner();
        setupRadioButtons();
        setupRecyclerViews();
        setupButtons();
    }

    private void initializeViews() {
        pizzaTypeSpinner = findViewById(R.id.spinnerPizzaSpecialty);
        availableToppingsRecyclerView = findViewById(R.id.recyclerViewToppings);
        crustTextView = findViewById(R.id.crustTextView);
        priceTextView = findViewById(R.id.priceTextView);
        smallRadioButton = findViewById(R.id.radioSmall);
        mediumRadioButton = findViewById(R.id.radioMedium);
        largeRadioButton = findViewById(R.id.radioLarge);
        pizzaImageView = findViewById(R.id.pizzaImageView);
        backButton = findViewById(R.id.backButton);
        orderButton = findViewById(R.id.orderButton);
    }

    private void setupSpinner() {
        String[] pizzaTypes = {"Select Pizza Type...", "Deluxe", "BBQ Chicken", "Meatzza", "Build your own"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(adapter);

        pizzaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPizzaType = (String) parent.getItemAtPosition(position);
                updatePizzaSelection(selectedPizzaType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRadioButtons() {
        smallRadioButton.setOnClickListener(v -> {
            mediumRadioButton.setChecked(false);
            largeRadioButton.setChecked(false);
            updatePrice();
        });

        mediumRadioButton.setOnClickListener(v -> {
            smallRadioButton.setChecked(false);
            largeRadioButton.setChecked(false);
            updatePrice();
        });

        largeRadioButton.setOnClickListener(v -> {
            smallRadioButton.setChecked(false);
            mediumRadioButton.setChecked(false);
            updatePrice();
        });
    }

    private void setupRecyclerViews() {
        availableToppingsAdapter = new ToppingAdapter(availableToppings, topping -> {
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                if (topping.isSelected()) {
                    // Deselect topping
                    topping.setSelected(false);
                    selectedToppingsAdapter.removeTopping(topping);
                    currPizza.getToppings().remove(topping);

                    // Check if no toppings are selected and reset image if true
                    if (currPizza.getToppings().isEmpty()) {
                        pizzaImageView.setImageResource(R.drawable.buildyourown);
                    }
                } else {
                    // Select topping
                    if (selectedToppingsAdapter.getItemCount() >= MAX_TOPPINGS_LIMIT) {
                        showAlert("Toppings Limit Reached", "You are only allowed to select up to " + MAX_TOPPINGS_LIMIT + " toppings.");
                        return;
                    }
                    topping.setSelected(true);
                    selectedToppingsAdapter.addTopping(topping);
                    currPizza.addTopping(topping);
                }

                // Update the pizza image and price
                updateBuildYourOwnImage();
                updatePrice();
                availableToppingsAdapter.notifyDataSetChanged(); // Refresh the UI
            }
        }, imageResId -> {
            pizzaImageView.setImageResource(imageResId);
        });

        selectedToppingsAdapter = new ToppingAdapter(new ArrayList<>(), topping -> {
            // No direct action for selected toppings adapter in this design
        }, imageResId -> {
            pizzaImageView.setImageResource(imageResId);
        });

        availableToppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableToppingsRecyclerView.setAdapter(availableToppingsAdapter);
    }

    private void updateBuildYourOwnImage() {
        if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
            List<Topping> selectedToppings = currPizza.getToppings();

            if (selectedToppings.isEmpty()) {
                // No toppings selected, reset to default image
                pizzaImageView.setImageResource(R.drawable.buildyourown);
            } else {
                // Show the image for the most recently added topping
                Topping lastTopping = selectedToppings.get(selectedToppings.size() - 1);
                int toppingImageResource = getToppingImageResource(lastTopping);
                pizzaImageView.setImageResource(toppingImageResource);
            }
        }
    }


    private int getToppingImageResource(Topping topping) {
        switch (topping) {
            case SAUSAGE: return R.drawable.sausage;
            case PEPPERONI: return R.drawable.pepperoni;
            case GREEN_PEPPER: return R.drawable.greenpepper;
            case ONION: return R.drawable.onion;
            case MUSHROOM: return R.drawable.mushroom;
            case BBQ_CHICKEN: return R.drawable.bbqchicken;
            case CHEDDAR: return R.drawable.cheddar;
            case PROVOLONE: return R.drawable.provolone;
            case BEEF: return R.drawable.beef;
            case HAM: return R.drawable.ham;
            case OLIVES: return R.drawable.olives;
            case PINEAPPLES: return R.drawable.pineapple;
            case JALAPENOS: return R.drawable.jalapenos;
            default: return R.drawable.buildyourown; // Fallback default image
        }
    }

    private void setupButtons() {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        orderButton.setOnClickListener(v -> addPizzaToOrder());
    }

    private void updatePizzaSelection(String pizzaType) {
        // Reset all toppings
        for (Topping topping : availableToppings) {
            topping.setSelected(false);
        }
        availableToppingsAdapter.notifyDataSetChanged();

        // Clear selected toppings
        selectedToppingsAdapter.clearToppings();

        // Create pizza based on selection
        switch (pizzaType) {
            case "Deluxe":
                currPizza = chicagoPizzaFactory.createDeluxe();
                selectedToppingsAdapter.addAllToppings(currPizza.getToppings());
                pizzaImageView.setImageResource(R.drawable.deluxe);  // Set the image for Deluxe
                break;
            case "BBQ Chicken":
                currPizza = chicagoPizzaFactory.createBBQChicken();
                selectedToppingsAdapter.addAllToppings(currPizza.getToppings());
                pizzaImageView.setImageResource(R.drawable.bbqchicken);  // Set the image for BBQ Chicken
                break;
            case "Meatzza":
                currPizza = chicagoPizzaFactory.createMeatzza();
                selectedToppingsAdapter.addAllToppings(currPizza.getToppings());
                pizzaImageView.setImageResource(R.drawable.meatzza);  // Set the image for Meatzza
                break;
            case "Build your own":
                currPizza = chicagoPizzaFactory.createBuildYourOwn();
                pizzaImageView.setImageResource(R.drawable.buildyourown);  // Default image for Build Your Own
                break;
            default:
                currPizza = null;
                pizzaImageView.setImageResource(R.drawable.pinkpizza);  // Fallback default image
                break;
        }

        crustTextView.setText("Crust: " + (currPizza != null ? currPizza.getCrust() : ""));

        // Reset size and price
        smallRadioButton.setChecked(false);
        mediumRadioButton.setChecked(false);
        largeRadioButton.setChecked(false);
        updatePrice();
    }

    private void updatePrice() {
        double price = 0.0;
        Size selectedSize = null;

        if (smallRadioButton.isChecked()) {
            selectedSize = Size.SMALL;
        } else if (mediumRadioButton.isChecked()) {
            selectedSize = Size.MEDIUM;
        } else if (largeRadioButton.isChecked()) {
            selectedSize = Size.LARGE;
        }

        if (selectedSize != null && currPizza != null) {
            currPizza.setSize(selectedSize);
            price = currPizza.price();
        }

        priceTextView.setText(String.format("$%.2f", price));
    }

    private void addPizzaToOrder() {
        String selectedPizza = (String) pizzaTypeSpinner.getSelectedItem();

        if ("Select Pizza Type...".equals(selectedPizza)) {
            showAlert("Cannot Add to Order", "Please select your pizza type to proceed");
            return;
        }

        if (!(smallRadioButton.isChecked() || mediumRadioButton.isChecked() || largeRadioButton.isChecked())) {
            showAlert("Cannot Add to Order", "You must select a size for the pizza to proceed");
            return;
        }

        if (currPizza != null) {
            if (currentOrder == null) {
                currentOrder = new Order(); // Initialize order if not already done
            }
            currentOrder.addAPizza(currPizza);
            showSuccess("Pizza Added to Order!", "Pizza added successfully!\nTotal pizzas in order: " + currentOrder.getPizzas().size());
            resetFields();
        } else {
            showAlert("Error Adding to Order", "Unable to add this pizza to your order");
        }
    }

    private void resetFields() {
        pizzaTypeSpinner.setSelection(0);
        availableToppingsAdapter.clearToppings();
        availableToppingsAdapter.addAllToppings(availableToppings);
        selectedToppingsAdapter.clearToppings();
        crustTextView.setText("Crust: ");
        priceTextView.setText("$0.00");
        smallRadioButton.setChecked(false);
        mediumRadioButton.setChecked(false);
        largeRadioButton.setChecked(false);
        pizzaImageView.setImageResource(R.drawable.pinkpizza);
        currPizza = null;
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showSuccess(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_light);
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
    }
}
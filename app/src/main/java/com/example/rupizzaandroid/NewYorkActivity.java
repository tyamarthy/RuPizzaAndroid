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

public class NewYorkActivity extends AppCompatActivity {

    private Spinner pizzaTypeSpinner;
    private RecyclerView availableToppingsRecyclerView;
    private TextView crustTextView;
    private TextView priceTextView;
    private RadioButton smallRadioButton;
    private RadioButton mediumRadioButton;
    private RadioButton largeRadioButton;
    private ImageView pizzaImageView;
    private Button backButton;
    private Button orderButton;

    private static final int MAX_TOPPINGS_LIMIT = 7;

    private Pizza currPizza;
    private NYPizza newYorkPizzaFactory = new NYPizza();
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
        setContentView(R.layout.ny_activity);

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
            // Check if the current pizza is "Build Your Own"
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                if (topping.isSelected()) {
                    // Deselect topping
                    topping.setSelected(false);
                    selectedToppingsAdapter.removeTopping(topping);
                    currPizza.getToppings().remove(topping);

                    // Reset image if no toppings are selected
                    if (currPizza.getToppings().isEmpty()) {
                        pizzaImageView.setImageResource(R.drawable.buildyourown);
                    }
                } else {
                    // Add topping, enforcing the maximum limit
                    if (selectedToppingsAdapter.getItemCount() >= MAX_TOPPINGS_LIMIT) {
                        showAlert("Toppings Limit Reached", "You are only allowed to select up to " + MAX_TOPPINGS_LIMIT + " toppings.");
                        return;
                    }
                    topping.setSelected(true);
                    selectedToppingsAdapter.addTopping(topping);
                    currPizza.addTopping(topping);
                }

                // Update the image and price specifically for "Build Your Own"
                updateBuildYourOwnImage();
                updatePrice();
                availableToppingsAdapter.notifyDataSetChanged(); // Refresh the UI
            } else {
                // Prevent topping selection for non-editable pizzas
                showAlert("Topping Selection Disabled", "Toppings can only be edited for 'Build Your Own' pizzas.");
            }
        }, imageResId -> {
            // Only update image if "Build Your Own" is selected
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                pizzaImageView.setImageResource(imageResId);
            }
        });

        selectedToppingsAdapter = new ToppingAdapter(new ArrayList<>(), topping -> {
            // No direct action for selected toppings adapter in this design
        }, imageResId -> {
            // Prevent image updates from selected toppings for other pizza types
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                pizzaImageView.setImageResource(imageResId);
            }
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
        // Reset all toppings to unselected and uneditable
        for (Topping topping : availableToppings) {
            topping.setSelected(false);
            topping.setEditable(false); // Disable editing by default
        }
        availableToppingsAdapter.notifyDataSetChanged();

        // Clear selected toppings
        selectedToppingsAdapter.clearToppings();

        // Handle pizza selection logic
        switch (pizzaType) {
            case "Deluxe":
                currPizza = newYorkPizzaFactory.createDeluxe();
                highlightSpecialtyToppings(List.of(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER,
                        Topping.ONION, Topping.MUSHROOM
                ));
                pizzaImageView.setImageResource(R.drawable.deluxe); // Deluxe image
                break;

            case "BBQ Chicken":
                currPizza = newYorkPizzaFactory.createBBQChicken();
                highlightSpecialtyToppings(List.of(
                        Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER,
                        Topping.PROVOLONE, Topping.CHEDDAR
                ));
                pizzaImageView.setImageResource(R.drawable.bbqchicken); // BBQ Chicken image
                break;

            case "Meatzza":
                currPizza = newYorkPizzaFactory.createMeatzza();
                highlightSpecialtyToppings(List.of(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF,
                        Topping.HAM
                ));
                pizzaImageView.setImageResource(R.drawable.meatzza); // Meatzza image
                break;

            case "Build your own":
                currPizza = newYorkPizzaFactory.createBuildYourOwn();
                for (Topping topping : availableToppings) {
                    topping.setEditable(true); // Make all toppings editable
                }
                pizzaImageView.setImageResource(R.drawable.buildyourown); // Default image for Build Your Own
                break;

            default:
                currPizza = null;
                pizzaImageView.setImageResource(R.drawable.pinkpizza); // Default fallback image
                break;
        }

        crustTextView.setText("Crust: " + (currPizza != null ? currPizza.getCrust() : ""));

        // Reset size and price
        smallRadioButton.setChecked(false);
        mediumRadioButton.setChecked(false);
        largeRadioButton.setChecked(false);
        updatePrice();
    }

    private void highlightSpecialtyToppings(List<Topping> specialtyToppings) {
        for (Topping topping : availableToppings) {
            if (specialtyToppings.contains(topping)) {
                topping.setSelected(true); // Preselect these toppings
                topping.setEditable(false); // Lock these toppings to make them uneditable
                selectedToppingsAdapter.addTopping(topping); // Show them in the selected toppings list
            } else {
                topping.setSelected(false); // Ensure other toppings are not selected
            }
        }
        availableToppingsAdapter.notifyDataSetChanged(); // Refresh the UI
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
}

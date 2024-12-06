package com.example.rupizzaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newYorkButton = findViewById(R.id.button);
        newYorkButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewYorkActivity.class);
            startActivity(intent);
        });
        Button chicagoButton = findViewById(R.id.button2);
        chicagoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChicagoActivity.class);
            startActivity(intent);
        });
        Button cartButton = findViewById(R.id.button3);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        Button viewPastOrdersButton = findViewById(R.id.button4);
        viewPastOrdersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlacedOrdersActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.rupizzaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the cart_activity.xml layout file
        setContentView(R.layout.cart_activity);
        Button backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to MainActivity
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Closes the current activity
            }
        });
    }
}

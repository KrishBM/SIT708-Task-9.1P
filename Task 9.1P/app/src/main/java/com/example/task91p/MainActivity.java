package com.example.task91p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.task91p.activities.CreateAdvertActivity;
import com.example.task91p.activities.ItemListActivity;
import com.example.task91p.activities.MapActivity;

/**
 * Main entry point for the Lost & Found application.
 * This activity serves as the home screen with navigation options.
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private CardView cardCreateAdvert;
    private CardView cardShowItems;
    private CardView cardShowMap;
    private TextView tvTitle;
    private TextView tvSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        
        // Set the content view
        setContentView(R.layout.activity_main);
        
        // Configure window insets for edge-to-edge display
        configureWindowInsets();
        
        // Initialize UI components
        initializeComponents();
        
        // Set up click listeners
        configureClickListeners();
    }

    /**
     * Configures window insets for edge-to-edge display
     */
    private void configureWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Initializes all UI components
     */
    private void initializeComponents() {
        cardCreateAdvert = findViewById(R.id.card_create_advert);
        cardShowItems = findViewById(R.id.card_show_items);
        cardShowMap = findViewById(R.id.card_show_map);
        tvTitle = findViewById(R.id.tv_title);
        tvSubtitle = findViewById(R.id.tv_subtitle);
    }

    /**
     * Sets up click listeners for navigation cards
     */
    private void configureClickListeners() {
        // Navigate to create advert screen
        cardCreateAdvert.setOnClickListener(v -> {
            Intent createIntent = new Intent(MainActivity.this, CreateAdvertActivity.class);
            startActivity(createIntent);
        });

        // Navigate to item list screen
        cardShowItems.setOnClickListener(v -> {
            Intent listIntent = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(listIntent);
        });
        
        // Navigate to map view screen
        cardShowMap.setOnClickListener(v -> {
            Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(mapIntent);
        });
    }
}
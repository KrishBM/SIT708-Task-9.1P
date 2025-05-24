package com.example.task91p.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.task91p.R;
import com.example.task91p.database.DatabaseHelper;
import com.example.task91p.model.Item;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView tvItemName;
    private TextView tvDate;
    private TextView tvLocation;
    private TextView tvDescription;
    private TextView tvContact;
    private Button btnRemove;

    private DatabaseHelper dbHelper;
    private long itemId;
    private Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Initialize database helper
        dbHelper = DatabaseHelper.getInstance(this);
        
        // Get item ID from intent
        itemId = getIntent().getLongExtra("ITEM_ID", -1);
        
        if (itemId == -1) {
            Toast.makeText(this, "Error: Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize UI components
        initializeUI();
        
        // Load item details
        loadItemDetails();
        
        // Setup remove button
        setupRemoveButton();
    }

    private void initializeUI() {
        tvItemName = findViewById(R.id.tv_item_name);
        tvDate = findViewById(R.id.tv_date);
        tvLocation = findViewById(R.id.tv_location);
        tvDescription = findViewById(R.id.tv_description);
        tvContact = findViewById(R.id.tv_contact);
        btnRemove = findViewById(R.id.btn_remove);
    }

    private void loadItemDetails() {
        Cursor cursor = dbHelper.getItemById(itemId);
        
        if (cursor != null && cursor.moveToFirst()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            
            currentItem = new Item(itemId, type, name, phone, description, date, location);
            
            // Update UI with item details
            displayItemDetails();
            
            cursor.close();
        } else {
            Toast.makeText(this, "Error: Item details not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayItemDetails() {
        if (currentItem == null) return;
        
        String itemTypePrefix = currentItem.getType().equals("Lost") ? "Lost " : "Found ";
        tvItemName.setText(itemTypePrefix + currentItem.getName());
        tvDate.setText(currentItem.getDate());
        tvLocation.setText(currentItem.getLocation());
        tvDescription.setText(currentItem.getDescription());
        tvContact.setText(currentItem.getPhone());
    }

    private void setupRemoveButton() {
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove this item?")
                .setPositiveButton("Yes", (dialog, which) -> removeItem())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void removeItem() {
        int rowsAffected = dbHelper.deleteItem(itemId);
        
        if (rowsAffected > 0) {
            Toast.makeText(this, "Item removed successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to remove item", Toast.LENGTH_SHORT).show();
        }
    }
} 
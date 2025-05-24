package com.example.task91p.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task91p.R;
import com.example.task91p.adapter.ItemAdapter;
import com.example.task91p.database.DatabaseHelper;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Initialize database helper
        dbHelper = DatabaseHelper.getInstance(this);
        
        // Initialize UI components
        initializeUI();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load items from database
        loadItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload items when returning to this activity
        loadItems();
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.recycler_view_items);
        tvEmpty = findViewById(R.id.tv_empty);
    }

    private void setupRecyclerView() {
        adapter = new ItemAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItems() {
        Cursor cursor = dbHelper.getAllItems();
        
        if (cursor != null && cursor.getCount() > 0) {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.refreshItems(cursor);
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
} 
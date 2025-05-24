package com.example.task91p.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.task91p.R;
import com.example.task91p.database.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize database helper
        dbHelper = DatabaseHelper.getInstance(this);
        
        // Get the SupportMapFragment and request notification when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        
        // Display all items on the map
        displayItemsOnMap();
    }
    
    private void displayItemsOnMap() {
        Cursor cursor = dbHelper.getAllItems();
        
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No items to display on map", Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                cursor.close();
            }
            // Show default view even when no items
            LatLng australia = new LatLng(-25.2744, 133.7751);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(australia, 4));
            return;
        }
        
        // Create a LatLngBounds to contain all markers
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boolean hasValidCoordinates = false;
        
        try {
            // Iterate through all items in the database
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex("id");
                int typeIndex = cursor.getColumnIndex("type");
                int nameIndex = cursor.getColumnIndex("name");
                int descriptionIndex = cursor.getColumnIndex("description");
                int locationIndex = cursor.getColumnIndex("location");
                int latitudeIndex = cursor.getColumnIndex("latitude");
                int longitudeIndex = cursor.getColumnIndex("longitude");
                
                // Skip if any required column is not found
                if (idIndex == -1 || typeIndex == -1 || nameIndex == -1 || 
                        descriptionIndex == -1 || locationIndex == -1 || 
                        latitudeIndex == -1 || longitudeIndex == -1) {
                    continue;
                }
                
                long id = cursor.getLong(idIndex);
                String type = cursor.getString(typeIndex);
                String name = cursor.getString(nameIndex);
                String description = cursor.getString(descriptionIndex);
                String location = cursor.getString(locationIndex);
                double latitude = cursor.getDouble(latitudeIndex);
                double longitude = cursor.getDouble(longitudeIndex);
                
                // Skip if coordinates are default values (indicating no location data)
                if (latitude == 0 && longitude == 0) {
                    continue;
                }
                
                // Create marker for each item
                LatLng position = new LatLng(latitude, longitude);
                
                // Use different colors for lost and found items
                float markerColor = type.equals("Lost") 
                        ? BitmapDescriptorFactory.HUE_RED 
                        : BitmapDescriptorFactory.HUE_GREEN;
                        
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(type + ": " + name)
                        .snippet(description)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor));
                        
                map.addMarker(markerOptions);
                
                // Include this point in the bounds
                boundsBuilder.include(position);
                hasValidCoordinates = true;
            }
        } finally {
            // Ensure cursor is always closed
            cursor.close();
        }
        
        // Move camera to show all markers
        if (hasValidCoordinates) {
            LatLngBounds bounds = boundsBuilder.build();
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } else {
            // If no valid coordinates, show Australia as default
            LatLng australia = new LatLng(-25.2744, 133.7751);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(australia, 4));
            Toast.makeText(this, "No items with valid location data", Toast.LENGTH_SHORT).show();
        }
    }
} 
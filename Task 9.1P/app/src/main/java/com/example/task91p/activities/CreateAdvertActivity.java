package com.example.task91p.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.task91p.R;
import com.example.task91p.database.DatabaseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private RadioGroup radioGroupType;
    private RadioButton radioLost;
    private RadioButton radioFound;
    private EditText editName;
    private EditText editPhone;
    private EditText editDescription;
    private EditText editDate;
    private Button btnGetCurrentLocation;
    private Button btnSave;

    private Calendar calendar;
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    
    // Location data
    private String selectedPlaceName = "";
    private double selectedLatitude = 0;
    private double selectedLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize database helper
        dbHelper = DatabaseHelper.getInstance(this);
        
        // Initialize calendar for date picker
        calendar = Calendar.getInstance();
        
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        // Initialize places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        placesClient = Places.createClient(this);
        
        // Initialize UI components
        initializeUI();
        
        // Set up Places Autocomplete
        setupPlacesAutocomplete();
        
        // Set up date picker
        setupDatePicker();
        
        // Set up current location button
        setupCurrentLocationButton();
        
        // Set up save button click listener
        setupSaveButton();
    }

    private void initializeUI() {
        radioGroupType = findViewById(R.id.radio_group_type);
        radioLost = findViewById(R.id.radio_lost);
        radioFound = findViewById(R.id.radio_found);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editDescription = findViewById(R.id.edit_description);
        editDate = findViewById(R.id.edit_date);
        btnGetCurrentLocation = findViewById(R.id.btn_get_current_location);
        btnSave = findViewById(R.id.btn_save);
    }
    
    private void setupPlacesAutocomplete() {
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        
        // Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        
        // Set up a PlaceSelectionListener to handle the response
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                selectedPlaceName = place.getAddress();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    selectedLatitude = latLng.latitude;
                    selectedLongitude = latLng.longitude;
                }
            }
            
            @Override
            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                Toast.makeText(CreateAdvertActivity.this, 
                        "Place selection failed: " + status.getStatusMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        // Set hint text
        autocompleteFragment.setHint("Enter location");
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateAdvertActivity.this,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }
    
    private void setupCurrentLocationButton() {
        btnGetCurrentLocation.setOnClickListener(v -> requestCurrentLocation());
    }

    private void requestCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(this, location -> {
            if (location != null) {
                selectedLatitude = location.getLatitude();
                selectedLongitude = location.getLongitude();
                selectedPlaceName = "Current Location (" + selectedLatitude + ", " + selectedLongitude + ")";
                autocompleteFragment.setText(selectedPlaceName);
                
                Toast.makeText(CreateAdvertActivity.this, 
                        "Current location set", 
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateAdvertActivity.this, 
                        "Could not get current location, please try again", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        locationTask.addOnFailureListener(this, e -> {
            Toast.makeText(CreateAdvertActivity.this, 
                    "Error getting location: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        editDate.setText(sdf.format(calendar.getTime()));
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        // Get values from form
        String type = radioLost.isChecked() ? "Lost" : "Found";
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String date = editDate.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(date) ||
                TextUtils.isEmpty(selectedPlaceName)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to database with location coordinates
        long result = dbHelper.addItem(type, name, phone, description, date, selectedPlaceName, selectedLatitude, selectedLongitude);
        
        if (result != -1) {
            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to previous screen
        } else {
            Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
        }
    }
} 
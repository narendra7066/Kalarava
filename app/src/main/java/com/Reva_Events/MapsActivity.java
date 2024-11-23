package com.Reva_Events;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.Reva_Events.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    MarkerDetails event;
    TextView eventName,startDate,endDate ,startTime,description,endTime,eventType,location;
    private ActivityMapsBinding binding;
    boolean ispermitted=false;
    private List<Marker> allMarkers = new ArrayList<>();
    Location currlocation;
    String city = "Location Not found";
    FusedLocationProviderClient clientLocation;
    private final int ACCESS_FINE_CODE = 1;
    final int PERMISSION_REQUEST_CODE=1001;
    SupportMapFragment mapFragment;

    EditText searchedit;
    ImageView searchbtn;
    //Search is crashing the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clientLocation = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);




        mapFragment.getMapAsync(MapsActivity.this);
        searchedit = findViewById(R.id.searchView);
        searchbtn =  findViewById(R.id.imageView);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call search_loc method when the search button is clicked
                search_loc(searchedit, searchbtn);
            }
        });


    }

    private void search_loc(EditText searchedit, ImageView btn) {

        String userText = searchedit.getText().toString();

        // Filter markers based on user's input
        for (Marker marker : allMarkers) {
            String title = marker.getTitle();

            String snippet = marker.getSnippet();
            Log.d("ram","ram"+title+" ram "+snippet);
            if (title.contains(userText) || snippet.contains(userText)) {
                marker.setVisible(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15)); // Adjust the zoom level as needed

            } else {
                marker.setVisible(false);
            }
        }
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getlocation();
        getOnGoingEvents();



        //search_loc();
    }



    private  void getOnGoingEvents()
    {

        //Marking Hosted Events in Gmap
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Approved");
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the events
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    /* Get event details */
                    MarkerDetails event = eventSnapshot.getValue(MarkerDetails.class);
                    String title=eventSnapshot.getKey();
                    double latitude = event.getLatitude();
                    double longitude = event.getLongitude();
                    String description = event.getDescription();
                    String type=event.getEventType();
                    String eventName = event.getevent();
                    String startDate = event.getStartDate();
                    String startTime =event.getStartTime();
                    String endTime = event.getEndTime();
                    String endDate = event.getEndDate();


                    // Add marker for each event on the map

                    LatLng eventLocation = new LatLng(latitude, longitude);
                    int which_marker = R.drawable.user; // Default marker

                    if ("Hospitals".equals(type)) {
                        which_marker = R.drawable.hospital;
                    } else if ("Marriage".equals(type)) {
                        which_marker = R.drawable.marriage;
                    } else if ("Hackathon".equals(type)) {
                        which_marker = R.drawable.hack;
                    } else if ("Bank".equals(type)) {
                        which_marker = R.drawable.bank;
                    } else if ("Educational".equals(type)) {
                        which_marker = R.drawable.edu;
                    } else if ("Police".equals(type)) {
                        which_marker = R.drawable.police;
                    } else if ("Parking".equals(type)) {
                        which_marker = R.drawable.parking;
                    }

                    //finding place where the event is happening
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            city = address.getAddressLine(0);


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(eventLocation)
                            .title(event.getEventType())
                            .snippet("Event Name : "+eventName+", \nDate: " + startDate+" ,End Date : " +endDate+", \nTime: " + startTime  +" ,EndTime : "+endTime+", \nDescription : "+description +", \nLocation : "+city)
                            .icon(BitmapDescriptorFactory.fromResource(which_marker))); // You can customize the marker icon as needed
                    allMarkers.add(marker);

                    // Inside your method where you add markers to the map (e.g., getOnGoingEvents method)
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            // Show the dialog when the info window of the marker is clicked
                            showDialogForMarker(marker);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(MapsActivity.this, "Failed to retrieve events: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void showDialogForMarker(Marker marker) {
        // Retrieve the relevant information from the marker
        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        // Create and show a dialog with the marker information
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(snippet)
                .setPositiveButton("OK", null) // Add your dialog buttons or functionality here
                .show();
    }


    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void getlocation() {
        // Check location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            if (!MapsActivity.isGPSEnabled(this)) {
                // Location permission granted
                showLocationTurnDialog();
            }
            // Get user's last known location
            clientLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Use the location object to get latitude and longitude
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng userLocation = new LatLng(latitude, longitude);
                        // Move the camera to the user's location
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
                        // Add a marker at the user's location
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("You are Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
                    } else {
                        // Unable to retrieve location, show a message or handle it as needed
                        Toast.makeText(MapsActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Location retrieval failed
                    Toast.makeText(MapsActivity.this, "Location retrieval failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void showLocationTurnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location services are disabled. Do you want to enable them?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Open location settings
                        Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(enableLocationIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
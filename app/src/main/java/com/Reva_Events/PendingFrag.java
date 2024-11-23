package com.Reva_Events;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PendingFrag extends Fragment implements recyclerContact.OnMoreInfoClickListener {

    recyclerContact adapter;
    ProgressDialog progressDialog;
FirebaseDatabase database;
DatabaseReference reference;
View view;

FirebaseAuth auth;
    ArrayList<MarkerDetails> list;
    TextView eventName,startDate,endDate ,startTime,description,endTime,eventType,location;
    TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public PendingFrag() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Pending");
        view = inflater.inflate(R.layout.fragment_pending, container, false);

        databaseworkAdapterwork(view,reference,database);
        return view;

    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onMoreInfoClicked(int position) {
        Log.d("ram","showdialog");
        Log.d("ram","showdialog"+position);
        MarkerDetails item = list.get(position);
        Log.d("ram","showdialog"+item);

        showDialog(item,position);

    }

    public void showDialog(MarkerDetails item,int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.approvedialog, null);
        builder.setView(dialog);

        builder.setCancelable(false);

        //finding the id of the dialog box
       eventName = dialog.findViewById(R.id.editTextText);
        endTime = dialog.findViewById(R.id.editTextText4);
        eventType = dialog.findViewById(R.id.textView2);
        startDate = dialog.findViewById(R.id.editTextText2);
        endDate = dialog.findViewById(R.id.editTextText8);
        startTime = dialog.findViewById(R.id.editTextText9);
        description = dialog.findViewById(R.id.editTextDescription);
        location =dialog.findViewById(R.id.location);

        //getting latitide and location
        Double latitude = item.getLatitude();
        Double longitude = item.getLongitude();
        String city = "Location Not found";
        //finding place where the event is happening
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                city = address.getAddressLine(0);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //retriving the deatils from pending database
        String eventType1 = item.getEventType();
        String eventName1= item.getevent();
        String startDate1 = item.getStartDate();
        String startTime1 = item.getStartTime();
        String endDate1 = item.getEndDate();
        String endTime1 = item.getEndTime();
        String description1 = item.getDescription();


        //seting the text in dialog box

        eventType.setText(item.getEventType());
        eventName.setText(" Event : "+item.getevent());
        startDate.setText(item.getStartDate());
        endDate.setText(item.getEndDate());
        startTime.setText(item.getStartTime());
        endTime.setText(item.getEndTime());
        String markerID = item.getMarkerDrawableId();
        description.setText(item.getDescription());
        location.setText(city);
        LatLng latLng = new LatLng(latitude, longitude);




        builder.setPositiveButton("Approve", null);

// Set negative button click listener
        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    if (markerID != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("Reject Request for " + eventName1)
                                .setMessage("Do You Want to Reject this Event for sure")
                                .setIcon(R.drawable.reject)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Delete marker from Firebase
                                        deleteMarkerFromFirebase(markerID);
                                        // Show progress dialog
                                        progressDialog = ProgressDialog.show(getActivity(), "Setting Up", "Please wait...", false);



                                        // Dismiss the dialog after a delay
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Dismiss the progress dialog
                                                if (progressDialog != null && progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                                Log.d("ram","krishna");
                                                dialogInterface.cancel();

                                                // Update RecyclerView
                                                databaseworkAdapterwork(view, reference, database);
                                            }
                                        }, 20000);


                                        // Dismiss the approval dialog

                                        storeMarkerDetailsInFirebaseRejected(latLng, eventType1, eventName1, startDate1, startTime1, endDate1, endTime1, description1);
//                                                list.remove(position);
//                                                adapter.notifyItemChanged(position);



                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });


                        builder.show();
                    }else {
                        Log.d("ram","exception");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
        });

// Create the AlertDialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        // Validate inputs

                        try {
                            if (markerID != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                        .setTitle("Approve Request for " + eventName1)
                                        .setMessage("Do You Want to Approve  this for sure" )
                                        .setIcon(R.drawable.apprived)
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Delete marker from Firebase
                                                deleteMarkerFromFirebase(markerID);
                                                alertDialog.dismiss();
                                                // Show progress dialog
                                                progressDialog = ProgressDialog.show(getActivity(), "Setting Up", "Please wait...", false);



                                                // Dismiss the dialog after a delay
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // Dismiss the progress dialog
                                                        if (progressDialog != null && progressDialog.isShowing()) {
                                                            progressDialog.dismiss();
                                                        }
                                                        Log.d("ram","krishna");


                                                        // Update RecyclerView
                                                        databaseworkAdapterwork(view, reference, database);
                                                    }
                                                }, 20000);


                                                // Dismiss the approval dialog

                                                storeMarkerDetailsInFirebaseApproved(latLng, eventType1, eventName1, startDate1, startTime1, endDate1, endTime1, description1);
//                                                list.remove(position);
//                                                adapter.notifyItemChanged(position);



                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });


                                builder.show();
                            }else {
                                Log.d("ram","exception");
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

// Show the dialog
        alertDialog.show();


    }

    private void storeMarkerDetailsInFirebaseApproved(LatLng latLng,String eventType,String eventName,String startDate,String startTime,String endDate,String endTime,String description) {
        // Store marker details in Firebase Realtime Database
        DatabaseReference markersRef = FirebaseDatabase.getInstance().getReference("Approved");
        String markerId = markersRef.push().getKey();

        if (markerId != null) {
            MarkerDetails markerDetails = new MarkerDetails(markerId,latLng.latitude, latLng.longitude, eventType, eventName, startDate, startTime, endDate, endTime, description);
            markersRef.child(markerId).setValue(markerDetails);
        }

//        Bundle bundle = new Bundle();
//
//        bundle.putString("eventName",eventName);



    }
    private void storeMarkerDetailsInFirebaseRejected(LatLng latLng,String eventType,String eventName,String startDate,String startTime,String endDate,String endTime,String description) {
        // Store marker details in Firebase Realtime Database
        DatabaseReference markersRef = FirebaseDatabase.getInstance().getReference("Reject");
        String markerId = markersRef.push().getKey();

        if (markerId != null) {
            MarkerDetails markerDetails = new MarkerDetails(markerId,latLng.latitude, latLng.longitude, eventType, eventName, startDate, startTime, endDate, endTime, description);
            markersRef.child(markerId).setValue(markerDetails);
        }




//        Bundle bundle = new Bundle();
//
//        bundle.putString("eventName",eventName);



    }
    private void deleteMarkerFromFirebase(String markerId) {
        // Get reference to the marker in Firebase Realtime Database
        DatabaseReference markerRef = FirebaseDatabase.getInstance().getReference("Pending").child(markerId);

        // Delete the marker from Firebase
        markerRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Marker deleted successfully from Firebase
                   // Toast.makeText(getActivity(), "Marker deleted successfully from Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure to delete marker from Firebase
                  //  Toast.makeText(getActivity(), "Failed to delete marker from Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void databaseworkAdapterwork(View view , DatabaseReference reference , FirebaseDatabase database)
    {
        list = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new recyclerContact(getActivity(),list,this);
        recyclerView.setAdapter(adapter);


        //database work

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and whenever data at this location is updated.
                //  MarkerDetails model = dataSnapshot.getValue(MarkerDetails.class);
//////////////////////adding the data from database to list
                for (DataSnapshot Snapshot: dataSnapshot.getChildren())
                {
                    MarkerDetails eventDetails = Snapshot.getValue(MarkerDetails.class);
                    list.add(eventDetails);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //System.out.println("Failed to read value." + error.toException());
                Log.d("event","Failed to retrive the data");
            }
        });




    }

}
package com.Reva_Events;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ApproveFrag extends Fragment implements recyclerContact.OnMoreInfoClickListener {

    recyclerContact adapter;
    ArrayList<MarkerDetails> list2;
    TextView eventName,startDate,endDate ,startTime,description,endTime,eventType,location;

    public ApproveFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Reference to your data
        // DatabaseReference myRef = database.getReference().child("Google markers");//.child(firebaseAuth.getUid());
        //  String refID ="kara";
// rettrive the data from database
        // Read data
        DatabaseReference reference = database.getReference().child("Approved");


        View view = inflater.inflate(R.layout.fragment_approve, container, false);
        list2 = new ArrayList<>();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new recyclerContact(getActivity(),list2,this);
        recyclerView.setAdapter(adapter);


        //Approve database work

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and whenever data at this location is updated.
                //  MarkerDetails model = dataSnapshot.getValue(MarkerDetails.class);
//////////////////////adding the data from database to list
                for (DataSnapshot Snapshot: dataSnapshot.getChildren())
                {
                    MarkerDetails eventDetails = Snapshot.getValue(MarkerDetails.class);
                    list2.add(eventDetails);
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









        return view;
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_pending, container, false);
        // Inflate the layout for this fragment

    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onMoreInfoClicked(int position) {
        Log.d("ram","showdialog");
        MarkerDetails item = list2.get(position);
        showDialog(item);
    }
    public void showDialog(MarkerDetails item)
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





        //seting the text in dialog box

        eventType.setText(item.getEventType());
        eventName.setText(" Event : "+item.getevent());
        startDate.setText(item.getStartDate());
        endDate.setText(item.getEndDate());
        startTime.setText(item.getStartTime());
        endTime.setText(item.getEndTime());
        description.setText(item.getDescription());
        location.setText(city);

        LatLng latLng = new LatLng(latitude, longitude);





        builder.setPositiveButton("Cancel", null);

// Set negative button click listener

// Create the AlertDialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

// Show the dialog
        alertDialog.show();


    }
}
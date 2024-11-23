package com.Reva_Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    Button login;
    final String [] roles={"null","Admin","Guest","Organiser"};
    TextInputLayout userId,userpswd;
    Spinner spin2;
    String selectedRole;

    FirebaseDatabase myFire;
    DatabaseReference myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.log_in);
        spin2=findViewById(R.id.spinner2);
        userId=(TextInputLayout) findViewById(R.id.logmail);
        userpswd=(TextInputLayout)findViewById(R.id.logpswrd);
        myFire=FirebaseDatabase.getInstance();


        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter);
        spin2.setOnItemSelectedListener(this);


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String logname = userId.getEditText().getText().toString().trim();
                String logpaswd = userpswd.getEditText().getText().toString().trim();
                switch (selectedRole) {
                    case "Admin":
                        myDb=myFire.getReference("Admin");
                        myDb.orderByChild("mail").equalTo(logname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Email exists in the database
                                    // Now, iterate through the dataSnapshot to find the user with the given email
                                    boolean passwordMatched = false; // Flag to indicate if password matched
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        // Retrieve the user's password from the database
                                        String passwordFromDb = snapshot.child("paswd").getValue(String.class);
                                        // Check if the retrieved password matches the one provided by the user
                                        if (passwordFromDb != null && passwordFromDb.equals(logpaswd)) {
                                            // Password matches, authentication successful
                                            passwordMatched = true;
                                            break; // Exit the loop as authentication is successful
                                        }
                                    }
                                    if (passwordMatched) {
                                        Toast.makeText(Login.this, "Credentials Matched !!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(Login.this, AdminsView.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(Login.this, "Password Not matched", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Email does not exist in the database
                                    Toast.makeText(Login.this, "Invalid email.Email does not Exists", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any errors
                                Toast.makeText(Login.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;

                    case"Guest":
                        myDb=myFire.getReference("Guest");
                        myDb.orderByChild("mail").equalTo(logname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Email exists in the database
                                    // Now, iterate through the dataSnapshot to find the user with the given email
                                    boolean passwordMatched = false; // Flag to indicate if password matched
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        // Retrieve the user's password from the database
                                        String passwordFromDb = snapshot.child("paswd").getValue(String.class);
                                        // Check if the retrieved password matches the one provided by the user
                                        if (passwordFromDb != null && passwordFromDb.equals(logpaswd)) {
                                            // Password matches, authentication successful
                                            passwordMatched = true;
                                            break; // Exit the loop as authentication is successful
                                        }
                                    }
                                    if (passwordMatched) {
                                        Toast.makeText(Login.this, "Credentials Matched !!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(Login.this, MapsActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(Login.this, "Password Not matched", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Email does not exist in the database
                                    Toast.makeText(Login.this, "Invalid email.Email does not Exists", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any errors
                                Toast.makeText(Login.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                            break;
                    case"Organiser":
                        myDb=myFire.getReference("Organiser");
                        myDb.orderByChild("mail").equalTo(logname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Email exists in the database
                                    // Now, iterate through the dataSnapshot to find the user with the given email
                                    boolean passwordMatched = false; // Flag to indicate if password matched
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        // Retrieve the user's password from the database
                                        String passwordFromDb = snapshot.child("paswd").getValue(String.class);
                                        // Check if the retrieved password matches the one provided by the user
                                        if (passwordFromDb != null && passwordFromDb.equals(logpaswd)) {
                                            // Password matches, authentication successful
                                            passwordMatched = true;
                                            break; // Exit the loop as authentication is successful
                                        }
                                    }
                                    if (passwordMatched) {
                                        Toast.makeText(Login.this, "Credentials Matched !!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(Login.this, OrganizeMapper.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(Login.this, "Password Not matched", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Email does not exist in the database
                                    Toast.makeText(Login.this, "Invalid email.Email does not Exists", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any errors
                                Toast.makeText(Login.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                            break;
                    default:
                        Toast.makeText(Login.this, "Roles can not be Null", Toast.LENGTH_SHORT).show();
                        break;



                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRole=roles[position];
        Toast.makeText(this, "selected : "+roles[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
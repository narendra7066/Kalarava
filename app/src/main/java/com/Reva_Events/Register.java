package com.Reva_Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import  android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final String[] roles= {"Null","Admin","Guest","Organiser"};
    Button register,signIn;
    Spinner spin1;
    String Role = "";
    TextInputLayout Uname,Umail,Upassword,Uconfirmpswd,Umob;
    Spinner role;
    FirebaseDatabase fireDb;
    //FirebaseAuth myAuth;
    DatabaseReference Dbrefer;
    FirebaseAuth myauth;
    EditText key, passwordadmin;
    Button submit, cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.regi);
        signIn = findViewById(R.id.sign_In);
        Uname = (TextInputLayout) findViewById(R.id.name);
        Umail = (TextInputLayout) findViewById(R.id.mail);
        Upassword = (TextInputLayout) findViewById(R.id.pswrd);
        Uconfirmpswd = (TextInputLayout) findViewById(R.id.cnfrpswrd);
        Umob = (TextInputLayout) findViewById(R.id.mob);
        spin1 = findViewById(R.id.spinner1);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                // Internet connection is available
                // You can proceed with your app logic
            } else {
                // No internet connection
                // Display an error message
                Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin1.setAdapter(adapter);
            spin1.setOnItemSelectedListener(this);

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Register.this, Login.class);
                    startActivity(i);
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate_Data();
                }
            });
        }
    }

    private void validate_Data()
    {
        String name=Uname.getEditText().getText().toString().trim();
        String email=Umail.getEditText().getText().toString();
        String pswd=Upassword.getEditText().getText().toString();
        String cfmr=Uconfirmpswd.getEditText().getText().toString();
        String mnum=Umob.getEditText().getText().toString();

        //Login Sound

        if(name.isEmpty()) {
            Uname.setError("Field can't be empty..");
        } else if (!name.matches("^[a-zA-Z]+")) {
            Uname.setError("Numbers are not Allowed!!");
        } else if (name.length()<3) {
            Uname.setError("Length should be atleast 3");
        }
        else {
            Uname.setError(null);
        }

        if(email.isEmpty()) {
            Umail.setError("Field can't be empty..");
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            Umail.setError("Invalid mail...");
        }
        else
        {
            Umail.setError(null);
        }

        if(pswd.isEmpty())
        {
            Upassword.setError("Field can't be empty..");
        } else if (pswd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
            Upassword.setError("Password is Strong");
        } else if (pswd.length()<6) {
            Upassword.setError("Password length should be atleast 6");
        }
        else {
            Upassword.setError(null);
        }

        if(cfmr.isEmpty()) {
            Uconfirmpswd.setError("Field can't be empty..");
        } else if (!cfmr.equals(pswd)) {
            Uconfirmpswd.setError("password does not match with original one");
        }
        else {
            Uconfirmpswd.setError(null);
        }

        if(mnum.isEmpty())
        {
            Umob.setError("Field can't be empty..");
        } else if (!mnum.matches("^[6-9][0-9]{9}$")) {
            Umob.setError("Invalid mobile number...");
        }  else {
            Umob.setError(null);
        }

        if(Uname.getError()==null && Umail.getError()==null && Upassword.getError()==null && Uconfirmpswd.getError()==null && Umob.getError()==null) {

            Users user=new Users(name,email,pswd,cfmr,mnum);
            fireDb=FirebaseDatabase.getInstance(); //Creating instance of Firebase DB
            switch (Role)
            {
                case "Admin":

                    Dbrefer=fireDb.getReference("Admin"); //Creating Database Refernces
                    Dbrefer.orderByChild("mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email already exists in the database
                                Toast.makeText(Register.this, "Admin Email already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email does not exist in the database, proceed with registration
                                dialog(name, user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Register.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    });

                    break;
                case "Guest":
                    Dbrefer=fireDb.getReference("Guest"); //Creating Database Refernces
                    Dbrefer.orderByChild("mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email already exists in the database
                                Toast.makeText(Register.this, "Guest Email already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email does not exist in the database, proceed with registration
                                addGuestDetails(name,user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Register.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    });
                    break;
                case "Organiser":
                    Dbrefer=fireDb.getReference("Organiser"); //Creating Database Refernces
                    Dbrefer.orderByChild("mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email already exists in the database
                                Toast.makeText(Register.this, "Organiser Email already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Email does not exist in the database, proceed with registration

                                addOrganiserDetails(name,user);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Register.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    });
                    break;
                default:
                    Toast.makeText(this, "Role Can not be Null !!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void addAdminDetails(String name,Users user)
    {

        Dbrefer.child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Register.this, "Storing Admin Data.....", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this,"Registration Successfull !!!!",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Register.this,Login.class);
                        startActivity(i);
                    }
                },10000 );

            }
        });
    }


    private  void addGuestDetails(String name,Users user)
    {
        Dbrefer.child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Register.this, "Storing Guest Data.....", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this,"Registration Successfull !!!!",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Register.this,Login.class);
                        startActivity(i);
                    }
                },10000 );

            }
        });
    }

    private void addOrganiserDetails(String name,Users user)
    {
        Dbrefer.child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Register.this, "Storing Organiser Data.....", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this,"Registration Successfull !!!!",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Register.this,Login.class);
                        startActivity(i);
                    }
                },10000 );

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Role=roles[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void dialog(String name , Users user)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialog = inflater.inflate(R.layout.cust_dialog, null);
        builder.setView(dialog);
        builder.setCancelable(false);

// Initialize Views
        key =dialog.findViewById(R.id.security);
        passwordadmin = dialog.findViewById(R.id.admin);




// Set positive button click listener
        builder.setPositiveButton("confirm", null);

// Set negative button click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
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
                        String securitykey = key.getText().toString();
                        String passadmin = passwordadmin.getText().toString();

                        if(TextUtils.isEmpty(securitykey))
                        {
                            key.setError("Please Enter Security Key");
                            Toast.makeText(Register.this, "Please Enter Security Key", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(passadmin))
                        {
                            passwordadmin.setError("please Enter Admin Password");
                            Toast.makeText(Register.this, "please Enter Admin password", Toast.LENGTH_SHORT).show();
                        }else if(!securitykey.trim().equals("Ram@369") || !passadmin.trim().equals("admin@123"))
                        {
                            key.setError("Please Enter Correct Key");

                            passwordadmin.setError("Please Enter the Correct Password");
                            Toast.makeText(Register.this, "Please Enter the Correct Cardentials for Admin Register", Toast.LENGTH_SHORT).show();
                        } else {
                            addAdminDetails(name,user);
                            alertDialog.dismiss();

                        }


                    }
                });
            }
        });
        alertDialog.show();
    }



}
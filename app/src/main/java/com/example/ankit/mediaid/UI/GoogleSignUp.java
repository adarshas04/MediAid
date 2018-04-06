package com.example.ankit.mediaid.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.ankit.mediaid.Models.User;
import com.example.ankit.mediaid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class GoogleSignUp extends AppCompatActivity {

    Spinner user_type;
    EditText store_name;
    Button sign_up;
    LinearLayout store_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);

        user_type = findViewById(R.id.user_types);
        store_name = findViewById(R.id.store_name);
        store_location = findViewById(R.id.store_location);
        sign_up = findViewById(R.id.sign_up);


        user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String med = String.valueOf(parent.getItemAtPosition(position));
                if(med.equals("Medical Store"))
                {
                    store_name.setVisibility(View.VISIBLE);
                    store_location.setVisibility(View.VISIBLE);
                }
                else
                {
                    store_name.setVisibility(View.GONE);
                    store_location.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                User u1 = new User();
                u1.setName(user.getDisplayName());
                u1.setEmail(user.getEmail());
                u1.setPhone(user.getPhoneNumber());
                u1.setStore_location("Ankit");
                u1.setStore_name("Ankit");
                u1.setImageurl(user.getPhotoUrl().toString());
                FirebaseDatabase.getInstance().getReference().child("user").setValue(u1);
            }
        });


    }
}

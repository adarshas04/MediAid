package com.example.ankit.mediaid.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.Models.Quantity;
import com.example.ankit.mediaid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Edit_Meds extends AppCompatActivity {

    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meds);

        Button b_edit = findViewById(R.id.b_edit);
        final EditText name = findViewById(R.id.name);
        final EditText description = findViewById(R.id.description);
        final EditText price = findViewById(R.id.price);
        final EditText quantity = findViewById(R.id.quantity);

        db = FirebaseDatabase.getInstance().getReference();
        final String user_id = FirebaseAuth.getInstance().getUid();

        final String id = getIntent().getStringExtra("id");
        db.child("meds").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meds m = dataSnapshot.getValue(Meds.class);
                name.setText(m.getName());
                description.setText(m.getDescrition());
                db.child("inventory").child(id).child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Quantity q = dataSnapshot.getValue(Quantity.class);
                        price.setText(q.getPrice());
                        quantity.setText(q.getQuantity());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meds m = new Meds();
                m.setName(name.getText().toString());
                m.setDescrition(description.getText().toString());
                m.setMed_id(id);
                Quantity q = new Quantity();
                q.setPrice(price.getText().toString());
                q.setQuantity(quantity.getText().toString());
                db.child("meds").child(id).setValue(m);
                db.child("inventory").child(id).child(user_id).setValue(q);
            }
        });
    }
}

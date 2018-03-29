package com.example.ankit.mediaid.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.Models.Quantity;
import com.example.ankit.mediaid.Models.Qw;
import com.example.ankit.mediaid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity {

    ListView list;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        list = findViewById(R.id.list_view);

        db = FirebaseDatabase.getInstance().getReference();
        final ArrayList<String> cart_meds = new ArrayList<>();
        //final HashMap<String, Quantity> meMap=new HashMap<String, Quantity>();
        db.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("yo","asasasas "+ dataSnapshot);
                cart_meds.clear();
               for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Log.i("yo","qwqwq"+ ds);
                    db.child("meds").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Meds m = dataSnapshot.getValue(Meds.class);
                            cart_meds.add(m.getName());
                            list.setAdapter(new ArrayAdapter<>(Cart.this, android.R.layout.simple_list_item_1, cart_meds));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Quantity q = ds.getValue(Quantity.class);
                    /*Qw q = ds.getValue(Qw.class);
                    Log.i("yo", " asasa"+ q.getQuantity());*/
                    /*Quantity q = d.getValue(Quantity.class);
                    meMap.put(ds.getValue(String.class),q);*/
                    //cart_meds.add(ds.getValue(String.class));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

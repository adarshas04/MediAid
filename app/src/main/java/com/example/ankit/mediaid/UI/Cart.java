package com.example.ankit.mediaid.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.mediaid.CartListViewAdapter;
import com.example.ankit.mediaid.Fragment.Store_Med_Billing;
import com.example.ankit.mediaid.ListViewAdaptor;
import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.Models.Quantity;
import com.example.ankit.mediaid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Cart extends AppCompatActivity {

    ListView list;
    ArrayList<Meds> cart_meds = new ArrayList<>();
    CartListViewAdapter adapter;
    DatabaseReference db;
    EditText inputSearch;
    private Integer t_price=0;
    private Integer t_quantiity = 0;
    TextView t_item;
    LinearLayout checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        list = findViewById(R.id.list_view);
        inputSearch = findViewById(R.id.inputSearch);
        t_item = findViewById(R.id.t_item);
        checkout = findViewById(R.id.checkout);


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                try {
                    Cart.this.adapter.getFilter().filter(cs);
                }catch (Exception e)
                {
                    Log.i("ex",e.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        db = FirebaseDatabase.getInstance().getReference();
        db.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("yo","asasasas "+ dataSnapshot);
                cart_meds.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Log.i("ankit2", ds.getKey());
                    Quantity q = ds.getValue(Quantity.class);
                    q.getQuantity();
                    q.getPrice();
                    t_price += Integer.parseInt(q.getPrice())*Integer.parseInt(q.getQuantity());
                    t_quantiity += Integer.parseInt(q.getQuantity());
                    db.child("meds").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("qwe"," "+ dataSnapshot.getValue());
                            Meds med = dataSnapshot.getValue(Meds.class);
                            Log.i("qwe"," "+ med);
                            if(med != null)
                            {
                                cart_meds.add(med);
                                Log.i("qwe", "asa"+ cart_meds);
                                Collections.sort(cart_meds, new Comparator<Meds>() {
                                    @Override
                                    public int compare(Meds meds, Meds t1) {
                                        return meds.getName().compareTo(t1.getName());
                                    }
                                });
                                adapter = new CartListViewAdapter(Cart.this, R.layout.cart_list, cart_meds);
                                list.setAdapter(adapter);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if(t_price != 0)
                    t_item.setText("Total item:-"+t_quantiity+"|  Total price:-"+t_price);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Cart.this,"Order is Placed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

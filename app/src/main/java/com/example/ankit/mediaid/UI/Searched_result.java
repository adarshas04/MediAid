package com.example.ankit.mediaid.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.Models.Quantity;
import com.example.ankit.mediaid.Models.User;
import com.example.ankit.mediaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Searched_result extends AppCompatActivity {

    ListView lv1,lv2,lv3,lv4,lv5,lv6;
    ArrayAdapter adapter;
    TextView t1,t2,t3,t4,t5,t6;
    DatabaseReference db;
    ProgressBar p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_result);

        db= FirebaseDatabase.getInstance().getReference();

        ArrayList<TextView> text = new ArrayList<>();
        final ArrayList<ListView> list = new ArrayList<>();
        ArrayList<String> s = getIntent().getStringArrayListExtra("req");

        p1 = findViewById(R.id.pbHeaderProgress);

        t1=findViewById(R.id.t1);text.add(t1);
        t2=findViewById(R.id.t2);text.add(t2);
        t3=findViewById(R.id.t3);text.add(t3);
        t4=findViewById(R.id.t4);text.add(t4);
        t5=findViewById(R.id.t5);text.add(t5);
        t6=findViewById(R.id.t6);text.add(t6);

        lv1=findViewById(R.id.lv1);list.add(lv1);
        lv2=findViewById(R.id.lv2);list.add(lv2);
        lv3=findViewById(R.id.lv3);list.add(lv3);
        lv4=findViewById(R.id.lv4);list.add(lv4);
        lv5=findViewById(R.id.lv5);list.add(lv5);
        lv6=findViewById(R.id.lv6);list.add(lv6);


        for(int i=0;i<s.size();i++)
        {
            text.get(i).setText(s.get(i));
            final ArrayList<String> store_name = new ArrayList<>();
            final int finalI = i;
            db.child("meds").orderByChild("name").equalTo(s.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("ankityo"," "+ dataSnapshot);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Meds temp= ds.getValue(Meds.class);
                        db.child("inventory").child(temp.getMed_id()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i("ankityo"," "+ dataSnapshot);
                                for(final DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    Quantity t = ds.getValue(Quantity.class);
                                    Log.i("ankityo"," "+ t+" "+ds.getKey());
                                    db.child("user").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.i("ankityo"," "+ dataSnapshot);
                                            User t1 = dataSnapshot.getValue(User.class);
                                            store_name.add(t1.getStore_name());
                                            p1.setVisibility(View.GONE);
                                            adapter = new ArrayAdapter<>(Searched_result.this, android.R.layout.simple_list_item_1, store_name);
                                            list.get(finalI).setAdapter(adapter);
                                            Log.i("yo","kl");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

}

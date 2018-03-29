package com.example.ankit.mediaid.Fragment;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.R;
import com.example.ankit.mediaid.UI.Searched_result;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ankit on 3/12/2018.
 */

public class Search_med extends Fragment {

    View root;

    ArrayList<String> medName = new ArrayList<>();

    TextView t1;
    AutoCompleteTextView e1,e2,e3,e4,e5,e6;
    Button b1;
    DatabaseReference db;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_search_med, container, false);

        e1=root.findViewById(R.id.e1);
        e2=root.findViewById(R.id.e2);
        e3=root.findViewById(R.id.e3);
        e4=root.findViewById(R.id.e4);
        e5=root.findViewById(R.id.e5);
        e6=root.findViewById(R.id.e6);
        t1=root.findViewById(R.id.t1);
        b1=root.findViewById(R.id.b1);



        db = FirebaseDatabase.getInstance().getReference();

        db.child("meds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Meds meds=ds.getValue(Meds.class);
                    medName.add(meds.getName());
                }
                Collections.sort(medName);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, medName);
                e1.setAdapter(adapter);
                e2.setAdapter(adapter);
                e3.setAdapter(adapter);
                e4.setAdapter(adapter);
                e5.setAdapter(adapter);
                e6.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(e2.getVisibility() == View.VISIBLE)
                {
                    if(e3.getVisibility() == View.VISIBLE)
                    {
                        if(e4.getVisibility() == View.VISIBLE)
                        {
                            if(e5.getVisibility() == View.VISIBLE)
                            {
                                if(e6.getVisibility() == View.VISIBLE)
                                {
                                    Toast.makeText(getContext(),"You can't Add more Meds",Toast.LENGTH_LONG).show();
                                }else {e6.setVisibility(View.VISIBLE);e6.requestFocus();}
                            }else {e5.setVisibility(View.VISIBLE);e5.requestFocus();}
                        }else {e4.setVisibility(View.VISIBLE);e4.requestFocus();}
                    }else {e3.setVisibility(View.VISIBLE);e3.requestFocus();}
                }else {e2.setVisibility(View.VISIBLE);e2.requestFocus();}
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),Searched_result.class);
                ArrayList<String> req = new ArrayList<>();
                req.add(e1.getText().toString());
                if(e2.getVisibility() == View.VISIBLE) {
                    req.add(e2.getText().toString());
                    if (e3.getVisibility() == View.VISIBLE) {
                        req.add(e3.getText().toString());
                        if (e4.getVisibility() == View.VISIBLE) {
                            req.add(e4.getText().toString());
                            if (e5.getVisibility() == View.VISIBLE) {
                                req.add(e5.getText().toString());
                                if (e6.getVisibility() == View.VISIBLE) {
                                    req.add(e6.getText().toString());
                                }
                            }
                        }
                    }
                }

                i.putStringArrayListExtra("req",req);
                startActivity(i);
            }
        });

        return  root;
    }
}

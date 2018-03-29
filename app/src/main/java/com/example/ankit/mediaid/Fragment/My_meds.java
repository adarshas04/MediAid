package com.example.ankit.mediaid.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ankit.mediaid.ListViewAdaptor;
import com.example.ankit.mediaid.Models.Meds;
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

/**
 * Created by ankit on 3/13/2018.
 */

public class My_meds extends Fragment {

    View root;
    ListView list;
    DatabaseReference db;
    ArrayList<Meds> medName = new ArrayList<>();
    ListViewAdaptor adapter;
    EditText inputSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_meds, container, false);

        list = root.findViewById(R.id.list_view);
        inputSearch = root.findViewById(R.id.inputSearch);
        db = FirebaseDatabase.getInstance().getReference();

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                My_meds.this.adapter.getFilter().filter(cs);
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

        adapter = new ListViewAdaptor(getContext(), R.layout.list_row, medName);
        list.setAdapter(adapter);

        db.child("store_inventory").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Log.i("qwee", " " + ds);
                    db.child("meds").child(ds.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("qwe"," "+ dataSnapshot.getValue());
                            Meds med = dataSnapshot.getValue(Meds.class);
                            Log.i("qwe"," "+ med);
                            if(med != null)
                            {
                                medName.add(med);
                                Log.i("qwe", "asa"+ medName);
                                Collections.sort(medName, new Comparator<Meds>() {
                                    @Override
                                    public int compare(Meds meds, Meds t1) {
                                        return meds.getName().compareTo(t1.getName());
                                    }
                                });
                                adapter.notifyDataSetChanged();
                                /*try {

                                }catch (Exception e)
                                {
                                    Log.i("ankur",e.toString());
                                }*/
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

        return root;
    }

}

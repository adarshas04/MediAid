package com.example.ankit.mediaid.Fragment;

import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ListView;

import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.MyLIstViewAdapter;
import com.example.ankit.mediaid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ankit on 3/14/2018.
 */

public class Med_billing extends Fragment {

    View root;
    EditText inputSearch;
    ListView list;
    DatabaseReference db;
    ArrayList<Meds> medName = new ArrayList<>();
    MyLIstViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_my_meds, container, false);


        inputSearch = root.findViewById(R.id.inputSearch);
        list = root.findViewById(R.id.list_view);

        db = FirebaseDatabase.getInstance().getReference();

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                try {
                    Med_billing.this.adapter.getFilter().filter(cs);
                } catch (Exception e) {
                    Log.i("ex", e.toString());
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

        adapter = new MyLIstViewAdapter(getContext(), R.layout.billing_list, medName);
        list.setAdapter(adapter);

        db.child("meds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Meds meds = ds.getValue(Meds.class);
                    if (meds != null) {
                        medName.add(meds);
                    }
                }
                Log.i("qwasa", medName + " ");
                Collections.sort(medName, new Comparator<Meds>() {
                    @Override
                    public int compare(Meds meds, Meds t1) {
                        return meds.getName().compareTo(t1.getName());
                    }
                });
                adapter.notifyDataSetChanged();
               /* try {
                    adapter = new MyLIstViewAdapter(getContext(), R.layout.billing_list, medName);
                    list.setAdapter(adapter);
                } catch (Exception e) {
                    Log.i("ankur", e.toString());
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return root;
    }
}

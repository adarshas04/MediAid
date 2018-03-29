package com.example.ankit.mediaid.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ankit.mediaid.FirebaseHelper;
import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.R;
import com.example.ankit.mediaid.UI.Meds_Detailes;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Med_List extends Fragment {

    View root;


    private ListView lv;

    FirebaseHelper helper;

    // Listview Adapter
    ArrayAdapter<String> adapter;


    // Search EditText
    EditText inputSearch;


    DatabaseReference db;

    ArrayList<String> medName = new ArrayList<>();



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_med__list, container, false);

        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);

        lv = root.findViewById(R.id.list_view);
        inputSearch = root.findViewById(R.id.inputSearch);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, medName);
        lv.setAdapter(adapter);


        db.child("meds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medName.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Meds meds=ds.getValue(Meds.class);
                    Log.i("yo","Qw"+meds);
                    if(meds != null ) {
                        medName.add(meds.getName());
                    }
                }
                Collections.sort(medName);
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String med = String.valueOf(adapterView.getItemAtPosition(i));
                Intent intent = new Intent(getContext(), Meds_Detailes.class);
                intent.putExtra("name",med);
                startActivity(intent);
                Toast.makeText(getContext(), med, Toast.LENGTH_LONG).show();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                try {
                    Med_List.this.adapter.getFilter().filter(cs);
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




        return root;
    }


}

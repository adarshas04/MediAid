package com.example.ankit.mediaid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ankit.mediaid.Models.Meds;
import com.example.ankit.mediaid.Models.Quantity;
import com.example.ankit.mediaid.UI.Edit_Meds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;



public class ListViewAdaptor extends ArrayAdapter implements Filterable {

    Context _context;
    ArrayList<Meds> med;
    ArrayList<Meds>filteredData = null;
    int resource;
    private ItemFilter mFilter = new ItemFilter();


    public ListViewAdaptor(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this._context = context;
        this.med = objects;
        this.filteredData = objects;
        this.resource = resource;
    }

    public int getCount() {
        return filteredData.size();
    }

    public Meds getItem(int i) {
        return filteredData.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View v, ViewGroup viewGroup) {
        final Meds meds = filteredData.get(i);
        View view;
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate( this.resource,null,true);

        TextView med_name = view.findViewById(R.id.title);
        final TextView price = view.findViewById(R.id.artist);
        final TextView quantity = view.findViewById(R.id.duration);
        Button delete = view.findViewById(R.id.delete);
        Button edit = view.findViewById(R.id.edit);

        ImageView image = view.findViewById(R.id.list_image);

        Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/mediaid-be62a.appspot.com/o/Image%2Fmedia_xll_7501226.png?alt=media&token=e50781ca-c5d1-4c16-89d3-a70228d1b378").into(image);
        med_name.setText(meds.getName());
       final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("inventory").child(meds.getMed_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quantity q = dataSnapshot.getValue(Quantity.class);
                Log.i("qwew"," "+q);
                if(q!= null) {
                    price.setText(q.getPrice());
                    quantity.setText(q.getQuantity());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = FirebaseAuth.getInstance().getUid();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Object toRemove = getItem(i);
                remove(toRemove);
                db.child("store_inventory").child(user_id).orderByValue().equalTo(meds.getMed_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            Log.i("yoqw"," " + ds);
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                db.child("meds").child(meds.getMed_id()).removeValue();
                db.child("inventory").child(meds.getMed_id()).child(user_id).removeValue();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), Edit_Meds.class);
                in.putExtra("id", meds.getMed_id());
                _context.startActivity(in);
            }

        });
        return view;
    }
    public Filter getFilter() {
        return mFilter;
    }



    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Meds> list = med;

            int count = list.size();
            final ArrayList<Meds> nlist = new ArrayList<>(count);

            Meds filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Meds>) results.values;
            notifyDataSetChanged();
        }

    }


}

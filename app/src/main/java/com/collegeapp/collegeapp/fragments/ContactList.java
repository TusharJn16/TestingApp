package com.collegeapp.collegeapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.adapters.RecyclerViewAdapter;
import com.collegeapp.collegeapp.models.contacts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactList extends Fragment {

    public RecyclerViewAdapter recyclerViewAdapter;
    public LinearLayoutManager layoutManager;
    public List<contacts> contactslist = new ArrayList<>();
    public DatabaseReference myref;
    public List<String> keylist = new ArrayList<>();
    public View v;
    ProgressDialog progressDialog;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    public ContactList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Contact List");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        init();
        loaddata();
    }

    private void loaddata() {
        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");
        contactslist.clear();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    contacts contactvar = new contacts(snapshot.child("name").getValue().toString(), snapshot.child("pos").getValue().toString(),
                            snapshot.child("number").getValue().toString(), snapshot.child("emailid").getValue().toString(), snapshot.child("image").getValue().toString());
                    contactslist.add(contactvar);
                    String value = snapshot.getKey();
                    keylist.add(value);

                }
                recyclerViewAdapter = new RecyclerViewAdapter(getContext(), contactslist, keylist);


                progressDialog.dismiss();
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void init() {

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);


    }
}

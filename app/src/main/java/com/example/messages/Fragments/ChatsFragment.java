package com.example.messages.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messages.Adapter.UsersAdapter;
import com.example.messages.Models.Users;
import com.example.messages.R;
import com.example.messages.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding mBinding;
    ArrayList<Users> wList = new ArrayList<>();
    FirebaseDatabase mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentChatsBinding.inflate(inflater,container,false);
        mDatabase=FirebaseDatabase.getInstance();

        UsersAdapter wAdapter = new UsersAdapter(wList,getContext());
        mBinding.wChatRecyclerView.setAdapter(wAdapter);

        LinearLayoutManager wLinearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.wChatRecyclerView.setLayoutManager(wLinearLayoutManager);

        mDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wList.clear();
                for(DataSnapshot wDataSnapshot : snapshot.getChildren()){
                    Users mUser = wDataSnapshot.getValue(Users.class);
                    mUser.setUserId(wDataSnapshot.getKey());
                    if(!mUser.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                        wList.add(mUser);
                    }
                }
                wAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mBinding.getRoot();
    }
}
package com.example.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.messages.Adapter.ChatsAdapter;
import com.example.messages.Models.MessageModel;
import com.example.messages.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding mBinding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        mBinding= ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        getSupportActionBar().hide();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final String wSenderId = mAuth.getUid();
        String wReceiveId = getIntent().getStringExtra("userId");
        String wUserName = getIntent().getStringExtra("userName");
        String wProfilePic = getIntent().getStringExtra("profilePic");

        mBinding.txtUsername.setText(wUserName);
        Picasso.get().load(wProfilePic).placeholder(R.drawable.avtar3).into(mBinding.wProfileImage);

        mBinding.wBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(mIntent);

            }
        });

        final ArrayList<MessageModel> wMessageModels = new ArrayList<>();
        final ChatsAdapter wChatAdapter = new ChatsAdapter(wMessageModels,this,wReceiveId);
        mBinding.wChatRecyclerView.setAdapter(wChatAdapter);

        LinearLayoutManager wLayoutManager = new LinearLayoutManager(this);
        mBinding.wChatRecyclerView.setLayoutManager(wLayoutManager);

        final String wSenderRoom = wSenderId + wReceiveId;
        final String wReceiverRoom = wReceiveId + wSenderId;

        mDatabase.getReference().child("chats").child(wSenderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                wMessageModels.clear();
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    MessageModel wModel = snapshot1.getValue(MessageModel.class);
                                    wModel.setwMessageId(snapshot1.getKey());
                                    wMessageModels.add(wModel);
                                }
                                wChatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        mBinding.arrowSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wMessage = mBinding.wEnterMessage.getText().toString();
                final MessageModel wModel = new MessageModel(wSenderId,wMessage);
                wModel.setwTimestamp(new Date().getTime());
                mBinding.wEnterMessage.setText("");

                mDatabase.getReference().child("chats").child(wSenderRoom).push()
                        .setValue(wModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mDatabase.getReference().child("chats").child(wReceiverRoom).push()
                                        .setValue(wModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });


    }
}
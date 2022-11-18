package com.example.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.messages.Adapter.ChatsAdapter;
import com.example.messages.Models.MessageModel;
import com.example.messages.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityGroupChatBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        getSupportActionBar().hide();

        mBinding.wBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(mIntent);

            }
        });

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String SenderId = FirebaseAuth.getInstance().getUid();
        mBinding.txtUsername.setText("Group Chat");

        final ChatsAdapter wChatAdapter = new ChatsAdapter(messageModels,this);
        mBinding.ChatRecyclerView.setAdapter(wChatAdapter);

        LinearLayoutManager wLinearLayoutManager = new LinearLayoutManager(this);
        mBinding.ChatRecyclerView.setLayoutManager(wLinearLayoutManager);

        mDatabase.getReference().child("Group Chats")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModels.clear();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                                    messageModels.add(model);
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
                final String wMessage = mBinding.wEnterMessage.getText().toString();
                final MessageModel wModel = new MessageModel(SenderId,wMessage);
                wModel.setwTimestamp(new Date().getTime());
                mBinding.wEnterMessage.setText("");

                mDatabase.getReference().child("Group Chats").push()
                        .setValue(wModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(GroupChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
package com.example.messages.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messages.ChatDetailActivity;
import com.example.messages.Models.Users;
import com.example.messages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    ArrayList<Users> wList;
    Context wContext;

    public UsersAdapter(ArrayList<Users> wList, Context wContext) {
        this.wList = wList;
        this.wContext = wContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wView = LayoutInflater.from(wContext).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(wView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users wUsers = wList.get(position);
        Picasso.get().load(wUsers.getProfilePic()).placeholder(R.drawable.avtar3).into(holder.wImage);
        holder.wUserName.setText(wUsers.getUserName());

        //To set last message in firebase
        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(FirebaseAuth.getInstance().getUid() + wUsers.getUserId())
                                .orderByChild("wTimestamp")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.hasChildren()){
                                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                holder.wLastMessage.setText(dataSnapshot.child("wMessage").getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(wContext, ChatDetailActivity.class);
                mIntent.putExtra("userId",wUsers.getUserId());
                mIntent.putExtra("profilePic",wUsers.getProfilePic());
                mIntent.putExtra("userName",wUsers.getUserName());
                wContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView wImage;
        TextView wUserName , wLastMessage;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            wImage = itemView.findViewById(R.id.w_profile_image);
            wUserName = itemView.findViewById(R.id.wUserNameList);
            wLastMessage = itemView.findViewById(R.id.wLastMessage);
        }
    }
}

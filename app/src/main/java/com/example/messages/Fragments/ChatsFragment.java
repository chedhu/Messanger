
package com.example.messages.Fragments;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatsAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> wMessageModel;
    Context wContext;
    String recId;

    int W_SENDER_VIEW_TYPE = 1;
    int W_RECIEVER_VIEW_TYPE = 2;

    public ChatsAdapter(ArrayList<MessageModel> wMessageModel, Context wContext) {
        this.wMessageModel = wMessageModel;
        this.wContext = wContext;
    }

    public ChatsAdapter(ArrayList<MessageModel> wMessageModel, Context wContext, String recId) {
        this.wMessageModel = wMessageModel;
        this.wContext = wContext;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==W_SENDER_VIEW_TYPE){
            View wView = LayoutInflater.from(wContext).inflate(R.layout.sample_send,parent,false);
            return new SenderViewHolder(wView);
        }
        else{
            View wView = LayoutInflater.from(wContext).inflate(R.layout.sample_receive,parent,false);
            return new RecieverViewHolder(wView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(wMessageModel.get(position).getwUId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return W_SENDER_VIEW_TYPE;
        }
        else{
            return W_RECIEVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = wMessageModel.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(wContext)
                        .setTitle("Delete")
                        .setMessage("Are you Sure you want to delete this message?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase wDatabase = FirebaseDatabase.getInstance();
                                String wSenderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                wDatabase.getReference().child("chats").child(wSenderRoom)
                                        .child(messageModel.getwMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        if(holder.getClass()  == SenderViewHolder.class){
            ((SenderViewHolder)holder).wSenderMsg.setText(messageModel.getwMessage());

            Date wDate = new Date(messageModel.getwTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String wstrDate = simpleDateFormat.format(wDate);
            ((SenderViewHolder)holder).wSenderTime.setText(wstrDate);
        }
        else{
            ((RecieverViewHolder)holder).wRecieverMsg.setText(messageModel.getwMessage());

            Date wDate = new Date(messageModel.getwTimestamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String wstrDate = simpleDateFormat.format(wDate);
            ((RecieverViewHolder)holder).wRecieverTime.setText(wstrDate);
        }

    }

    @Override
    public int getItemCount() {
        return wMessageModel.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView wRecieverMsg , wRecieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            wRecieverMsg=itemView.findViewById(R.id.wReceiveText);
            wRecieverTime=itemView.findViewById(R.id.wReceiveTime);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView wSenderMsg , wSenderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            wSenderMsg=itemView.findViewById(R.id.wSendText);
            wSenderTime=itemView.findViewById(R.id.wSendTime);
        }
    }
}

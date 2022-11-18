package com.example.messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.messages.Models.Users;
import com.example.messages.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding mBinding;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        getSupportActionBar().hide();

        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mBinding.wBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(mIntent);
            }
        });

        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mBinding.txtUserName.getText().toString().equals("") && !mBinding.txtStatus.getText().toString().equals(""))
                {

                    String wUserName = mBinding.txtUserName.getText().toString();
                    String wStatus = mBinding.txtStatus.getText().toString();

                    HashMap<String,Object> wObj = new HashMap<>();
                    wObj.put("userName", wUserName);
                    wObj.put("status",wStatus);

                    mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(wObj);

                    Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(SettingsActivity.this, "Please enter UserName and Status", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users mUser = snapshot.getValue(Users.class);
                                Picasso.get()
                                        .load(mUser.getProfilePic())
                                        .placeholder(R.drawable.avtar3)
                                        .into(mBinding.wProfileImage);


                                mBinding.txtUserName.setText(mUser.getStatus());
                                mBinding.txtStatus.setText(mUser.getStatus());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        mBinding.wPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_GET_CONTENT);
                mIntent.setType("image/*");
                startActivityForResult(mIntent,25);
            }
        });

    }

    @Override
    protected void onActivityResult(int mRequestCode, int mResultCode, @Nullable Intent data) {
        super.onActivityResult(mRequestCode, mResultCode, data);

        if(data.getData()!=null){
            Uri wSFile = data.getData();
            mBinding.wProfileImage.setImageURI(wSFile);

            final StorageReference wReference = mStorage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            wReference.putFile(wSFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    wReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });

        }
    }
}
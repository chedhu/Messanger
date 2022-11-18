package com.example.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.messages.Models.Users;
import com.example.messages.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding mBinding;
    private FirebaseAuth mMAuth;
    FirebaseDatabase mDatabase;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mMAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();

        getSupportActionBar().hide();
        mProgressDialog = new ProgressDialog(SignUpActivity.this);
        mProgressDialog.setTitle("Creating Account");
        mProgressDialog.setMessage("We're creating your account.");

        mBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mBinding.txtUsername.getText().toString().isEmpty() && !mBinding.txtEmail.getText().toString().isEmpty() && !mBinding.txtPassword.getText().toString().isEmpty()){
                    mProgressDialog.show();
                    mMAuth.createUserWithEmailAndPassword(mBinding.txtEmail.getText().toString(),mBinding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> mTask) {
                                    Log.e("manan",mBinding.txtEmail.getText().toString());
                                    mProgressDialog.dismiss();
                                    if(mTask.isSuccessful()){
                                        Log.e("manan ","stage 1");
                                        Users mUser = new Users(mBinding.txtUsername.getText().toString(),mBinding.txtEmail.getText().toString(),mBinding.txtPassword.getText().toString());
                                        String mId = mTask.getResult().getUser().getUid();
                                        mDatabase.getReference().child("Users").child(mId).setValue(mUser);
                                        Toast.makeText(SignUpActivity.this,"Sign Up Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Log.e("manan","stage 2");
                                        Toast.makeText(SignUpActivity.this,mTask.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Enter Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.textAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(mIntent);
            }
        });
    }
}
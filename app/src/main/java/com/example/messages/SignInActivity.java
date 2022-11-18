package com.example.messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.messages.Models.Users;
import com.example.messages.databinding.ActivitySignInBinding;
import com.example.messages.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding mBinding;
    FirebaseAuth mMAuth;
    FirebaseDatabase mFirebaseDatabase;
    ProgressDialog mProgressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        getSupportActionBar().hide();
        mMAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase= FirebaseDatabase.getInstance();

        mProgressDialog = new ProgressDialog(SignInActivity.this);
        mProgressDialog.setTitle("Login");
        mProgressDialog.setMessage("Please wait,Validation in progress");

        GoogleSignInOptions mGsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,mGsOptions);

        mBinding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBinding.txtEmail.getText().toString().isEmpty() && !mBinding.txtPassword.getText().toString().isEmpty()){
                    mProgressDialog.show();
                    mMAuth.signInWithEmailAndPassword(mBinding.txtEmail.getText().toString(),mBinding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> mTask) {
                                    Log.e("manan",mBinding.txtEmail.getText().toString());
                                    mProgressDialog.dismiss();
                                    if(mTask.isSuccessful()){
                                        Log.e("manan ","stage 1");
                                        Intent mIntent =new Intent(SignInActivity.this,MainActivity.class);
                                        startActivity(mIntent);

                                    }
                                    else{
                                        Log.e("manan","stage 2");
                                        Toast.makeText(SignInActivity.this,mTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignInActivity.this,"Enter Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(mMAuth.getCurrentUser()!=null){
            Intent mIntent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(mIntent);
        }

        mBinding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(mIntent);
            }
        });

        mBinding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });
    }

    int M_RC_SIGN_IN = 65;
    private void SignIn(){
        Intent mSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(mSignInIntent, M_RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int mRequestCode, int mResultCode, Intent mData) {
        super.onActivityResult(mRequestCode, mResultCode, mData);

        if(mRequestCode==M_RC_SIGN_IN){
            Task<GoogleSignInAccount> mmTask = GoogleSignIn.getSignedInAccountFromIntent(mData);
            try{
                GoogleSignInAccount mAccount = mmTask.getResult(ApiException.class);
                Log.d("TAG","firebaseAuthWithGoogle:" + mAccount.getId());
                mFirebaseAuthWithGoogle(mAccount.getIdToken());

            }catch(ApiException e){
                Log.w("TAG", "Google Sign in failed",e);
            }
        }
    }
    private void mFirebaseAuthWithGoogle(String mIdToken){
        AuthCredential mCredential = GoogleAuthProvider.getCredential(mIdToken, null);
        mMAuth.signInWithCredential(mCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> mTask) {
                        if (mTask.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser muser = mMAuth.getCurrentUser();

                            Users mUser = new Users();
                            mUser.setUserId(muser.getUid());
                            mUser.setUserName(muser.getDisplayName());
                            mUser.setProfilePic(muser.getPhotoUrl().toString());
                            mFirebaseDatabase.getReference().child("Users").child(muser.getUid()).setValue(mUser);
                            Intent mIntent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(mIntent);

                            Toast.makeText(SignInActivity.this, "Sign In with Google", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Log.w("TAG", "signInWithCredential:success", mTask.getException());
                        }
                    }

                });
    }
}
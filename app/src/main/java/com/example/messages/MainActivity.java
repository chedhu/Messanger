package com.example.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.messages.Adapter.FragmentsAdapter;
import com.example.messages.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding mBinding;
    FirebaseAuth mMAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mMAuth = FirebaseAuth.getInstance();

        mBinding.viewsPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        mBinding.tabsLayout.setupWithViewPager(mBinding.viewsPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater wInflater = getMenuInflater();
        wInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                //Toast.makeText(this, "Settings is clicked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent2);
                break;

            case R.id.groupChat:
                //Toast.makeText(this, "Group Chat is started", Toast.LENGTH_SHORT).show();
                Intent whIntent = new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(whIntent);
                break;

            case R.id.logout:
                mMAuth.signOut();
                Intent mIntent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
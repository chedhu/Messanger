package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class SplashFragment extends BaseFragment {

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_splash, container, false);
        new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(Exception e){
                }
                finally {
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent DashboardActivity = new Intent(getActivity(),DashBoardActivity.class);
                        DashboardActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(DashboardActivity);
                    }else{
                        triggerFragment(new LoginFragment(),false);
                    }
                }
            }
        }.start();
        return view;
    }
}
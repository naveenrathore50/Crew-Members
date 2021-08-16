package com.example.firebaseapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.firebaseapp.MainActivity;
import com.example.firebaseapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profilrActivity extends AppCompatActivity {

    FirebaseAuth  firebaseAuth;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilr);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        firebaseAuth=FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(selecteditem);
        actionBar.setTitle("Home");
        HomeFragment homeFragment=new HomeFragment();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.frameLayout,homeFragment,"Home");
        ft1.commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener selecteditem=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
           switch (item.getItemId()){
               case R.id.nav_home:
                   actionBar.setTitle("Home");
                   HomeFragment homeFragment=new HomeFragment();
                   FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                   ft1.replace(R.id.frameLayout,homeFragment,"Home");
                   ft1.commit();
                   return true;
               case R.id.nav_profile:
                   actionBar.setTitle("Profile");
                   ProfileFragment profileFragment=new ProfileFragment();
                   FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                   ft2.replace(R.id.frameLayout,profileFragment,"");
                   ft2.commit();
                   return true;
               case R.id.nav_User:
                   actionBar.setTitle("Users");
                   UserFragment userFragment=new UserFragment();
                   FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                   ft3.replace(R.id.frameLayout,userFragment,"");
                   ft3.commit();
                   return true;
               case R.id.nav_chat:
                   actionBar.setTitle("Chats");
                   chatFragment chatfragment=new chatFragment();
                   FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                   ft4.replace(R.id.frameLayout,chatfragment,"");
                   ft4.commit();
                   return true;


           }
            return false;
        }
    };
        private  void checkUserStatus(){
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){

                }
                else{
                    startActivity(new Intent(profilrActivity.this, MainActivity.class));
                }
        }
    @Override
    protected void onStart() {
         checkUserStatus();
        super.onStart();
    }


}
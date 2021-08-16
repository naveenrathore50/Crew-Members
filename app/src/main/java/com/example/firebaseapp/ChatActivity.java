package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIV;
    TextView nameTV,userstatusTV;
    EditText chatmessageET;
    ImageButton sendbutton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String hisUID,myUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        Intent intent=getIntent();
        hisUID= intent.getStringExtra("hisUID");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        Query userquery=databaseReference.orderByChild("UID").equalTo(hisUID);
        userquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                 for(DataSnapshot ds: snapshot.getChildren()){

                     String Name = ""+ds.child("Name").getValue();
                     String Image=""+ds.child("Image").getValue();
                     nameTV.setText(Name);
                     try{
                         Picasso.get().load(Image).placeholder(R.drawable.ic_default_image_white).into(profileIV);
                     }
                     catch (Exception e){
                         Picasso.get().load(R.drawable.ic_default_image_white).into(profileIV);
                     }
                 }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.chat_recyclerview);
        profileIV=findViewById(R.id.profileIV);
        nameTV=findViewById(R.id.chat_name_TV);
        userstatusTV=findViewById(R.id.chat_userstatus_TV);
        chatmessageET=findViewById(R.id.messageTV);
        sendbutton=findViewById(R.id.sendBtn);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=chatmessageET.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Can't Send Empty Message", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage(String message) {
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
        HashMap<Object,String> hashMap =new HashMap<>();
        hashMap.put("Sender",myUID);
        hashMap.put("Reciever",hisUID);
        hashMap.put("Message",message);
        databaseReference1.child("Chats").push().setValue(hashMap);
        chatmessageET.setText("");
    }

    private  void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
          myUID = user.getUid();
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main,menu);
         menu.findItem(R.id.mSearch).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.mlogout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
package com.example.firebaseapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.firebaseapp.ChatActivity;
import com.example.firebaseapp.LoginActivity;
import com.example.firebaseapp.MainActivity;
import com.example.firebaseapp.Models.modelUser;
import com.example.firebaseapp.R;

import com.example.firebaseapp.RegisterActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {
    Context context;
    List<modelUser> userList;

    public AdapterUsers(Context context, List<modelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AdapterUsers.MyHolder holder, int position) {
        String hisUID=userList.get(position).getUID();
        String userimage=userList.get(position).getImage();
        String username=userList.get(position).getName();
        String useremail=userList.get(position).getEmail();
        holder.mnameTV.setText(username);
        holder.memailTV.setText(useremail);
        try{
            Picasso.get().load(userimage).placeholder(R.drawable.ic_default_img).into(holder.AvatarIV);
        }
        catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(context, ChatActivity.class);
             intent.putExtra("hisUID",hisUID);
             context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView AvatarIV;
        TextView mnameTV,memailTV;

        public MyHolder(@NonNull  View itemView) {
            super(itemView);
            AvatarIV=itemView.findViewById(R.id.AvatarIV);
            memailTV=itemView.findViewById(R.id.EmailTV);
            mnameTV=itemView.findViewById(R.id.NameTV);

        }

    }
}

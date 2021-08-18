package com.example.firebaseapp.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.Models.Modelchat;
import com.example.firebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;



import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
    public static final int MSG_LEFT=0;
    public static final int MSG_RIGHT=1;
    Context context;
    List<Modelchat> chatlist;
    String imageurl;
   FirebaseUser fuser;


    public AdapterChat(Context context, List<Modelchat> chatlist, String imageurl) {
        this.context = context;
        this.chatlist = chatlist;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return new MyHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull  AdapterChat.MyHolder holder, int position) {
           String message=chatlist.get(position).getMessage();
           String timestamp=chatlist.get(position).getTimestamp();
          Calendar cal=Calendar.getInstance(Locale.ENGLISH);
          cal.setTimeInMillis(Long.parseLong(timestamp));
          String dateAndtime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
          holder.messageTV.setText(message);
          holder.timestapTV.setText(dateAndtime);
          try {
              Picasso.get().load(imageurl).into(holder.profileIV);
          }
          catch (Exception e){

          }

          if(chatlist.get(position).isseen()){
                  holder.isseenTV.setText("Seen");
              }
          else{
                  holder.isseenTV.setText("delivered");
              }



    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatlist.get(position).getSender().equals(fuser.getUid())){
            return MSG_RIGHT;
        }
        else{
            return  MSG_LEFT;
        }

    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView profileIV;
        TextView isseenTV,messageTV,timestapTV;
        public MyHolder(@NonNull  View itemView) {
            super(itemView);
           profileIV=itemView.findViewById(R.id.ProfileIV_chat);
           messageTV=itemView.findViewById(R.id.MessageTV);
           isseenTV=itemView.findViewById(R.id.isSeenTV);
           timestapTV=itemView.findViewById(R.id.timestampTV);
        }
    };


}

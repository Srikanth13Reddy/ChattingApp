package com.apptomate.chattingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.activities.home.ChatActivity;
import com.apptomate.chattingapp.activities.home.ChatDetailedActivity;
import com.apptomate.chattingapp.activities.home.MainActivity;
import com.apptomate.chattingapp.models.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter  extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    ArrayList<Users> arrayList;
    Context context;

    public UsersAdapter(ArrayList<Users> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v=  LayoutInflater.from(context).inflate(R.layout.sample_show_users,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users=arrayList.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.profile).into(holder.imageView);
        holder.userName.setText(users.getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, ChatActivity.class);
                i.putExtra("userId",users.getUserId());
                i.putExtra("profilePic",users.getProfilepic());
                i.putExtra("userName",users.getUserName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView imageView;
        TextView userName,lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.userName);
            lastMessage=itemView.findViewById(R.id.lastMessage);
        }
    }
}



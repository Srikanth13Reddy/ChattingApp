package com.apptomate.chattingapp.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.models.ModelChatNew;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapterNew extends RecyclerView.Adapter<ChatAdapterNew.MyViewHolder>{

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    ArrayList<ModelChatNew > chatList;
    Context context;
    String imageUrl;
    FirebaseUser firebaseUser;

    public ChatAdapterNew(ArrayList<ModelChatNew> chatList, Context context, String imageUrl) {
        this.chatList = chatList;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType==MSG_TYPE_LEFT)
       {
          View v= LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
          return  new MyViewHolder(v);
       }else {
           View v= LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
           return  new MyViewHolder(v);
       }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        ModelChatNew model=chatList.get(position);
        Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(model.getTimeStamp());
        String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
        holder.tv_time.setText(dateTime);
        holder.tv__msg.setText(model.getMessage());
        Picasso.get().load(imageUrl).placeholder(R.drawable.profile).into(holder.iv_profile);
        if (position==chatList.size()-1)
        {
            if (chatList.get(position).getSeenMessage().equalsIgnoreCase("yes"))
            {
                holder.tv_isSeen.setText("Seen");
            }else {
                holder.tv_isSeen.setText("Delivered");
            }
        }else {
            holder.tv_isSeen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equalsIgnoreCase(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView iv_profile;
        TextView tv__msg,tv_time,tv_isSeen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv__msg=itemView.findViewById(R.id.tv_msg);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_isSeen=itemView.findViewById(R.id.tvisSeenTv);
            iv_profile=itemView.findViewById(R.id.profile_image);
        }
    }

    public String getTime(long time){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);

        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        Date date = new Date(time);
        String kTime = format.format(date);
        return kTime;
    }
}

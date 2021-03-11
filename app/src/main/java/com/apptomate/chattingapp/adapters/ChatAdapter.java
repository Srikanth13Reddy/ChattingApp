package com.apptomate.chattingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class ChatAdapter  extends RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==SENDER_VIEW_TYPE)
        {
            View v= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(v);
        }else {
            View v= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel=messageModels.get(position);

        if (holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).sender_mesg.setText(messageModel.getMessage());
            ((SenderViewHolder)holder).sender_time.setText(getTime(messageModel.getTimeStamp()));
           // ((SenderViewHolder)holder).sender_time.setText(messageModel.getTimeStamp());
        }else {
            ((ReceiverViewHolder)holder).receiver_mesg.setText(messageModel.getMessage());
            ((ReceiverViewHolder)holder).receiver_time.setText(getTime(messageModel.getTimeStamp()));
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (messageModels.get(position).getuId().equalsIgnoreCase(FirebaseAuth.getInstance().getUid()))
        {
            return  SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder
    {

        TextView receiver_mesg,receiver_time;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver_mesg=itemView.findViewById(R.id.receiver_text);
            receiver_time= itemView.findViewById(R.id.receiver_time);
        }
    }


    public class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView sender_mesg,sender_time;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sender_mesg=itemView.findViewById(R.id.sender_text);
            sender_time= itemView.findViewById(R.id.sender_time);
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

package com.apptomate.chattingapp.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.adapters.ChatAdapter;
import com.apptomate.chattingapp.adapters.ChatAdapterNew;
import com.apptomate.chattingapp.databinding.ActivityChatBinding;
import com.apptomate.chattingapp.databinding.ActivityChatDetailedBinding;
import com.apptomate.chattingapp.models.MessageModel;
import com.apptomate.chattingapp.models.ModelChatNew;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static java.lang.System.currentTimeMillis;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ValueEventListener seenListner;
    DatabaseReference databaseRefForSeen;
   String imageUrl,userName;
    ArrayList<ModelChatNew> arrayList;
    ChatAdapterNew adapterNew;
    private String senderID,receiverId;
    DatabaseReference userDbReference;
    ImageButton attchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
         senderID=auth.getUid();
        receiverId=getIntent().getStringExtra("userId");
        // imageUrl=getIntent().getStringExtra("profilePic");
       //  userName=getIntent().getStringExtra("userName");


        userDbReference= database.getReference("Users");
        Query userQuery=userDbReference.orderByChild("userId").equalTo(receiverId);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    userName=""+ ds.child("userName").getValue();
                    imageUrl=""+ ds.child("profilepic").getValue();
                  String  typing=""+ ds.child("typingTo").getValue();
                  if (typing.equalsIgnoreCase(senderID))
                  {
                      binding.userOnlineStatus.setText("typing....");
                  }else {
                      String onlineStatus=""+ds.child("onlineStatus").getValue();
                      if (onlineStatus.equalsIgnoreCase("Online"))
                      {
                          binding.userOnlineStatus.setText("Online");
                      }else {
                          Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
                          calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                          String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
                          binding.userOnlineStatus.setText("Last Seen  "+dateTime);
                      }
                  }

                    binding.userName.setText(userName);
                    Picasso.get().load(imageUrl).placeholder(R.drawable.profile).into(binding.profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.attchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


        binding.backArrow.setOnClickListener(v -> finish());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(false);
        // linearLayoutManager.scrollToPositionWithOffset(2, 20);
        binding.messagesRv.setLayoutManager(linearLayoutManager);
        // adapter.notifyDataSetChanged();
        binding.msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.etMessage.getText().toString().trim();
                HashMap<String,Object> hm=new HashMap<>();
                hm.put("sender",senderID);
                hm.put("receiver",receiverId);
                hm.put("message",message);
                hm.put("timeStamp",new Date().getTime());
                hm.put("seenMessage","no");
                //MessageModel messageModel=new MessageModel(senderID,message);
              //  messageModel.setTimeStamp(new Date().getTime());
                binding.etMessage.setText("");
                database.getReference().child("chatsnew").push().setValue(hm);
            }
        });
        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                       if (s.toString().length()==0)
                       {
                           checkTypingStatus("noOne");
                       }else {
                           checkTypingStatus(receiverId);
                       }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        readMessages();
        seenMessage();


    }

    private void seenMessage()
    {
        databaseRefForSeen=FirebaseDatabase.getInstance().getReference("chatsnew");
        seenListner=databaseRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              for (DataSnapshot ds:snapshot.getChildren())
              {
                  ModelChatNew chatNew=ds.getValue(ModelChatNew.class);
                  if (chatNew.getReceiver().equalsIgnoreCase(senderID)&&chatNew.getSender().equalsIgnoreCase(receiverId))
                  {
                      HashMap<String,Object> hmSeen=new HashMap<>();
                      hmSeen.put("seenMessage","yes");
                      ds.getRef().updateChildren(hmSeen);
                  }
              }
//                adapterNew.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkOnlineStatus(String status)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(senderID);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);
        databaseReference.updateChildren(hashMap);
    }

    private void checkTypingStatus(String typing)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(senderID);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("typingTo",typing);
        databaseReference.updateChildren(hashMap);
    }

    private void readMessages()
    {
        arrayList=new ArrayList<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("chatsnew");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ModelChatNew chatNew=dataSnapshot.getValue(ModelChatNew.class);
                    if (chatNew.getReceiver().equalsIgnoreCase(senderID)&&chatNew.getSender().equalsIgnoreCase(receiverId)||
                            chatNew.getReceiver().equalsIgnoreCase(receiverId)&&chatNew.getSender().equalsIgnoreCase(senderID))
                    {
                        arrayList.add(chatNew);
                        adapterNew=new ChatAdapterNew(arrayList,ChatActivity.this,imageUrl);
                        adapterNew.notifyDataSetChanged();
                        binding.messagesRv.setAdapter(adapterNew);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
        checkOnlineStatus("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
       String timeStamp= String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timeStamp);
        checkTypingStatus("noOne");
        databaseRefForSeen.removeEventListener(seenListner);
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("Online");
        super.onResume();
    }

    private void checkUserStatus()
    {
        auth.getCurrentUser();
    }
}
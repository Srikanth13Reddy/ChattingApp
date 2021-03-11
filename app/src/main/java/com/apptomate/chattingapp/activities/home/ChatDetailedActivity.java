package com.apptomate.chattingapp.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.adapters.ChatAdapter;
import com.apptomate.chattingapp.databinding.ActivityChatDetailedBinding;
import com.apptomate.chattingapp.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailedActivity extends AppCompatActivity {

    ActivityChatDetailedBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
       final String senderID=auth.getUid();
        String receiverId=getIntent().getStringExtra("userId");
        String profilePic=getIntent().getStringExtra("profilePic");
        String userName=getIntent().getStringExtra("userName");
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.profile).into(binding.profileImage);
        binding.backArrow.setOnClickListener(v -> finish());

        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        final ChatAdapter adapter=new ChatAdapter(messageModels,this);
        binding.messagesRv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(false);
       // linearLayoutManager.scrollToPositionWithOffset(2, 20);
        binding.messagesRv.setLayoutManager(linearLayoutManager);
       // adapter.notifyDataSetChanged();

        final String senderRoom=senderID+receiverId;
        final String receiverRoom=receiverId+senderID;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
               for (DataSnapshot dataSnapshot:snapshot.getChildren())
               {
                   MessageModel model=dataSnapshot.getValue(MessageModel.class);
                   messageModels.add(model);
               }
                binding.messagesRv.smoothScrollToPosition(messageModels.size());
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        binding.msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.etMessage.getText().toString();
                MessageModel messageModel=new MessageModel(senderID,message);
                messageModel.setTimeStamp(new Date().getTime());
                binding.etMessage.setText("");
                database.getReference().child("chats").child(senderRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats").child(receiverRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });
    }
}
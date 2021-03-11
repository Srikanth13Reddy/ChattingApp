package com.apptomate.chattingapp.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();

    }
}
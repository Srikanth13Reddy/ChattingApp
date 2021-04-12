package com.apptomate.chattingapp.activities.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.databinding.ActivityAccountVerificationBinding;
import com.apptomate.chattingapp.databinding.ActivityPhoneNumberLoginBinding;

public class AccountVerificationActivity extends AppCompatActivity {

    ActivityAccountVerificationBinding binding;
    String phoneNumber,countryCode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAccountVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Account verification");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b=getIntent().getExtras();
        phoneNumber= b.getString("phoneNumber");
        countryCode=b.getString("countryCode");
        binding.textTv.setText("We sent otp to "+countryCode+phoneNumber);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
           finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyOtp(View view) {
    }
}
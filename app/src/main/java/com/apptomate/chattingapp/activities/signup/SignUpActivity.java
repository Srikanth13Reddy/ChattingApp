package com.apptomate.chattingapp.activities.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.apptomate.chattingapp.activities.login.SignInActivity;
import com.apptomate.chattingapp.databinding.ActivitySignUpBinding;
import com.apptomate.chattingapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Ceating Account");
        progressDialog.setMessage("Please wait....");
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.etEmailSignup.getText().toString().trim();
                String password = binding.etPasswordSignup.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.etEmailSignup.setError("Enter valid email");
                    binding.etEmailSignup.requestFocus();
                    binding.etEmailSignup.setFocusable(true);
                } else if (password.length() < 6) {
                    binding.etPasswordSignup.setError("Password length at least 6 characters");
                    binding.etPasswordSignup.requestFocus();
                    binding.etPasswordSignup.setFocusable(true);
                } else {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(binding.etEmailSignup.getText().toString(), binding.etPasswordSignup.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Users user = new Users(binding.etNameSignup.getText().toString(), binding.etEmailSignup.getText().toString(), binding.etPasswordSignup.getText().toString(),"Online");
                                        String id = task.getResult().getUser().getUid();
                                       // database.getReference().child("Users").child(id).setValue(user);
                                        DatabaseReference databaseReference=database.getReference("Users");
                                        databaseReference.child(id).setValue(user);
                                        Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                        binding.etEmailSignup.setText("");
                                        binding.etNameSignup.setText("");
                                        binding.etPasswordSignup.setText("");
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void signIn(View view) {
        Intent i=new Intent(this, SignInActivity.class);
        startActivity(i);
    }
}
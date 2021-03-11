package com.apptomate.chattingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.activities.home.MainActivity;
import com.apptomate.chattingapp.activities.login.SignInActivity;
import com.apptomate.chattingapp.adapters.UsersAdapter;
import com.apptomate.chattingapp.databinding.FragmentChatsBinding;
import com.apptomate.chattingapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Users> arrayList=new ArrayList<>();

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentChatsBinding.inflate(inflater, container, false);
        database= FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
       getAllUsers();
         return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
       MenuItem item= menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query))
                {
                    getSerachUsers(query);
                }else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!TextUtils.isEmpty(query))
                {
                    getSerachUsers(query);
                }else {
                    getAllUsers();
                }
                return false;
            }
        });
         super.onCreateOptionsMenu(menu,inflater);
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                break;
            case R.id.logout:
                auth.signOut();
                getActivity().finish();
                Intent i=new Intent(getContext(), SignInActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    void getAllUsers()
    {
        UsersAdapter adapter=new UsersAdapter(arrayList,getContext());
        binding.chatsRv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        // linearLayoutManager.setReverseLayout(true);
        binding.chatsRv.setLayoutManager(linearLayoutManager);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users= dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!users.getUserId().equalsIgnoreCase(auth.getUid()))
                    {
                        arrayList.add(users);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getSerachUsers(String query)
    {
        UsersAdapter adapter=new UsersAdapter(arrayList,getContext());
        binding.chatsRv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        // linearLayoutManager.setReverseLayout(true);
        binding.chatsRv.setLayoutManager(linearLayoutManager);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users= dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!users.getUserId().equalsIgnoreCase(auth.getUid()))
                    {
                        if (users.getUserName().toLowerCase().contains(query)||users.getMail().toLowerCase().contains(query.toLowerCase()))
                        {
                            arrayList.add(users);
                        }

                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
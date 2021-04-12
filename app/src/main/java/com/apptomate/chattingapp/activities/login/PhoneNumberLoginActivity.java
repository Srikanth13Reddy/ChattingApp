package com.apptomate.chattingapp.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.apptomate.chattingapp.R;
import com.apptomate.chattingapp.activities.home.MainActivity;
import com.apptomate.chattingapp.adapters.CountryCodeAdapter;
import com.apptomate.chattingapp.databinding.ActivityPhoneNumberLoginBinding;
import com.apptomate.chattingapp.databinding.ActivitySignInBinding;
import com.apptomate.chattingapp.models.CountryCodeModel;
import com.apptomate.chattingapp.utils.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhoneNumberLoginActivity extends AppCompatActivity  implements CountryCodeAdapter.EventListener{

    ActivityPhoneNumberLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPhoneNumberLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.etCountrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View v=layoutInflater.inflate(R.layout.countrycodesearch_layout,null,false);
                RecyclerView rv_search=v.findViewById(R.id.rv_search_);
                AppCompatTextView tv_notFound=v.findViewById(R.id.tv_notFound);
                SearchView country_search=v.findViewById(R.id.country_search_);
                country_search.setIconifiedByDefault(false);
                AlertDialog.Builder alb=new AlertDialog.Builder(PhoneNumberLoginActivity.this);
                alb.setView(v);
                AlertDialog ad=alb.create();
                ApiConstants.setAnimation(PhoneNumberLoginActivity.this,v,ad);
                getCounteryCode(rv_search,country_search,ad,tv_notFound);
            }
        });
    }

    private void getCounteryCode(RecyclerView rv_search, SearchView country_search, AlertDialog ad, AppCompatTextView tv_notFound)
    {
        ad.show();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_search.getContext(), llm.getOrientation());
        rv_search.addItemDecoration(dividerItemDecoration);
        rv_search.setLayoutManager(llm);
        ArrayList<CountryCodeModel> arrayList=new ArrayList<>();
        try {
            JSONObject js=new JSONObject(ApiConstants.Country_Code);
            JSONArray ja= js.getJSONArray("countries");
            for (int i=0;i<ja.length();i++)
            {
                JSONObject json= ja.getJSONObject(i);
                String code=json.getString("code");
                String name=json.getString("name");
                CountryCodeModel countryCodeModel=new CountryCodeModel();
                countryCodeModel.setCountrycode(code);
                countryCodeModel.setCountryname(name);
                arrayList.add(countryCodeModel);
            }
            CountryCodeAdapter countryAdapter=new CountryCodeAdapter(arrayList,this,tv_notFound,PhoneNumberLoginActivity.this,ad);

            rv_search.setAdapter(countryAdapter);

            country_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    String text = newText;
                    countryAdapter.filter(text);
                    return false;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeEvent(String Countrycode, String name)
    {
        binding.etCountrycode.setText(Countrycode);
    }

    public void send(View view) {

        if (binding.etCountrycode.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please select country code", Toast.LENGTH_SHORT).show();
        }
        else if (binding.etPhoneNumber.getText().toString().isEmpty())
        {
            binding.etPhoneNumber.setError("Required");
            binding.etPhoneNumber.requestFocus();
        }
        else if (binding.etPhoneNumber.getText().toString().length()>12||binding.etPhoneNumber.getText().toString().length()<10)
        {
            binding.etPhoneNumber.setError("Enter valid phone number");
            binding.etPhoneNumber.requestFocus();
        }else
        {
            Intent i=new Intent(this, AccountVerificationActivity.class);
            i.putExtra("countryCode",binding.etCountrycode.getText().toString());
            i.putExtra("phoneNumber",binding.etPhoneNumber.getText().toString());
            startActivity(i);
        }

    }
}
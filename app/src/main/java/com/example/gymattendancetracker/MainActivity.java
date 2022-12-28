package com.example.gymattendancetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gymattendancetracker.Fragments.HomeFragment;
import com.example.gymattendancetracker.Fragments.ProfileFragment;
import com.example.gymattendancetracker.Fragments.ScanQRFragment;
import com.example.gymattendancetracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static boolean  flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // to set home frag by default
        replace(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replace(new HomeFragment());
                    break;
                case R.id.scanqr:
                    if(flag == false){
                        startActivity(new Intent(MainActivity.this , ScanActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "You have already scanned QR code!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.profile:
                    replace(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    //to replace fragments we making a method named replace
    private void replace(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout,fragment);
        transaction.commit();
    }
}
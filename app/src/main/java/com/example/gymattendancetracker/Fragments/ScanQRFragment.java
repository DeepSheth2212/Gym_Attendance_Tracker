package com.example.gymattendancetracker.Fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gymattendancetracker.R;
import com.example.gymattendancetracker.ScanActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.xmlpull.v1.XmlPullParser;

import java.util.Timer;
import java.util.TimerTask;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanQRFragment extends Fragment implements ZXingScannerView.ResultHandler {
    DatabaseReference dbref;
    ZXingScannerView scannerView;
    int count;

    public ScanQRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //you cannot use "this" for context in fragments do we use "getContext"
        scannerView = new ZXingScannerView(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate((XmlPullParser) scannerView, container, false);
        //for firebase
        dbref = FirebaseDatabase.getInstance().getReference("Data").child("LiveCounter");
        Dexter.withContext(getActivity().getApplicationContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

        return view;
    }

    @Override
    public void handleResult(Result rawResult) {

        String Data = rawResult.getText().toString();
        if(Data.equals("GymAttendence")){

            //*************************** Timer part
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    //to decreament after 1 hour
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            count = snapshot.getValue(Integer.class);
                            count = count -1;
                            dbref.setValue(count);
                            HomeFragment.counter.setText(String.valueOf(count));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            };
            //*************************************************************

            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = snapshot.getValue(Integer.class);
                    count = count +1;
                    dbref.setValue(count) ;
                    HomeFragment.counter.setText(String.valueOf(count));
                    timer.schedule(timerTask,10000);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
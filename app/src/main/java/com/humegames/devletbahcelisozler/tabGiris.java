package com.humegames.devletbahcelisozler;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.R.attr.permission;
import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * Created by Mert on 18.05.2017.
 */

public class tabGiris extends Fragment {

    int firstTime;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabgiris, container, false);


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor mEdit1 = sp.edit();
        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(getContext());

         firstTime = mSharedPreference1.getInt("first_Time",1);

        if(firstTime == 1){
            mEdit1.putInt("first_Time",0);
            mEdit1.commit();
           startActivity(new Intent(getActivity(),Pop.class));
        }



        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            }
        }

        AdView mAdMobAdView;
        mAdMobAdView = (AdView) rootView.findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("Device ID")
                .build();
        mAdMobAdView.loadAd(adRequest);

        Button btn1 = (Button) rootView.findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://goo.gl/forms/cAWJ5EkqNfNO7OSf1");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        Button btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Devlet Bahçeli'nin eğlenceli sesleri. https://play.google.com/store/apps/details?id=com.humegames.devletbahcelisozler");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        return rootView;
    }


}

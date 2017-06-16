package com.humegames.devletbahcelisozler;

/**
 * Created by Mert on 7.06.2017.
 */
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;





public class tabFavoriler extends Fragment {
    MediaPlayer mp1 = null;

    String[] buttonTexts ;
    String[] sesTexts ;
    int i = 0;

    float scale ;
    int buttonWidth;
    int buttonHeight;
    int marginsLRPx;
    int marginsTBPx;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View rootView = inflater.inflate(R.layout.tabfavoriler, container, false);


        AdView mAdMobAdView;
        mAdMobAdView = (AdView) rootView.findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("Device ID")
                .build();
        mAdMobAdView.loadAd(adRequest);


        TableLayout table = (TableLayout) rootView.findViewById(R.id.tableLayoutForButtons);

        scale = getContext().getResources().getDisplayMetrics().density;
        buttonWidth = (int) (150 * scale + 0.5f);
        buttonHeight = (int) (75 * scale + 0.5f);
        marginsLRPx = (int) (3 * scale + 0.5f);
        marginsTBPx = (int) (30 * scale + 0.5f);
        buttonTexts = new String[500];
        sesTexts = new String[500];


        i=0;

        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = mSharedPreference1.getInt("buttonText_size", 0);



        for(int j=0;j<=size;j++)
        {
            buttonTexts[j] = mSharedPreference1.getString("buttonText_" + j,null);
            sesTexts[j] = mSharedPreference1.getString("sesText_" + j,null);
        }

        int rowMax;
        if(size % 2 == 0){
            rowMax = size/2;
        }else{
            rowMax = size/2;
            rowMax++;
        }

        for(int row = 0; row < rowMax ; row++){
            TableRow tableRow = new TableRow(getContext());
            TableLayout.LayoutParams tL = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,1.0f);
            tL.setMargins(50,marginsTBPx,0,0);
            tableRow.setLayoutParams(tL);
            table.addView(tableRow);

            int colMax = 2;
            if(size % 2 == 0) {
                colMax = 2;
            } else if(size % 2 == 1 && row == rowMax-1) {
                colMax = 1;
            }

            for(int col = 0; col < colMax;col++){

                final Button button = new Button(getContext());

                TableRow.LayoutParams layoutParams = new  TableRow.LayoutParams(buttonWidth,buttonHeight);
                layoutParams.setMargins(0,0,marginsLRPx,0);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;

                button.setLayoutParams(layoutParams);
                button.setId(i);
                button.setText(buttonTexts[i]);
                button.setBackgroundColor(Color.parseColor("#FFF82831"));
                button.setTextColor(Color.parseColor("#FFFFFF"));


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp1 != null){
                            mp1.reset();
                            mp1.release();
                        }
                        int resId = getResources().getIdentifier("raw/" + sesTexts[button.getId()],null,getContext().getPackageName());
                        mp1 = MediaPlayer.create(getContext(),resId);

                        mp1.start();
                    }
                });

                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final int resId = getResources().getIdentifier("raw/" + sesTexts[button.getId()],null,getContext().getPackageName());
                        PopupMenu popupMenu = new PopupMenu(getContext(),button);
                        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId() == R.id.menuPaylas){
                                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                                        sesPaylas(sesTexts[button.getId()], resId);
                                    }else{
                                        AlertDialog.Builder builder;
                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                            builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                                        }else{
                                            builder = new AlertDialog.Builder(getContext());
                                        }
                                        builder.setTitle("Erişim izni")
                                                .setMessage("Paylaşmak için erişim izni vermelisiniz.İzin vermek istiyor musunuz?")
                                                .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        ActivityCompat.requestPermissions(getActivity(),
                                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                                1);
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(getContext(), "Zil Sesi Yapılamadı", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                                    }
                                }

                                if(item.getItemId() == R.id.menuFavoriEkle){

                                }

                                if(item.getItemId() == R.id.menuzilSesiYap){
                                    boolean retVal = true;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        retVal = Settings.System.canWrite(getContext());

                                        if(retVal){
                                            saveas(RingtoneManager.TYPE_RINGTONE,sesTexts[button.getId()],resId);
                                            Toast.makeText(getContext(), "Zil Sesi Olarak Ayarlandı", Toast.LENGTH_SHORT).show();
                                        }else{
                                            AlertDialog.Builder builder;
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                                builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                                            }else{
                                                builder = new AlertDialog.Builder(getContext());
                                            }
                                            builder.setTitle("Zil sesi ayarı")
                                                    .setMessage("Yapabilmek için izin vermelisiniz.İzin vermek istiyor musunuz?")
                                                    .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Toast.makeText(getContext(), "Zil Sesi Yapılamadı", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                                        }
                                    }else{
                                        saveas(RingtoneManager.TYPE_RINGTONE,sesTexts[button.getId()],resId);
                                        Toast.makeText(getContext(), "Zil Sesi Olarak Ayarlandı", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                if(item.getItemId() == R.id.menuBildirimSesiYap){
                                    boolean retVal = true;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        retVal = Settings.System.canWrite(getContext());

                                        if(retVal){
                                            saveas(RingtoneManager.TYPE_NOTIFICATION,sesTexts[button.getId()],resId);
                                            Toast.makeText(getContext(), "Bildirim Sesi Olarak Ayarlandı", Toast.LENGTH_SHORT).show();
                                        }else{
                                            AlertDialog.Builder builder;
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                                builder = new AlertDialog.Builder(getContext(),android.R.style.Theme_Material_Dialog_Alert);
                                            }else{
                                                builder = new AlertDialog.Builder(getContext());
                                            }
                                            builder.setTitle("Bildirim sesi ayarı")
                                                    .setMessage("Yapabilmek için izin vermelisiniz.İzin vermek istiyor musunuz?")
                                                    .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Toast.makeText(getContext(), "Bildirim Sesi Yapılamadı", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                                        }
                                    }else{
                                        saveas(RingtoneManager.TYPE_NOTIFICATION,sesTexts[button.getId()],resId);
                                        Toast.makeText(getContext(), "Bildirim Sesi Olarak Ayarlandı", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                return false;
                            }
                        });

                        popupMenu.show();

                        return false;
                    }
                });

                tableRow.addView(button);
                i++;

                if(i == buttonTexts.length){
                    int buttonHeight2 = (int) (50 * scale + 0.5f);;
                    TableRow tableRow2 = new TableRow(getContext());
                    TableLayout.LayoutParams tL2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT,1.0f);

                    tL2.setMargins(50,marginsTBPx,0,0);
                    tableRow2.setLayoutParams(tL);
                    table.addView(tableRow2);

                    Button btn = new Button(getContext());
                    btn.setWidth(buttonWidth);
                    btn.setHeight(buttonHeight2);
                    tableRow2.addView(btn);

                }

            }
        }




        return rootView;
    }


    public void sesPaylas(String sesAdi,int id){
        InputStream inputStream;
        FileOutputStream fileOutputStream;

        try {

            String path = Environment.getExternalStorageDirectory().getPath()
                    + "/media/audio/ringtones/";

            boolean exists = (new File(path)).exists();
            if (!exists) {
                new File(path).mkdirs();
            }

            inputStream = getResources().openRawResource(id);
            fileOutputStream = new FileOutputStream(
                    new File(Environment.getExternalStorageDirectory() + "/media/audio/ringtones", sesAdi + ".mp3" ));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/media/audio/ringtones" + "/" + sesAdi + ".mp3"));
        intent.setType("audio/mp3");
        startActivity(Intent.createChooser(intent, "Share sound"));


    }

    public boolean saveas(int type,String sesAdi,int rId) {
        byte[] buffer = null;

        InputStream fIn = getActivity().getBaseContext().getResources().openRawResource(
                rId);
        int size = 0;

        try {
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
        } catch (IOException e) {
            return false;
        }

        String path = Environment.getExternalStorageDirectory().getPath()
                + "/media/audio/ringtones/";

        String filename = sesAdi + ".mp3";

        boolean exists = (new File(path)).exists();
        if (!exists) {
            new File(path).mkdirs();
        }

        FileOutputStream save;
        try {
            save = new FileOutputStream(path + filename);
            save.write(buffer);
            save.flush();
            save.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + path + filename)));

        File k = new File(path, filename);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, filename);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");


        if (RingtoneManager.TYPE_RINGTONE == type) {
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        } else if (RingtoneManager.TYPE_NOTIFICATION == type) {
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        } else if (RingtoneManager.TYPE_ALARM == type) {
            values.put(MediaStore.Audio.Media.IS_ALARM, true);
        }

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(k
                .getAbsolutePath());

        Uri newUri = getContext().getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(getActivity(), type,
                newUri);

        getContext().getContentResolver()
                .insert(MediaStore.Audio.Media.getContentUriForPath(k
                        .getAbsolutePath()), values);

        return true;
    }






}

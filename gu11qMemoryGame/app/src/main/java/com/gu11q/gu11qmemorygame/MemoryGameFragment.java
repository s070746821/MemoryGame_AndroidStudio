package com.gu11q.gu11qmemorygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.media.MediaPlayer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MemoryGameFragment extends Fragment   {


    List<String> filenameList;
    List<String> filestoplay;
    AssetManager assets;
    String[] filelist;
    LinearLayout[] rows;
    MediaPlayer player;
    MediaPlayer lose;
    MediaPlayer Victory;
    MediaPlayer LoseGame;
    boolean isFirst;
    String currentPic;
    int Settingrows;
    int correct;
    SharedPreferences prefs;
    int Hiscore;
    Timer timer;
    TimerTask task;
    int time;






    public MemoryGameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        View view=inflater.inflate(R.layout.fragment_memory_game, container, false);

        prefs= PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String initial= prefs.getString("pref_number_of_choices", "10");




        Settingrows= Integer.parseInt(initial) / 2;


        try{
            assets= getActivity().getAssets();
            filelist=assets.list("");
            filenameList= new ArrayList<String>();
            rows= new LinearLayout[5];
            player= MediaPlayer.create(this.getActivity(), R.raw.cardflip);
            lose= MediaPlayer.create(this.getActivity(), R.raw.wrong);
            Victory= MediaPlayer.create(this.getActivity(), R.raw.winning);
            LoseGame= MediaPlayer.create(this.getActivity(), R.raw.losegame);
            isFirst=true;
            correct=0;
            Hiscore=1000;

            rows[0] =(LinearLayout) view.findViewById(R.id.row1);
            rows[1] =(LinearLayout) view.findViewById(R.id.row2);
            rows[2] =(LinearLayout) view.findViewById(R.id.row3);
            rows[3] =(LinearLayout) view.findViewById(R.id.row4);
            rows[4] =(LinearLayout) view.findViewById(R.id.row5);

            for(int i=0; i<filelist.length;i++){





                if(!filelist[i].equals("images")&&!filelist[i].equals("sounds")&&!filelist[i].equals("webkit"))
                    filenameList.add(filelist[i]);




            }



            Collections.shuffle(filenameList);



            filestoplay= new ArrayList<String>();

            for(int i=0; i<Settingrows*2; i++){

                filestoplay.add(filenameList.get(i));
                filestoplay.add(filenameList.get(i));
            }

            for (String file : filestoplay){
                System.out.println(file);
            }

            Collections.shuffle(filestoplay);


            int filestoplayCount=0;

            for(int i=0; i<Settingrows; i++){

                LinearLayout row= rows[i];
                for (int column = 0; column < row.getChildCount(); column++) {
                    ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                    imagebutton.setContentDescription(filestoplay.get(filestoplayCount));
                    filestoplayCount++;
                    imagebutton.setOnClickListener(guessButtonListener);
                }
            }

            for(int i=0; i<5; i++){

                LinearLayout row= rows[i];
                for (int column = 0; column < row.getChildCount(); column++) {
                    ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                    imagebutton.setVisibility(View.INVISIBLE);


                }
            }
            // display appropriate guess button LinearLayouts
            for(int i=0; i<Settingrows; i++){

                LinearLayout row= rows[i];
                for (int column = 0; column < row.getChildCount(); column++) {
                    ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                    imagebutton.setVisibility(View.VISIBLE);


                }
            }






            System.out.println("that's all folks");
        }
        catch(IOException ie){
            ie.printStackTrace();
        }


        final Handler ahandler = new Handler();
        time=0;


        timer=new Timer(false);
        task = new TimerTask() {
            @Override
            public void run() {
                ahandler.post(new Runnable() {
                    @Override
                    public void run() {

                        time++;
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000,1000);











        return view;
    }


    private View.OnClickListener guessButtonListener = new View.OnClickListener() {


         public void onClick(View v) {


            ImageButton guessButton = ((ImageButton) v);

            String fname= v.getContentDescription().toString().trim();


             try (InputStream stream = assets.open(fname)) {




                 Drawable img = Drawable.createFromStream(stream, fname);

                 ((ImageButton) v).setImageDrawable(img);
                 player.start();




                 if(isFirst){

                     currentPic=fname;
                     ((ImageButton) v).setClickable(false);
                     isFirst=false;


                 }
                 else{

                     for(int i=0; i<Settingrows; i++){

                         LinearLayout row= rows[i];
                         for (int column = 0; column < row.getChildCount(); column++) {
                             ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                             imagebutton.setClickable(false);


                         }
                     }

                     final String myfname= fname;

                     Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             if(myfname==currentPic){





                                 for(int i=0; i<Settingrows; i++){

                                     LinearLayout row= rows[i];
                                     for (int column = 0; column < row.getChildCount(); column++) {
                                         ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                                         if(imagebutton.getContentDescription().toString().trim().equals(myfname)){
                                             imagebutton.setVisibility(View.INVISIBLE);

                                         }

                                     }
                                 }
                                 correct++;
                                 System.out.println("THe number of correct is "+correct +"rows is "+Settingrows);
                                 if(correct>=Settingrows*2){


                                     victory();
                                 }

                             }
                             else{
                                 lose.start();
                                 for(int i=0; i<Settingrows; i++){

                                     LinearLayout row= rows[i];
                                     for (int column = 0; column < row.getChildCount(); column++) {
                                         ImageButton imagebutton = (ImageButton) row.getChildAt(column);

                                         imagebutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));



                                     }
                                 }
                                 Hiscore-=40;
                                 if(Hiscore<=0){
                                     defeat();
                                 }


                             }

                             for(int i=0; i<Settingrows; i++){

                                 LinearLayout row= rows[i];
                                 for (int column = 0; column < row.getChildCount(); column++) {
                                     ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                                     if(imagebutton.getVisibility()!=View.INVISIBLE){
                                         imagebutton.setClickable(true);
                                     }

                                 }
                             }

                             //Do something after 100ms
                         }
                     }, 500);









                     isFirst=true;

                 }




             }
             catch (IOException exception) {

                 Log.e("buttonclick", "error loading");

             }














         }
    };


    public void SettingChange(SharedPreferences sharedPreferences){

        String choices = sharedPreferences.getString("pref_number_of_choices", null);
        Settingrows = Integer.parseInt(choices) / 2;



        restartAPP();




    }

    public void restartAPP(){

        Collections.shuffle(filenameList);
        filestoplay.clear();

        for(int i=0; i<Settingrows*2; i++){

            filestoplay.add(filenameList.get(i));
            filestoplay.add(filenameList.get(i));
        }

        for (String file : filestoplay){
            System.out.println(file);
        }

        Collections.shuffle(filestoplay);


        int filestoplayCount=0;

        for(int i=0; i<Settingrows; i++){

            LinearLayout row= rows[i];
            for (int column = 0; column < row.getChildCount(); column++) {
                ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                imagebutton.setContentDescription(filestoplay.get(filestoplayCount));
                imagebutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));

                filestoplayCount++;
                imagebutton.setOnClickListener(guessButtonListener);
            }
        }

        for(int i=0; i<5; i++){

            LinearLayout row= rows[i];
            for (int column = 0; column < row.getChildCount(); column++) {
                ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                imagebutton.setVisibility(View.INVISIBLE);


            }
        }
        // display appropriate guess button LinearLayouts
        for(int i=0; i<Settingrows; i++){

            LinearLayout row= rows[i];
            for (int column = 0; column < row.getChildCount(); column++) {
                ImageButton imagebutton = (ImageButton) row.getChildAt(column);
                imagebutton.setVisibility(View.VISIBLE);


            }
        }

        isFirst=true;
        correct=0;
        Hiscore=1000;
        time=0;
    }


    public void victory(){

        Victory.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Congratulations!!!");
        builder.setMessage("Your Score is "+ Hiscore+ " with a time of "+ time+" seconds");




        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartAPP();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                System.exit(0);
            }
        });

        AlertDialog alert= builder.create();
        alert.show();


    }

    public void defeat(){

        LoseGame.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Oh no!");
        builder.setMessage("Your Score reached zero"+ " You played for "+ time+" seconds");




        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartAPP();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                System.exit(0);
            }
        });

        AlertDialog alert= builder.create();
        alert.show();

    }







}

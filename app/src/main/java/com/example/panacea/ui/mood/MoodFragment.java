package com.example.panacea.ui.mood;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.panacea.R;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;
import java.util.Vector;

public class MoodFragment extends Fragment {

    private MoodViewModel moodViewModel;
    SeekBar anxiety,anger,hopelessness,boredom,sadness;
    TextView anxiety_points,anger_points,hopelessness_points,boredom_points,sadness_points;
    Button getContentButton,provideContentButton;
    TextView heading,anx,ang,hop,bor,sad;
    static LinearLayout.LayoutParams params;
    static LinearLayout myLinearLayout;
    static ScrollView myScrollView;
    static View root;
    String myGames[];

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        moodViewModel =
                ViewModelProviders.of(this).get(MoodViewModel.class);
        root = inflater.inflate(R.layout.fragment_mood, container, false);
        moodViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        heading=(TextView)root.findViewById(R.id.textView12);
        anx=(TextView)root.findViewById(R.id.textView7);
        ang=(TextView)root.findViewById(R.id.textView8);
        hop=(TextView)root.findViewById(R.id.textView9);
        bor=(TextView)root.findViewById(R.id.textView10);
        sad=(TextView)root.findViewById(R.id.textView11);
        anxiety= (SeekBar) root.findViewById(R.id.seekBar8);
        anger= (SeekBar) root.findViewById(R.id.seekBar9);
        hopelessness= (SeekBar) root.findViewById(R.id.seekBar10);
        boredom= (SeekBar) root.findViewById(R.id.seekBar11);
        sadness= (SeekBar) root.findViewById(R.id.seekBar12);
        myScrollView=(ScrollView)root.findViewById(R.id.myScrollView);
        myLinearLayout=(LinearLayout)root.findViewById(R.id.myLinearLayout);
        myScrollView.setVisibility(View.GONE);
        myLinearLayout.setVisibility(View.GONE);
        anxiety.setMax(5);
        anxiety.setProgress(0);
        anger.setMax(5);
        anger.setProgress(0);
        hopelessness.setMax(5);
        hopelessness.setProgress(0);
        boredom.setMax(5);
        boredom.setProgress(0);
        sadness.setMax(5);
        sadness.setProgress(0);
        anxiety_points=(TextView)root.findViewById(R.id.textView13);
        anxiety_points.setText("0");
        anger_points=(TextView)root.findViewById(R.id.textView14);
        anger_points.setText("0");
        hopelessness_points=(TextView)root.findViewById(R.id.textView15);
        hopelessness_points.setText("0");
        boredom_points=(TextView)root.findViewById(R.id.textView18);
        boredom_points.setText("0");
        sadness_points=(TextView)root.findViewById(R.id.textView21);
        sadness_points.setText("0");
        getContentButton=(Button)root.findViewById(R.id.button12);
        provideContentButton=(Button)root.findViewById(R.id.button13);
        anxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                anxiety_points.setText(Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        anger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                anger_points.setText(Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        hopelessness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hopelessness_points.setText(Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        boredom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                boredom_points.setText(Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sadness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sadness_points.setText(Integer.toString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        getContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent(anxiety.getProgress(),anger.getProgress(),hopelessness.getProgress(),boredom.getProgress(),sadness.getProgress());
            }
        });
        provideContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProvideContentForm();
            }
        });
        return root;
    }
    public void getContent(int anx_points,int ang_points,int hope_points,int bore_points,int sad_points) {
        heading.setText("Loading...");
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(getActivity()));
        Python py=Python.getInstance();
        PyObject pyobj=py.getModule("MoodBasedContent");
        PyObject obj=pyobj.callAttr("get_content",anx_points,ang_points,hope_points,bore_points,sad_points);
        String result=obj.toString();
        int x=result.lastIndexOf('[');
        String movies=result.substring(x,result.length());
        result=result.substring(0,x);
        x=result.lastIndexOf('[');
        String games=result.substring(x,result.length());
        result=result.substring(0,x);
        String memes=result;
        memes=memes.substring(1,memes.length()-1);
        games=games.substring(1,games.length()-1);
        movies=movies.substring(1,movies.length()-1);
        String memearr[]=memes.split(",");
        String gamearr[]=games.split(",");
        String moviearr[]=movies.split(",");
        Vector<String> memelist,gamelist,movielist;
        memelist=new Vector<String>();
        gamelist=new Vector<String>();
        movielist=new Vector<String>();
        for(int i=0;i<memearr.length;i++) {
            memearr[i]=memearr[i].trim();
            memelist.add(memearr[i].substring(1,memearr[i].length()-1));
        }
        for(int i=0;i<gamearr.length;i++) {
            gamearr[i]=gamearr[i].trim();
            gamelist.add(gamearr[i].substring(1,gamearr[i].length()-1));
        }
        for(int i=0;i<moviearr.length;i++) {
            moviearr[i]=moviearr[i].trim();
            movielist.add(moviearr[i].substring(1,moviearr[i].length()-1));
        }
        /*String t1="Memes: ",t2="Games: ",t3="Movies: ";
        for(int i=0;i<memelist.size();i++) {
            if(i==memelist.size()-1)
                t1=t1+memelist.get(i);
            else
                t1=t1+memelist.get(i)+",";
        }
        for(int i=0;i<gamelist.size();i++) {
            if(i==gamelist.size()-1)
                t2=t2+gamelist.get(i);
            else
                t2=t2+gamelist.get(i)+",";
        }
        for(int i=0;i<movielist.size();i++) {
            if(i==movielist.size()-1)
                t3=t3+movielist.get(i);
            else
                t3=t3+movielist.get(i)+",";
        }
        anxiety_points.setText(t1);
        anger_points.setText(t2);
        hopelessness_points.setText(t3);*/
        showContent(memelist,gamelist,movielist);
    }
    public void showContent(Vector<String> memelist,Vector<String> gamelist,Vector<String> movielist) {
        heading.setText("Content based on you Mood");
        anx.setVisibility(View.GONE);
        ang.setVisibility(View.GONE);
        hop.setVisibility(View.GONE);
        bor.setVisibility(View.GONE);
        sad.setVisibility(View.GONE);
        anxiety.setVisibility(View.GONE);
        anger.setVisibility(View.GONE);
        hopelessness.setVisibility(View.GONE);
        boredom.setVisibility(View.GONE);
        sadness.setVisibility(View.GONE);
        anxiety_points.setVisibility(View.GONE);
        anger_points.setVisibility(View.GONE);
        hopelessness_points.setVisibility(View.GONE);
        boredom_points.setVisibility(View.GONE);
        sadness_points.setVisibility(View.GONE);
        getContentButton.setVisibility(View.GONE);
        provideContentButton.setVisibility(View.GONE);
        myScrollView.setVisibility(View.VISIBLE);
        myLinearLayout.setVisibility(View.VISIBLE);
        heading.setVisibility(View.GONE);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
        String memelistarr="";
        String gamelistarr="";
        String movielistarr="";
        for(int i=0;i<memelist.size();i++)
            memelistarr=memelistarr+memelist.get(i)+" ";
        memelistarr=memelistarr.trim();
        for(int i=0;i<gamelist.size();i++)
            gamelistarr=gamelistarr+gamelist.get(i)+"@";
        gamelistarr=gamelistarr.substring(0,gamelistarr.length()-1);
        for(int i=0;i<movielist.size();i++)
            movielistarr=movielistarr+movielist.get(i)+"@";
        movielistarr=movielistarr.substring(0,movielistarr.length()-1);
        Python py=Python.getInstance();
        PyObject pyobj=py.getModule("getContentList");
        PyObject selectedMemes=pyobj.callAttr("get_memes",memelistarr);
        PyObject selectedGames=pyobj.callAttr("get_games",gamelistarr);
        PyObject selectedMovies=pyobj.callAttr("get_movies",movielistarr);
        /*ImageView memeImages=new ImageView(getActivity());
        Glide.with(getActivity()).load(sc.next()).into(memeImages);
        sc.close();
        myLinearLayout.addView(memeImages);*/
        String longMemeString=selectedMemes.toString();
        String myMemes[]=longMemeString.split(" ");
        ImageView memeImages[]=new ImageView[myMemes.length];
        TextView titleText=new TextView(getActivity());
        titleText.setLayoutParams(params);
        titleText.setText("Mood Based on your Content");
        titleText.setTextSize(30);
        myLinearLayout.addView(titleText);
        TextView temp1=new TextView(getActivity());
        temp1.setLayoutParams(params);
        myLinearLayout.addView(temp1);
        TextView memeheading=new TextView(getActivity());
        memeheading.setLayoutParams(params);
        memeheading.setTextSize(23);
        memeheading.setText("Memes:");
        myLinearLayout.addView(memeheading);
        TextView temp2=new TextView(getActivity());
        temp1.setLayoutParams(params);
        myLinearLayout.addView(temp2);
        for(int i=0;i<memeImages.length;i++)
        {
            memeImages[i]=new ImageView(getActivity());
            Glide.with(getActivity()).load(myMemes[i]).into(memeImages[i]);
            memeImages[i].setPadding(25,50,25,60);
            myLinearLayout.addView(memeImages[i]);
        }
        TextView temp3=new TextView(getActivity());
        temp1.setLayoutParams(params);
        myLinearLayout.addView(temp3);
        TextView gameheading=new TextView(getActivity());
        gameheading.setLayoutParams(params);
        gameheading.setTextSize(23);
        gameheading.setText("Games:");
        myLinearLayout.addView(gameheading);
        TextView temp4=new TextView(getActivity());
        temp1.setLayoutParams(params);
        myLinearLayout.addView(temp4);
        //games
        String longGameString=selectedGames.toString();
        myGames=longGameString.split("@");
        Button gameTexts[]=new Button[myGames.length/2];
        int j=0;
        for(int i=0;i<gameTexts.length;i++,j+=2)
        {
            gameTexts[i]=new Button(getActivity());
            gameTexts[i].setLayoutParams(params);
            gameTexts[i].setText(myGames[j]);
            gameTexts[i].setId(j+1);
            gameTexts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoGameLink(view.getId());
                }
            });
            myLinearLayout.addView(gameTexts[i]);
        }
        TextView temp5=new TextView(getActivity());
        temp1.setLayoutParams(params);
        myLinearLayout.addView(temp5);
        TextView movieheading=new TextView(getActivity());
        movieheading.setLayoutParams(params);
        movieheading.setTextSize(23);
        movieheading.setText("Movies:");
        myLinearLayout.addView(movieheading);
        TextView temp6=new TextView(getActivity());
        temp1.setLayoutParams(params);
        myLinearLayout.addView(temp6);
        //movies
        String longMovieString=selectedMovies.toString();
        String myMovies[]=longMovieString.split("@");
        TextView movieTexts[]=new TextView[myMovies.length/2];
        j=0;
        for(int i=0;i<movieTexts.length;i++,j+=2)
        {
            movieTexts[i]=new TextView(getActivity());
            movieTexts[i].setLayoutParams(params);
            movieTexts[i].setText(myMovies[j]+" ("+myMovies[j+1]+")");
            myLinearLayout.addView(movieTexts[i]);
        }
        Button feedbackButton=new Button(getActivity());
        feedbackButton.setLayoutParams(params);
        feedbackButton.setText("Provide Feedback");
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGoogleForm();
            }
        });
        feedbackButton.setGravity(Gravity.CENTER_HORIZONTAL);
        myLinearLayout.addView(feedbackButton);
    }
    public void gotoGameLink(int id)
    {
        Uri uri=Uri.parse(myGames[id]);
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    public void gotoGoogleForm()
    {
        Uri uri=Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScAUZiXikW4TuVnUfnYFFNQIKnn--AnJ0RYhOD4dXXrSo9cKg/viewform?usp=sf_link");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    public void gotoProvideContentForm()
    {
        Uri uri=Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdsJjul382BQ4SndK9M6g3kGBxHw5cjc6QCo0pvBL1M8b1DBw/viewform?usp=sf_link");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
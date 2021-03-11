package com.example.panacea.ui.mood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.panacea.R;

import org.w3c.dom.Text;

import java.util.Vector;

public class MoodFragment extends Fragment {

    private MoodViewModel moodViewModel;
    SeekBar anxiety,anger,hopelessness,boredom,sadness;
    TextView anxiety_points,anger_points,hopelessness_points,boredom_points,sadness_points;
    Button getContentButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        moodViewModel =
                ViewModelProviders.of(this).get(MoodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mood, container, false);
        moodViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        anxiety= (SeekBar) root.findViewById(R.id.seekBar8);
        anger= (SeekBar) root.findViewById(R.id.seekBar9);
        hopelessness= (SeekBar) root.findViewById(R.id.seekBar10);
        boredom= (SeekBar) root.findViewById(R.id.seekBar11);
        sadness= (SeekBar) root.findViewById(R.id.seekBar12);
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
        return root;
    }
    public void getContent(int anx_points,int ang_points,int hope_points,int bore_points,int sad_points) {
        getContentButton.setText("Loading...");
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
        getContentButton.setText("Get Content");
    }
}
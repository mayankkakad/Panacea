package com.example.panacea.ui.motivation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.panacea.MainActivity;
import com.example.panacea.R;

public class MotivationFragment extends Fragment {

    private MotivationViewModel motivationViewModel;
    TextView quoteBox;
    TextView authorBox;
    Button nextQuote,previousQuote,submitQuote,likeButton,dislikeButton;
    static int curr_quote=0;
    String quotes[],authors[];
    static PyObject pyobj;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        motivationViewModel =
                ViewModelProviders.of(this).get(MotivationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_motivation, container, false);
        motivationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        quoteBox=(TextView)root.findViewById(R.id.editTextTextMultiLine2);
        authorBox=(TextView)root.findViewById(R.id.textView16);
        nextQuote=(Button)root.findViewById(R.id.button15);
        previousQuote=(Button)root.findViewById(R.id.button16);
        submitQuote=(Button)root.findViewById(R.id.button17);
        likeButton=(Button)root.findViewById(R.id.button18);
        dislikeButton=(Button)root.findViewById(R.id.button14);
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(getActivity()));
        Python py=Python.getInstance();
        pyobj=py.getModule("GetQuotes");
        PyObject obj=pyobj.callAttr("get_quotes");
        String finalstring=obj.toString();
        String quoteauthor[]=finalstring.split("@");
        quotes=new String[quoteauthor.length/2];
        authors=new String[quoteauthor.length/2];
        for(int i=0,j=0;i<quoteauthor.length/2;i++) {
            quotes[i]=quoteauthor[j++];
            authors[i]=quoteauthor[j++];
        }
        quoteBox.setText(quotes[curr_quote]);
        authorBox.setText(authors[curr_quote]);
        nextQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNextQuote();
            }
        });
        previousQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPreviousQuote();
            }
        });
        submitQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGoogleForm();
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PyObject obj1=pyobj.callAttr("like_quote",curr_quote);
                Toast.makeText(getActivity(),"Quote Liked",Toast.LENGTH_SHORT).show();
            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PyObject obj2=pyobj.callAttr("dislike_quote",curr_quote);
                Toast.makeText(getActivity(),"Quote disliked",Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    public void displayNextQuote() {
        curr_quote++;
        if(curr_quote==quotes.length)
            curr_quote=0;
        quoteBox.setText(quotes[curr_quote]);
        authorBox.setText(authors[curr_quote]);
    }
    public void displayPreviousQuote() {
        curr_quote--;
        if(curr_quote==-1)
            curr_quote=quotes.length-1;
        quoteBox.setText(quotes[curr_quote]);
        authorBox.setText(authors[curr_quote]);
    }
    public void gotoGoogleForm() {
        Uri uri=Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdHhbWHgP1UYfg1Cri2mXLEWUEzYo2wu5W0L77Xp1oo_stSjQ/viewform?usp=sf_link");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
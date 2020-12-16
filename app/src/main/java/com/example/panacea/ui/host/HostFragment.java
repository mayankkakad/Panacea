package com.example.panacea.ui.host;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.panacea.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.panacea.MainActivity;
import com.example.panacea.R;
import com.example.panacea.ui.play.PlayFragment;
import com.example.panacea.ui.sports.SportsFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HostFragment extends Fragment {

    private HostViewModel hostViewModel;
    static Vector<String> items;
    FirebaseFirestore db;
    static Spinner sport;
    static View root;
    Button hostButton;
    TextView req,loc;
    EditText avail,need;
    MapView mv;
    Button backButton,startGame;
    static boolean flag=false;
    static String myName;
    static int av,nd;
    static String temp;
    static Vector<String> requestse,requestsn;
    static LinearLayout myLeft,myRight;
    static TextView namet[],aget[];
    static Button acceptb[],rejectb[];
    static LinearLayout.LayoutParams lparams,tparams;
    static DocumentReference docRef[];

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        hostViewModel =
                ViewModelProviders.of(this).get(HostViewModel.class);
        root = inflater.inflate(R.layout.fragment_host, container, false);
        db=FirebaseFirestore.getInstance();
        items=new Vector<String>();
        startGame=root.findViewById(R.id.button8);
        startGame.setVisibility(View.GONE);
        sport=root.findViewById(R.id.spinner2);
        requestse=new Vector<String>();
        requestsn=new Vector<String>();
        req=root.findViewById(R.id.textView5);
        req.setVisibility(View.INVISIBLE);
        mv=root.findViewById(R.id.mapView);
        loc=root.findViewById(R.id.textView4);
        hostButton=root.findViewById(R.id.button4);
        avail=root.findViewById(R.id.editTextNumber);
        need=root.findViewById(R.id.editTextNumber2);
        backButton=root.findViewById(R.id.button7);
        db.collection("sports").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        items.add(document.get("sport").toString());
                    }
                    String it[]=new String[items.size()+1];
                    it[0]="Select Sport";
                    for(int i=0;i<items.size();i++)
                        it[i+1]=items.get(i);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, it);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sport.setAdapter(adapter);
                }
            }
        });
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(avail.getText().toString().equals("")) {
                    avail.setError("Cannot be empty!");
                    avail.requestFocus();
                }
                else if(need.getText().toString().equals("")) {
                    need.setError("Cannot be empty!");
                    need.requestFocus();
                }
                else {
                    av=Integer.parseInt(avail.getText().toString());
                    nd=Integer.parseInt(need.getText().toString());
                    Constants.sport=sport.getSelectedItem().toString();
                    gotoRequests();
                }
            }
        });
        final DocumentReference docRef=db.collection(MainActivity.loggedemail).document("sport");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()&&document.get("sport")!=null) {
                        Constants.sport = document.get("sport").toString();
                        sport.setVisibility(View.GONE);
                        avail.setVisibility(View.GONE);
                        need.setVisibility(View.GONE);
                        hostButton.setVisibility(View.GONE);
                        loc.setVisibility(View.GONE);
                        mv.setVisibility(View.GONE);
                        req.setVisibility(View.VISIBLE);
                        startGame.setVisibility(View.VISIBLE);
                        showRequests();
                    }
                }
            }
        });
        if(Constants.sport!=null) {
            sport.setVisibility(View.GONE);
            avail.setVisibility(View.GONE);
            need.setVisibility(View.GONE);
            hostButton.setVisibility(View.GONE);
            loc.setVisibility(View.GONE);
            mv.setVisibility(View.GONE);
            req.setVisibility(View.VISIBLE);
            startGame.setVisibility(View.VISIBLE);
            showRequests();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference delRole=db.collection("users").document(MainActivity.loggedemail);
                Map<String,Object> data=new HashMap<>();
                data.put("role", FieldValue.delete());
                data.put("sport",FieldValue.delete());
                delRole.update(data);
                db.collection(MainActivity.loggedemail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        }
                    }
                });
                try{db.collection(Constants.sport).document(MainActivity.loggedemail).delete();}catch(Exception e){}
                Constants.sport=null;
                Constants.requests=null;
                SportsFragment sf=new SportsFragment();
                FragmentManager manager=getParentFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.nav_host_fragment,sf)
                        .commit();
            }
        });
        return root;
    }
    public void gotoRequests() {
        sport.setVisibility(View.GONE);
        avail.setVisibility(View.GONE);
        need.setVisibility(View.GONE);
        hostButton.setVisibility(View.GONE);
        loc.setVisibility(View.GONE);
        mv.setVisibility(View.GONE);
        req.setVisibility(View.VISIBLE);
        startGame.setVisibility(View.VISIBLE);
        DocumentReference docRef = db.collection("users").document(MainActivity.loggedemail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("name")!=null)
                            myName=document.get("name").toString();
                        else
                            myName="Unknown";
                    }
                    Map<String,Object> data= new HashMap<>();
                    data.put("email",MainActivity.loggedemail);
                    data.put("name",myName);
                    data.put("available",av);
                    data.put("need",nd);
                    Map<String,Object> data1=new HashMap<>();
                    data1.put("sport",sport.getSelectedItem());
                    db.collection(sport.getSelectedItem().toString()).document(MainActivity.loggedemail).set(data);
                    db.collection(MainActivity.loggedemail).document("sport").set(data1);
                    showRequests();
                }
            }
        });
        Map<String,Object> data=new HashMap<>();
        data.put("role","host");
        data.put("sport",sport.getSelectedItem().toString());
        db.collection("users").document(MainActivity.loggedemail).update(data);
    }
    public void showRequests() {
        myLeft=(LinearLayout)root.findViewById(R.id.myLeft);
        myRight=(LinearLayout)root.findViewById(R.id.myRight);
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
        tparams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,120);
        db.collection(MainActivity.loggedemail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.get("status")==null)
                            continue;
                        if(document.get("status").toString().equals("pending")) {
                            requestse.add(document.get("email").toString());
                            if(document.get("name").toString()!=null)
                                requestsn.add(document.get("name").toString());
                            else
                                requestsn.add("Unknown");
                        }
                    }
                    namet=new TextView[requestse.size()];
                    aget=new TextView[requestse.size()];
                    acceptb=new Button[requestse.size()];
                    rejectb=new Button[requestse.size()];
                    for(int i=0;i<requestse.size();i++) {
                        Constants.requests=new Vector<String>();
                        Constants.requests.add(requestse.get(i));
                        namet[i]=new TextView(getActivity());
                        aget[i]=new TextView(getActivity());
                        acceptb[i]=new Button(getActivity());
                        rejectb[i]=new Button(getActivity());
                        acceptb[i].setId(i);
                        rejectb[i].setId(i+1);
                        namet[i].setLayoutParams(lparams);
                        aget[i].setLayoutParams(lparams);
                        acceptb[i].setLayoutParams(lparams);
                        rejectb[i].setLayoutParams(lparams);
                        namet[i].setTextSize(20);
                        aget[i].setTextSize(20);
                        acceptb[i].setTextSize(14);
                        rejectb[i].setTextSize(14);
                        namet[i].setText(requestsn.get(i));
                        aget[i].setText("Age: ");
                        acceptb[i].setText("Accept");
                        rejectb[i].setText("Reject");
                        TextView t1=new TextView(getActivity());
                        TextView t2=new TextView(getActivity());
                        t1.setLayoutParams(tparams);
                        t2.setLayoutParams(tparams);
                        myLeft.addView(namet[i]);
                        myRight.addView(acceptb[i]);
                        myLeft.addView(aget[i]);
                        myRight.addView(rejectb[i]);
                        myLeft.addView(t1);
                        myRight.addView(t2);
                        acceptb[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                acceptRequest(view.getId());
                            }
                        });
                        rejectb[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                rejectRequest(view.getId());
                            }
                        });
                    }
                }
            }
        });
    }
    public void acceptRequest(int id) {
        db.collection(MainActivity.loggedemail).document(requestse.get(id)).update("status","accept");
        db.collection(Constants.sport).document(MainActivity.loggedemail).update("available", FieldValue.increment(1));
        db.collection(Constants.sport).document(MainActivity.loggedemail).update("need",FieldValue.increment(-1));
        Constants.playerlist=new Vector<String>();
        Constants.playerlist.add(requestse.get(id));
        HostFragment hf=new HostFragment();
        FragmentManager manager=getParentFragmentManager();
        manager.beginTransaction()
                .replace(R.id.nav_host_fragment,hf)
                .commit();
    }
    public void rejectRequest(int id) {
        db.collection(MainActivity.loggedemail).document(requestse.get(id-1)).update("status","reject");
        HostFragment hf=new HostFragment();
        FragmentManager manager=getParentFragmentManager();
        manager.beginTransaction()
                .replace(R.id.nav_host_fragment,hf)
                .commit();
    }
}
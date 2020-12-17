package com.example.panacea.ui.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.panacea.Constants;
import com.example.panacea.MainActivity;
import com.example.panacea.R;
import com.example.panacea.ui.host.HostViewModel;
import com.example.panacea.ui.sports.SportsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class PlayFragment extends Fragment {

    private PlayViewModel playViewModel;
    static Vector<String> items;
    FirebaseFirestore db;
    static Spinner sport;
    static View root;
    Button playButton,backButton;
    TextView hosts;
    static TextView names[],avails[],needs[];
    static Button reqbuts[];
    static Vector<String> namev,availv,needv,emailv;
    static LinearLayout myLayout,buttonLayout;
    static LinearLayout.LayoutParams lparams,tparams;
    static int index;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        playViewModel =
                ViewModelProviders.of(this).get(PlayViewModel.class);
        root = inflater.inflate(R.layout.fragment_play, container, false);
        db=FirebaseFirestore.getInstance();
        emailv=new Vector<String>();
        namev=new Vector<String>();
        availv=new Vector<String>();
        needv=new Vector<String>();
        backButton=root.findViewById(R.id.button6);
        playButton=root.findViewById(R.id.button5);
        hosts=root.findViewById(R.id.textView6);
        hosts.setVisibility(View.INVISIBLE);
        sport=root.findViewById(R.id.spinner);
        items=new Vector<String>();
        if(Constants.requests!=null) {
            sport.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
            hosts.setVisibility(View.VISIBLE);
            getHosts(Constants.playsport);
        }
        DocumentReference check=db.collection("users").document(MainActivity.loggedemail);
        check.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document=task.getResult();
                    if(document.exists()&&document.get("role")!=null) {
                        sport.setVisibility(View.GONE);
                        playButton.setVisibility(View.GONE);
                        hosts.setVisibility(View.VISIBLE);
                        Constants.requests=null;
                        getHosts(document.get("sport").toString());
                    }
                }
            }
        });
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
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sport.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                hosts.setVisibility(View.VISIBLE);
                Constants.requests=null;
                getHosts(sport.getSelectedItem().toString());
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference delRole=db.collection("users").document(MainActivity.loggedemail);
                Map<String,Object> data=new HashMap<>();
                data.put("role", FieldValue.delete());
                data.put("sport",FieldValue.delete());
                delRole.update(data);
                if(Constants.requests!=null) {
                    for(int i=0;i<Constants.requests.size();i++) {
                        db.collection(Constants.requests.get(i)).document(MainActivity.loggedemail).delete();
                    }
                }
                SportsFragment sf=new SportsFragment();
                FragmentManager manager=getParentFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.nav_host_fragment,sf)
                        .commit();
                Constants.requests=null;
            }
        });
        return root;
    }
    public void getHosts(String sp) {
        Constants.playsport=sp;
        myLayout=(LinearLayout)root.findViewById(R.id.myLayout);
        buttonLayout=(LinearLayout)root.findViewById(R.id.buttonLayout);
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
        tparams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,80);
        db.collection(sp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int counter=0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(counter++==0&&(Boolean)document.get("start")==true)
                            break;
                        if(Constants.requests!=null&&Constants.requests.contains(document.get("email").toString())) {
                            continue;
                        }
                        if(document.get("email").toString().equals(MainActivity.loggedemail))
                            continue;
                        if(document.get("name").toString()!=null)
                            namev.add(document.get("name").toString().split(" ")[0]);
                        else
                            namev.add("Unknown");
                        emailv.add(document.get("email").toString());
                        availv.add(document.get("available").toString());
                        needv.add(document.get("need").toString());
                    }
                    names=new TextView[namev.size()];
                    avails=new TextView[availv.size()];
                    needs=new TextView[needv.size()];
                    reqbuts=new Button[namev.size()];
                    for(int i=0;i<names.length;i++) {
                        names[i]=new TextView(getActivity());
                        names[i].setId(i);
                        avails[i]=new TextView(getActivity());
                        avails[i].setId(i+1);
                        needs[i]=new TextView(getActivity());
                        needs[i].setId(i+2);
                        reqbuts[i]=new Button(getActivity());
                        reqbuts[i].setId(i+3);
                        reqbuts[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendRequest(view.getId());
                            }
                        });
                        names[i].setLayoutParams(lparams);
                        avails[i].setLayoutParams(lparams);
                        needs[i].setLayoutParams(lparams);
                        reqbuts[i].setLayoutParams(lparams);
                        names[i].setTextSize(20);
                        avails[i].setTextSize(20);
                        needs[i].setTextSize(20);
                        reqbuts[i].setTextSize(14);
                        names[i].setText(namev.get(i));
                        avails[i].setText("Available: "+availv.get(i));
                        needs[i].setText("Need: "+needv.get(i));
                        reqbuts[i].setText("Request");
                        TextView t1=new TextView(getActivity());
                        TextView t2=new TextView(getActivity());
                        TextView t3=new TextView(getActivity());
                        TextView t4=new TextView(getActivity());
                        t1.setLayoutParams(lparams);
                        t1.setTextSize(20);
                        t1.setText("");
                        t2.setLayoutParams(lparams);
                        t2.setTextSize(20);
                        t2.setText("");
                        t3.setLayoutParams(tparams);
                        t3.setTextSize(20);
                        t3.setText("");
                        t4.setLayoutParams(tparams);
                        t4.setTextSize(20);
                        t4.setText("");
                        myLayout.addView(names[i]);
                        myLayout.addView(avails[i]);
                        buttonLayout.addView(t1);
                        buttonLayout.addView(reqbuts[i]);
                        myLayout.addView(needs[i]);
                        buttonLayout.addView(t2);
                        myLayout.addView(t3);
                        buttonLayout.addView(t4);
                    }
                }
            }
        });
    }
    public void sendRequest(int id) {
        index=id-3;
        DocumentReference docRef = db.collection("users").document(MainActivity.loggedemail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Map<String,String> data=new HashMap<>();
                        data.put("name",document.get("name").toString());
                        data.put("email",MainActivity.loggedemail);
                        data.put("status","pending");
                        db.collection(emailv.get(index)).document(MainActivity.loggedemail).set(data);
                        if(Constants.requests==null) {
                            Constants.requests=new Vector<String>();
                            Constants.requests.add(emailv.get(index));
                        }
                        else
                            Constants.requests.add(emailv.get(index));
                    }
                    PlayFragment pf=new PlayFragment();
                    FragmentManager manager=getParentFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.nav_host_fragment,pf)
                            .commit();
                }
            }
        });
        Map<String,Object> data=new HashMap<>();
        data.put("role","play");
        data.put("sport",sport.getSelectedItem().toString());
        db.collection("users").document(MainActivity.loggedemail).update(data);
    }
}
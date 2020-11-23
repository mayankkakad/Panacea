package com.example.panacea.ui.host;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.panacea.MainActivity;
import com.example.panacea.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        hostViewModel =
                ViewModelProviders.of(this).get(HostViewModel.class);
        root = inflater.inflate(R.layout.fragment_host, container, false);
        db=FirebaseFirestore.getInstance();
        items=new Vector<String>();
        req=root.findViewById(R.id.textView5);
        req.setVisibility(View.INVISIBLE);
        mv=root.findViewById(R.id.mapView);
        loc=root.findViewById(R.id.textView4);
        hostButton=root.findViewById(R.id.button4);
        avail=root.findViewById(R.id.editTextNumber);
        need=root.findViewById(R.id.editTextNumber2);
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
                    sport=root.findViewById(R.id.spinner2);
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
                else
                    gotoRequests(Integer.parseInt(avail.getText().toString()),Integer.parseInt(need.getText().toString()));
            }
        });
        return root;
    }
    public void gotoRequests(int av,int nd) {
        sport.setVisibility(View.GONE);
        avail.setVisibility(View.GONE);
        need.setVisibility(View.GONE);
        hostButton.setVisibility(View.GONE);
        loc.setVisibility(View.GONE);
        mv.setVisibility(View.GONE);
        req.setVisibility(View.VISIBLE);
        Map<String,Integer> data= new HashMap<>();
        data.put("available",av);
        data.put("need",nd);
        db.collection(sport.getSelectedItem().toString()).document(MainActivity.loggedemail).set(data);
        showRequests();
    }
    public void showRequests() {

    }

}
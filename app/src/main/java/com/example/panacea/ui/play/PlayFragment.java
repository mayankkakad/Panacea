package com.example.panacea.ui.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.panacea.R;
import com.example.panacea.ui.host.HostViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Vector;

public class PlayFragment extends Fragment {

    private PlayViewModel playViewModel;
    static Vector<String> items;
    FirebaseFirestore db;
    static Spinner sport;
    static View root;
    Button playButton;
    TextView hosts;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        playViewModel =
                ViewModelProviders.of(this).get(PlayViewModel.class);
        root = inflater.inflate(R.layout.fragment_play, container, false);
        db=FirebaseFirestore.getInstance();
        playButton=root.findViewById(R.id.button5);
        hosts=root.findViewById(R.id.textView6);
        hosts.setVisibility(View.INVISIBLE);
        items=new Vector<String>();
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
                    sport=root.findViewById(R.id.spinner);
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
                getHosts();
            }
        });
        return root;
    }
    public void getHosts() {
        
    }
}
package com.example.panacea.ui.sports;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.panacea.R;
import com.example.panacea.ui.host.HostFragment;
import com.example.panacea.ui.play.PlayFragment;
import com.google.android.gms.tasks.Tasks;

public class SportsFragment extends Fragment {

    private SportsViewModel sportsViewModel;
    ImageButton goHost,goPlay;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        sportsViewModel =
                ViewModelProviders.of(this).get(SportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sports, container, false);
        goHost=root.findViewById(R.id.imageButton7);
        goPlay=root.findViewById(R.id.imageButton8);
        goHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HostFragment hf=new HostFragment();
                FragmentManager manager=getParentFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.nav_host_fragment,hf)
                        .commit();

            }
        });
        goPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayFragment hf=new PlayFragment();
                FragmentManager manager=getParentFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.nav_host_fragment,hf)
                        .commit();
            }
        });
        return root;
    }
}

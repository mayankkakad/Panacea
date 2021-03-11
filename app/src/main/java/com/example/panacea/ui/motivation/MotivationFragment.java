package com.example.panacea.ui.motivation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.panacea.R;

public class MotivationFragment extends Fragment {

    private MotivationViewModel motivationViewModel;

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
        return root;
    }
}
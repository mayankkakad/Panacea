package com.example.panacea.ui.helpline;

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

public class HelplineFragment extends Fragment {

    private HelplineViewModel helplineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        helplineViewModel =
                ViewModelProviders.of(this).get(HelplineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_helpline, container, false);
        final TextView textView = root.findViewById(R.id.text_helpline);
        helplineViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
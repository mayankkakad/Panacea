package com.example.panacea.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.panacea.MainActivity;
import com.example.panacea.R;
import com.example.panacea.ui.host.HostFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button logoutButton;
    FirebaseAuth mFirebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mFirebaseAuth=FirebaseAuth.getInstance();
        logoutButton=(Button)root.findViewById(R.id.button21);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInClient mGoogleSignInClient;
                GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
                mGoogleSignInClient= GoogleSignIn.getClient(getActivity(), gso);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                if(account!=null)
                    mGoogleSignInClient.signOut();
                mFirebaseAuth.signOut();
                account = GoogleSignIn.getLastSignedInAccount(getActivity());
                if(mFirebaseAuth.getCurrentUser()==null&&account==null)
                    Toast.makeText(getActivity(),"Logout Successful",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(),"Logout Failed",Toast.LENGTH_SHORT).show();

                MainActivity.loggedemail=null;
                Intent i=new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });
        return root;
    }
}
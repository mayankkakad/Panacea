package com.example.panacea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String loggedemail=null;

    EditText emailId,pass;
    Button btnLog;
    ImageButton gSignIn;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(loggedemail!=null)
        {
            Intent init=new Intent(MainActivity.this,Home.class);
            startActivity(init);
        }
        db=FirebaseFirestore.getInstance();
        btnLog=findViewById(R.id.button);
        gSignIn=findViewById(R.id.imageButton);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null) {
                    loggedemail=mFirebaseUser.getEmail();
                    Intent intent=new Intent(MainActivity.this,Home.class);
                    startActivity(intent);
                }
            }
        };
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null) {
            loggedemail=account.getEmail();
            Intent gdone = new Intent(this, Home.class);
            startActivity(gdone);
        }
        gSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageButton:
                        signIn();
                        break;
                }
            }
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailId=findViewById(R.id.emailId);
                pass=findViewById(R.id.password);
                if(emailId.getText().toString().isEmpty())
                {
                    emailId.setError("Email ID cannot be kept empty!");
                    emailId.requestFocus();
                }
                else if(pass.getText().toString().isEmpty())
                {
                    pass.setError("Password cannot be kept empty!");
                    pass.requestFocus();
                }
                else
                {
                    final String email=emailId.getText().toString();
                    String passwd=pass.getText().toString();
                    mFirebaseAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                logIn(email);
                            }
                        }
                    });
                }
            }
        });
        signup=(Button)findViewById(R.id.button2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUp();
            }
        });
    }
    public void openSignUp() {
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,536);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 536) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
            loggedemail=account.getEmail().toString();
            DocumentReference docRef = db.collection("users").document(loggedemail);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("name",account.getDisplayName());
                            user.put("email", loggedemail);
                            db.collection("users").document(loggedemail).set(user);
                        }
                    }
                }
            });
            Intent gdone = new Intent(this, Home.class);
            startActivity(gdone);
        } catch (ApiException e) {
            Toast.makeText(MainActivity.this,"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_SHORT).show();
        }
    }
    public void logIn(String email)
    {
        loggedemail=email;
        Intent intent=new Intent(MainActivity.this,Home.class);
        startActivity(intent);
    }
}
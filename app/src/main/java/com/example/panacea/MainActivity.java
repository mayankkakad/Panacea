package com.example.panacea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailId,pass;
    Button btnLog;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLog=findViewById(R.id.button);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null) {
                    Intent intent=new Intent(MainActivity.this,Home.class);
                    startActivity(intent);
                }
            }
        };
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
                    String email=emailId.getText().toString();
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
                                Intent intent=new Intent(MainActivity.this,Home.class);
                                startActivity(intent);
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
}
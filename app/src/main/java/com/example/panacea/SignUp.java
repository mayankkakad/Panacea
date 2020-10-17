package com.example.panacea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText emailId;
    EditText name;
    EditText dob;
    EditText pass;
    Button btnSign;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        btnSign=findViewById(R.id.button3);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=findViewById(R.id.editTextTextPersonName2);
                dob=findViewById(R.id.editTextDate);
                emailId=findViewById(R.id.editTextTextPersonName3);
                pass=findViewById(R.id.editTextTextPassword2);
                if(name.getText().toString().isEmpty())
                {
                    name.setError("Name cannot be kept empty!");
                    name.requestFocus();
                }
                else if(dob.getText().toString().isEmpty())
                {
                    dob.setError("DOB cannot be kept empty!");
                    dob.requestFocus();
                }
                else if(emailId.getText().toString().isEmpty())
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
                    mFirebaseAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(SignUp.this,"Email ID already registered",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name.getText().toString());
                                user.put("dob", dob.getText().toString());
                                user.put("email", emailId.getText().toString());
                                db.collection("users").add(user);
                                Toast.makeText(SignUp.this,"Sign Up Successful",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SignUp.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}
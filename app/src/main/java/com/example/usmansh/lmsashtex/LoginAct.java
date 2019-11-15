package com.example.usmansh.lmsashtex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {


    EditText logEmail, logPassword;
    Button logBt, Regbt;
    String email, password;
    Intent goLoginAct;
    Intent goMainAct;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        logEmail    = (EditText) findViewById(R.id.logEmail);
        logPassword = (EditText) findViewById(R.id.logPassword);
        logBt       = (Button) findViewById(R.id.logBt);
        Regbt       = (Button) findViewById(R.id.Regbt);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if(user.getEmail().equals("admin@adminlms.com")) {
                        goMainAct = new Intent(getApplicationContext(), AdminMainAct.class);
                    }else{
                        goMainAct = new Intent(getApplicationContext(), MainActivity.class);
                    }
                    goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMainAct);
                    finish();
                }
            }
        };




        logBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = logEmail.getText().toString();
                password = logPassword.getText().toString();

                LoginUser();

            }
        });


        Regbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goReg = new Intent(getApplicationContext(),RegistrationAct.class);
                goReg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goReg);
                finish();
            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void LoginUser() {

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){


            if(email.equals("admin")) {
                //if admin login
                email = email.toLowerCase()+"@adminlms.com";
                goLoginAct = new Intent(getApplicationContext(),AdminMainAct.class);
            }
            else{
                //If User Login
                email = email.toLowerCase()+"@lms.com";
                goLoginAct = new Intent(getApplicationContext(),MainActivity.class);
            }

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goLoginAct);
                        finish();
                    }else{
                        Toast.makeText(LoginAct.this, "Login Error..!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else {
            Toast.makeText(this, "Field is empty..!", Toast.LENGTH_SHORT).show();
        }

    }





}

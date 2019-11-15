package com.example.usmansh.lmsashtex;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RegistrationAct extends AppCompatActivity {

    EditText inputPhno, inputUserName,inputPassword,inputCode;
    Button registerBt,cancelRegBt,verifyBt;
    DatabaseReference RegUserData;
    FirebaseAuth mAuth;
    CheckBox checkTerm;
    String Phno,UserName,Password,numb,msgCode,SendmsgCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        inputPhno     = (EditText)findViewById(R.id.inputPhno);
        inputUserName = (EditText)findViewById(R.id.inputUserName);
        inputPassword = (EditText)findViewById(R.id.inputPassword);
        registerBt    = (Button)findViewById(R.id.RegBt);
        cancelRegBt   = (Button)findViewById(R.id.cancelReg);
        checkTerm     = (CheckBox)findViewById(R.id.checkBoxTerm);

        mAuth = FirebaseAuth.getInstance();
        RegUserData = FirebaseDatabase.getInstance().getReference("RegUsers");


        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkTerm.isChecked()){
                registerUser();
                }
                else{
                    Toast.makeText(RegistrationAct.this, "Accept Term & Service..!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        cancelRegBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
                goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goLoginAct);
                finish();
            }
        });



    }




    private void registerUser() {

        Phno = inputPhno.getText().toString().trim();
        UserName = inputUserName.getText().toString().trim();
        Password = inputPassword.getText().toString().trim();


        if(TextUtils.isEmpty(UserName)){
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
        }
        else if(UserName.contains(" ")){
            Toast.makeText(this, "White Space is not allowed in User Name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }

        else if(Password.length() < 6){
            Toast.makeText(this, "Password must contain 6 characters at least", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Phno)){
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if(Phno.contains(" ")){
            Toast.makeText(this, "White Space is not allowed in Phone no..!", Toast.LENGTH_SHORT).show();
        }



        else{

            createAccount();
        }


    }

    private void createAccount() {

        UserName = UserName.toLowerCase() +"@lms.com";

        Toast.makeText(this, "UserName: "+UserName, Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(UserName,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    UploadingUserData();

                }else{
                    Toast.makeText(RegistrationAct.this, "Authentication Error..!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void UploadingUserData() {


        User user = new User();
        user.setPhno(Phno);
        user.setUsername(UserName);
        user.setPassword(Password);
        user.setRole("user");

        RegUserData.child(inputUserName.getText().toString().toLowerCase()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(RegistrationAct.this, "You have Registered Successfully..!", Toast.LENGTH_SHORT).show();
                    Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
                    goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goLoginAct);
                    finish();

                }else{

                    Toast.makeText(RegistrationAct.this, "Registration Error..!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }




    @Override
    public void onBackPressed() {
        Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
        goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goLoginAct);
        finish();
    }


}

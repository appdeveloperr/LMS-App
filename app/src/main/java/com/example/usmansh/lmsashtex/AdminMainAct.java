package com.example.usmansh.lmsashtex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminMainAct extends AppCompatActivity {

    Button addBook,addCategory,adSignOutBt,deletebook,editbook;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        addBook     = (Button)findViewById(R.id.AddBook);
        addCategory = (Button)findViewById(R.id.addCatBt);
        adSignOutBt = (Button)findViewById(R.id.adSignOutBt);
        deletebook  = (Button)findViewById(R.id.DeleteBook);
        editbook    = (Button)findViewById(R.id.EditBook);


        mAuth = FirebaseAuth.getInstance();


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextAct = new Intent(getApplicationContext(), AddCatAct.class);
                startActivity(nextAct);
            }
        });


        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextAct = new Intent(getApplicationContext(), AddBookAct.class);
                startActivity(nextAct);
            }
        });


        deletebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextAct = new Intent(getApplicationContext(), DelBookAct.class);
                startActivity(nextAct);
            }
        });


        editbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextAct = new Intent(getApplicationContext(), EditBookAct.class);
                startActivity(nextAct);
            }
        });


        adSignOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goMain = new Intent(getApplicationContext(), LoginAct.class);
                goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goMain);
                finish();
                mAuth.signOut();

            }
        });

    }



}

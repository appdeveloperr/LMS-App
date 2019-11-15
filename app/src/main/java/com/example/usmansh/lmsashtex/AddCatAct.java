package com.example.usmansh.lmsashtex;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddCatAct extends AppCompatActivity {


    Button ac_addCatbt,ac_backBt;
    EditText ac_cated;
    DatabaseReference catListDB,catCountDB;
    String categoryName;
    ArrayList<String> CatNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cat);


            ac_addCatbt = (Button)findViewById(R.id.Ac_addCatBt);
            ac_backBt   = (Button)findViewById(R.id.Ac_backbt);
            ac_cated    = (EditText) findViewById(R.id.Ac_cated);

            catListDB   = FirebaseDatabase.getInstance().getReference("Category List");
            catCountDB  = FirebaseDatabase.getInstance().getReference("Category Count");

            getCatList();


        ac_addCatbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    categoryName = ac_cated.getText().toString();

                    if(TextUtils.isEmpty(categoryName)){
                        Toast.makeText(AddCatAct.this, "Error: Plz Enter the Category Name..!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        checkCategoryExist();
                    }

                }
            });


            ac_backBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });



    }


    private void checkCategoryExist() {

        boolean bookExist = false;

        for(int i=0; i <CatNameList.size(); i++){
            if(CatNameList.get(i).equalsIgnoreCase(categoryName)){
                bookExist = true;
                break;
            }
        }

        if(!bookExist){
            UploadCategory();
        }else{
            Toast.makeText(this, "Error: This Category Already Exist..!", Toast.LENGTH_SHORT).show();
        }

    }

    private void getCatList() {

        catListDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    String catName = postData.getValue(String.class);

                    if (catName != null) {
                        CatNameList.add(catName);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void UploadCategory() {

                // Adding First Category in DB
                    catListDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() == null) {
                                catListDB.child("1").setValue(categoryName);
                                catCountDB.child("CatCountVal").setValue("1");
                                Toast.makeText(AddCatAct.this, "Category Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                checkCounterFirstThenUploadDataTEXT();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

    }

    private void checkCounterFirstThenUploadDataTEXT() {


        catCountDB.child("CatCountVal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    int count = Integer.parseInt(counter);
                    count++;

                    catCountDB.child("CatCountVal").setValue(String.valueOf(count));
                    catListDB.child(String.valueOf(count)).setValue(categoryName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddCatAct.this, "Category Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(AddCatAct.this, "Error while uploading Category..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




}

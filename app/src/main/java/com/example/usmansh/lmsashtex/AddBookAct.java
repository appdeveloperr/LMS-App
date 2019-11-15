package com.example.usmansh.lmsashtex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddBookAct extends AppCompatActivity {


    EditText ab_titleEd, ab_authorEd, ab_yearEd;
    Button ab_submitBt, ab_backBt;
    Spinner spinnerCatList;
    DatabaseReference regBookDB,catListDB,regBookCount;
    ArrayList<String> CatNameList = new ArrayList<>();
    ArrayList<String> BookNameList = new ArrayList<>();
    ArrayList<Book> BooksArr = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String categorySelected;
    Book book;
    boolean bookExist;
    int countBookLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ab_titleEd     = (EditText) findViewById(R.id.Ab_titileed);
        ab_authorEd    = (EditText) findViewById(R.id.Ab_authored);
        ab_yearEd      = (EditText) findViewById(R.id.Ab_yeared);
        ab_submitBt    = (Button) findViewById(R.id.Ab_submitBt);
        ab_backBt      = (Button) findViewById(R.id.Ab_backBt);
        spinnerCatList = (Spinner)findViewById(R.id.spinner);


        regBookDB    = FirebaseDatabase.getInstance().getReference("Book List");
        catListDB    = FirebaseDatabase.getInstance().getReference("Category List");
        regBookCount = FirebaseDatabase.getInstance().getReference("Reg Book Count");

        getCatList();

        ab_submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categorySelected = spinnerCatList.getSelectedItem().toString();
                checkBooksLimit();
            }
        });


        ab_backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                adapter = new ArrayAdapter<>(AddBookAct.this, android.R.layout.simple_spinner_dropdown_item, CatNameList);
                spinnerCatList.setAdapter(adapter);

                //Getting Book List Now
                getBookList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private void getBookList() {

        regBookDB.child(spinnerCatList.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    Book book = postData.getValue(Book.class);
                    if(book != null){

                        //Getting All Books from Book List
                        BooksArr.add(book);
                        BookNameList.add(book.getBooktitle());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkBooksLimit() {


        regBookCount.child(categorySelected).child("Value").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                        countBookLimit = 1;
                        //Toast.makeText(AddBookAct.this, "CountBook: "+countBookLimit, Toast.LENGTH_SHORT).show();
                        checkBookExistence();
                }
                else{
                    String counter = dataSnapshot.getValue(String.class);
                     countBookLimit = Integer.parseInt(counter);
                    //Toast.makeText(AddBookAct.this, "CountBook: "+countBookLimit, Toast.LENGTH_SHORT).show();
                    if(countBookLimit > 9){
                        Toast.makeText(AddBookAct.this, "Book Limit is exceeding..!", Toast.LENGTH_SHORT).show();
                    }else{
                        checkBookExistence();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void checkBookExistence() {

        bookExist = false;
        for(int i=0; i<BookNameList.size();i++){
            String checkName = BookNameList.get(i);
            if(checkName.equalsIgnoreCase(ab_titleEd.getText().toString())){
                bookExist = true;
                break;
            }
        }

        if(bookExist){
            Toast.makeText(AddBookAct.this, "This Book Already Exist..!", Toast.LENGTH_SHORT).show();
        }else{
            submitBook();
        }


    }


    private void submitBook() {

        if (TextUtils.isEmpty(ab_titleEd.getText().toString())) {
            Toast.makeText(this, "Error: Enter The Title..!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ab_authorEd.getText().toString())) {
            Toast.makeText(this, "Error: Enter the Author Name..!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(ab_yearEd.getText().toString())){
            Toast.makeText(this, "Error: Enter the Year..!", Toast.LENGTH_SHORT).show();
        }else{

            book = new Book();
            book.setBooktitle(ab_titleEd.getText().toString());
            book.setBookauthor(ab_authorEd.getText().toString());
            book.setBookyear(ab_yearEd.getText().toString());
            book.setBookcategory(categorySelected);


            regBookDB.child(categorySelected).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {

                        regBookDB.child(categorySelected).child(ab_titleEd.getText().toString()).setValue(book);
                        regBookCount.child(categorySelected).child("Value").setValue("1");
                        Toast.makeText(AddBookAct.this, "Book Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        checkCounterFirstThenUploadBook();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AddBookAct.this, "Error: While uploading book..!", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
    private void checkCounterFirstThenUploadBook() {


        regBookCount.child(categorySelected).child("Value").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    int count = Integer.parseInt(counter);
                    count++;

                    regBookCount.child(categorySelected).child("Value").setValue(String.valueOf(count));
                    regBookDB.child(categorySelected).child(ab_titleEd.getText().toString()).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddBookAct.this, "Book Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(AddBookAct.this, "Error: while uploading Book..!", Toast.LENGTH_SHORT).show();
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

package com.example.usmansh.lmsashtex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsBookList extends AppCompatActivity {

    ListView bookListV;
    EditText bookSearched;
    ArrayAdapter<String> newArrAdapter,arrAdapter;
    DatabaseReference regBookDB;
    ArrayList<String> BookNameList = new ArrayList<>();
    ArrayList<Book> BooksArr = new ArrayList<>();
    String catName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_book_list);

        catName = getIntent().getStringExtra("catName");

        bookSearched = (EditText)findViewById(R.id.bookSearched);
        bookListV = (ListView)findViewById(R.id.bookListV);

        regBookDB    = FirebaseDatabase.getInstance().getReference("Book List");

        arrAdapter = new ArrayAdapter<>(UsBookList.this,android.R.layout.simple_list_item_1,BookNameList);
        bookListV.setAdapter(arrAdapter);

        getBookList();

        //Searching Function coding
        bookSearched.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence searchText, int arg1, int arg2, int arg3) {
                //When user changed the Text
                ArrayList<String> secondarr = new ArrayList<String>();

                for (int i = 0; i < BooksArr.size(); i++) {

                    Book book = BooksArr.get(i);

                    if (book.getBooktitle().contains(searchText) ||
                            book.getBookauthor().contains(searchText) ||
                            book.getBookyear().contains(searchText) ||
                            book.getBookcategory().contains(searchText))
                    {
                        secondarr.add(book.getBooktitle());
                    }

                }
                bookListV.setAdapter(new ArrayAdapter<>(UsBookList.this,android.R.layout.simple_list_item_1,secondarr));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


        bookListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book book = BooksArr.get(position);

                Intent gonext = new Intent(getApplicationContext(),usShowBookAct.class);
                gonext.putExtra("bTitle",book.getBooktitle());
                gonext.putExtra("bAuth",book.getBookauthor());
                gonext.putExtra("bYear",book.getBookyear());
                gonext.putExtra("bCat",book.getBookcategory());
                startActivity(gonext);

            }
        });


    }


    private void getBookList() {

        regBookDB.child(catName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    Book book = postData.getValue(Book.class);
                    if(book != null){

                        //Getting All Books from Book List
                        BooksArr.add(book);
                        BookNameList.add(book.getBooktitle());
                        arrAdapter.notifyDataSetChanged();
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

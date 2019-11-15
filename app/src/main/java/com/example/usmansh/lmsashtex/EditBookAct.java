package com.example.usmansh.lmsashtex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditBookAct extends AppCompatActivity {

    Spinner catListSp;
    ListView listView;
    DatabaseReference regBookDB,catListDB,regBookCount;
    ArrayList<String> CatNameList = new ArrayList<>();
    ArrayList<String> BookNameList = new ArrayList<>();
    ArrayList<Book> BooksArr = new ArrayList<>();
    ArrayAdapter<String> catadapter;
    ArrayAdapter<String> bookAdapter;
    Book book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);


        catListSp = (Spinner)findViewById(R.id.catListSpinner);
        listView  = (ListView)findViewById(R.id.listView);


        regBookDB    = FirebaseDatabase.getInstance().getReference("Book List");
        catListDB    = FirebaseDatabase.getInstance().getReference("Category List");
        regBookCount = FirebaseDatabase.getInstance().getReference("Reg Book Count");



        bookAdapter = new ArrayAdapter<>(EditBookAct.this,android.R.layout.simple_list_item_1,BookNameList);
        listView.setAdapter(bookAdapter);


        catadapter = new ArrayAdapter<>(EditBookAct.this, android.R.layout.simple_spinner_dropdown_item, CatNameList);
        catListSp.setAdapter(catadapter);


        getCatList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                book = BooksArr.get(position);
                Intent showDetails = new Intent(getApplicationContext(),showBookDetAct.class);
                showDetails.putExtra("bTitle",book.getBooktitle());
                showDetails.putExtra("bAuthor",book.getBookauthor());
                showDetails.putExtra("bYear",book.getBookyear());
                showDetails.putExtra("bCategory",book.getBookcategory());
                startActivity(showDetails);

            }
        });




        //Event Calls on spinner item select
        catListSp.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                bookAdapter.clear();
                BooksArr.clear();
                BookNameList.clear();
                getBookList();

            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });





    }


    @Override
    protected void onResume() {
        super.onResume();

        catadapter.clear();
        bookAdapter.clear();
        getCatList();
    }

    private void getCatList() {

        catListDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    String catName = postData.getValue(String.class);
                    if (catName != null) {
                        CatNameList.add(catName);
                        catadapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void getBookList() {

        regBookDB.child(catListSp.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    Book book = postData.getValue(Book.class);
                    if(book != null){

                        //Getting All Books from Book List
                        BooksArr.add(book);
                        BookNameList.add(book.getBooktitle());
                        bookAdapter.notifyDataSetChanged();
                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

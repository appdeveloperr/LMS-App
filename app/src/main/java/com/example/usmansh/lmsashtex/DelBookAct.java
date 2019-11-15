package com.example.usmansh.lmsashtex;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.storage.FileDownloadTask;

import java.util.ArrayList;

public class DelBookAct extends AppCompatActivity {

    Button db_delbt,db_backbt;
    EditText db_cated,db_nameed;
    DatabaseReference regBookDB,catListDB,regBookCount;
    String bookName,bookCategory;
    ArrayList<String> BookNameList = new ArrayList<>();
    boolean bookExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_book);

        db_delbt  = (Button)findViewById(R.id.Db_delbt);
        db_backbt = (Button)findViewById(R.id.Db_backbt);
        db_cated  = (EditText)findViewById(R.id.Db_cated);
        db_nameed = (EditText)findViewById(R.id.Db_booked);

        regBookDB    = FirebaseDatabase.getInstance().getReference("Book List");
        catListDB    = FirebaseDatabase.getInstance().getReference("Category List");
        regBookCount = FirebaseDatabase.getInstance().getReference("Reg Book Count");


        db_delbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(db_cated.getText().toString())){
                    Toast.makeText(DelBookAct.this, "Enter the category..!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(db_nameed.getText().toString())){
                    Toast.makeText(DelBookAct.this, "Enter the naem of Book..!", Toast.LENGTH_SHORT).show();
                }else {

                    getAllBooks();
                }

            }
        });



    }




    private void getAllBooks() {

        bookName = db_nameed.getText().toString();
        bookCategory = db_cated.getText().toString();

        regBookDB.child(bookCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    Book book = postData.getValue(Book.class);
                    if(book != null){
                        //Getting All Books from Book List
                        BookNameList.add(book.getBooktitle());
                    }
                }

                //Check Book presence
                checkBookExistence();
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
            if(checkName.equalsIgnoreCase(bookName)){
                bookExist = true;
                break;
            }
        }

        if(bookExist){

            //Delete Book
            regBookDB.child(bookCategory).child(bookName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(DelBookAct.this, "Book Deleted successfully..!", Toast.LENGTH_SHORT).show();
                        //Decrement Count No
                        DecrementBookCount();
                    }

                }
            });

        }else{

            Toast.makeText(DelBookAct.this, "Error: Invalid Category or Book Name..!", Toast.LENGTH_SHORT).show();
        }


    }



    private void DecrementBookCount() {


        regBookCount.child(bookCategory).child("Value").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    String counter = dataSnapshot.getValue(String.class);
                    int count = Integer.parseInt(counter);
                    count--;

                    if(!(count < 0)) {
                        regBookCount.child(bookCategory).child("Value").setValue(String.valueOf(count));
                        finish();
                    }else {
                        regBookCount.child(bookCategory).child("Value").setValue("0");
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DelBookAct.this, "Error: Counter Did not Decrement..!", Toast.LENGTH_SHORT).show();
            }
        });


    }


}

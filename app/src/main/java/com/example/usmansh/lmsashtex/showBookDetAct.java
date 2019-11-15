package com.example.usmansh.lmsashtex;

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

public class showBookDetAct extends AppCompatActivity {

    EditText sb_title,sb_author,sb_year,sb_category;
    Button   sb_editBt,sb_backBt;
    String bTitle,bAuthor,bYear,bCategory;
    DatabaseReference regBookDB,catListDB,regBookCount;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_det);


        bTitle    = getIntent().getStringExtra("bTitle");
        bAuthor   = getIntent().getStringExtra("bAuthor");
        bYear     = getIntent().getStringExtra("bYear");
        bCategory = getIntent().getStringExtra("bCategory");

        sb_title    = (EditText)findViewById(R.id.Sb_titileed);
        sb_author   = (EditText)findViewById(R.id.Sb_authored);
        sb_year     = (EditText)findViewById(R.id.Sb_yeared);
        sb_category = (EditText)findViewById(R.id.Sb_cated);
        sb_editBt   = (Button)findViewById(R.id.Sb_editBt);
        sb_backBt   = (Button)findViewById(R.id.Sb_backBt);

        regBookDB    = FirebaseDatabase.getInstance().getReference("Book List");
        catListDB    = FirebaseDatabase.getInstance().getReference("Category List");
        regBookCount = FirebaseDatabase.getInstance().getReference("Reg Book Count");


        sb_title.setText(bTitle);
        sb_author.setText(bAuthor);
        sb_year.setText(bYear);
        sb_category.setText(bCategory);



        sb_editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            submitBook();
            }
        });


        sb_backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }



    private void submitBook() {

        if (TextUtils.isEmpty(sb_title.getText().toString())) {
            Toast.makeText(this, "Error: Enter The Title..!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sb_author.getText().toString())) {
            Toast.makeText(this, "Error: Enter the Author Name..!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(sb_year.getText().toString())){
            Toast.makeText(this, "Error: Enter the Year..!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(sb_category.getText().toString())){
            Toast.makeText(this, "Error: Enter the Category..!", Toast.LENGTH_SHORT).show();
        }else{

            book = new Book();
            book.setBooktitle(sb_title.getText().toString());
            book.setBookauthor(sb_author.getText().toString());
            book.setBookyear(sb_year.getText().toString());
            book.setBookcategory(sb_category.getText().toString());

            regBookDB.child(sb_category.getText().toString()).child(bTitle).removeValue();
            regBookDB.child(sb_category.getText().toString()).child(sb_title.getText().toString()).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(showBookDetAct.this, "Book Successfully Updated..!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(showBookDetAct.this, "Error: Not Updated..!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }



}

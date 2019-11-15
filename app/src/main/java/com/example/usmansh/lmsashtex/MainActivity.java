package com.example.usmansh.lmsashtex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView showUname;
    Button us_signOutbt;
    GridView us_gridView;
    ArrayAdapter<String> arrAdapter;
    ArrayList<String> catListName = new ArrayList<>();
    DatabaseReference catListDB;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            us_signOutbt = (Button)findViewById(R.id.us_signOutbt);
            us_gridView  = (GridView)findViewById(R.id.us_gridView);
            showUname    = (TextView)findViewById(R.id.showUname);

            mAuth  = FirebaseAuth.getInstance();
            catListDB    = FirebaseDatabase.getInstance().getReference("Category List");

            getCatList();

            String email = mAuth.getCurrentUser().getEmail();
            String[] parts = email.split("@");
            String userName = parts[0];

            showUname.setText(showUname.getText().toString()+userName);


            arrAdapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,catListName);
            us_gridView.setAdapter(arrAdapter);

            us_signOutbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goMain = new Intent(getApplicationContext(), LoginAct.class);
                    goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMain);
                    finish();
                    mAuth.signOut();
                }
            });


            us_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent gonext = new Intent(getApplicationContext(),UsBookList.class);
                    gonext.putExtra("catName",catListName.get(position));
                    startActivity(gonext);
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
                        catListName.add(catName);
                    }
                }

                arrAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



}

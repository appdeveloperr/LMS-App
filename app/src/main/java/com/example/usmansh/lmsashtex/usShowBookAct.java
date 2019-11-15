package com.example.usmansh.lmsashtex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class usShowBookAct extends AppCompatActivity {

    TextView sb_titletv,sb_authortv,sb_yeartv,sb_cattv;
    String booktilte,bookauthor,bookyear,bookcategory;
    Button sb_okBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_show_book);


        booktilte    = getIntent().getStringExtra("bTitle");
        bookauthor   = getIntent().getStringExtra("bAuth");
        bookyear     = getIntent().getStringExtra("bYear");
        bookcategory = getIntent().getStringExtra("bCat");


        sb_titletv  = (TextView)findViewById(R.id.Sb_titiletv);
        sb_authortv = (TextView)findViewById(R.id.Sb_authortv);
        sb_yeartv   = (TextView)findViewById(R.id.Sb_yeartv);
        sb_cattv    = (TextView)findViewById(R.id.Sb_cattv);
        sb_okBt     = (Button)findViewById(R.id.Sb_okbt);

        sb_titletv.setText(booktilte);
        sb_authortv.setText(bookauthor);
        sb_yeartv.setText(bookyear);
        sb_cattv.setText(bookcategory);


        sb_okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

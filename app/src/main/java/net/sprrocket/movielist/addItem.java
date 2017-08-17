package net.sprrocket.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addItem extends AppCompatActivity {
    private moviesDbAdapter dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MovieList");

        dbHelper = new moviesDbAdapter(this);
        dbHelper.open();

        final EditText et_movieTitle = (EditText) findViewById(R.id.et_movieTitle);
        final EditText et_Director = (EditText) findViewById(R.id.et_Director);
        final EditText et_Year = (EditText) findViewById(R.id.et_Year);
        final EditText et_Rating = (EditText) findViewById(R.id.et_Rating);
        Button bt_submit = (Button) findViewById(R.id.bt_submit);

        bt_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dbHelper.createMovie(et_movieTitle.getText().toString(), et_Director.getText().toString(), et_Year.getText().toString(),
                    et_Rating.getText().toString());
                startActivity(new Intent(addItem.this, MainMenu.class));
            }
        });
    }



}

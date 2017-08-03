package net.sprrocket.movielist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainMenu extends AppCompatActivity {

    private moviesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MovieList");

        dbHelper = new moviesDbAdapter(this);
        dbHelper.open();
        dbHelper.insertSomeMovies();
        displayListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, addItem.class));
            }
        });
    }

    private void displayListView(){
        Cursor cursor = dbHelper.fetchAllMovies();

        // The desired columns to be bound
        String[] columns = new String[]{
                moviesDbAdapter.KEY_TITLE,
                moviesDbAdapter.KEY_DIRECTOR,
                moviesDbAdapter.KEY_YEAR,
                moviesDbAdapter.KEY_RATING
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.movietitle,
                R.id.director,
                R.id.year,
                R.id.rating
        };

        //create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(this, R.layout.movie_info,
                cursor, columns, to, 0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        //Assign adapter to ListView
        listView.setAdapter(dataAdapter);

    }

}

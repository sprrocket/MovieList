package net.sprrocket.movielist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class mainMenu extends AppCompatActivity {

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
        displayListView();

        FloatingActionButton addMovieButton = (FloatingActionButton) findViewById(R.id.fab);
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainMenu.this, addItem.class));
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

        final ListView movieListView = (ListView) findViewById(R.id.listView1);
        //Assign adapter to ListView
        movieListView.setAdapter(dataAdapter);
        movieListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                //dialogue to remove a listing

                final String selected =((TextView)movieListView.getChildAt(pos).findViewById(R.id.movietitle)).getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(mainMenu.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Delete this listing?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteMovie(selected);//delete movie from database
                                displayListView();//refresh list
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }


}

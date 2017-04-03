package com.udacity.android.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgresBar;
    private MoviePosterAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.rc_movie_grid);
        mErrorMessageTextView = (TextView)findViewById(R.id.tv_error_message);
        mProgresBar = (ProgressBar)findViewById(R.id.pb_loading_progress);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // COMPLETED (41) Set the layoutManager on mRecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MoviePosterAdapter();
        mRecyclerView.setAdapter(mAdapter);
        Log.v("Movie key", "Movie key " + getString(R.string.movie_db_key));
    }
}

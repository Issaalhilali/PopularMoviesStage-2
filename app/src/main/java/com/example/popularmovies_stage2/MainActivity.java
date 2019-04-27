package com.example.popularmovies_stage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies_stage2.Utils.JsonUtils;
import com.example.popularmovies_stage2.Utils.NetworkUtils;
import com.example.popularmovies_stage2.database.FavoriteMovie;
import com.example.popularmovies_stage2.model.Movies;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String SORT_FAVORITE = "favorite";
    private static String currentSort = SORT_POPULAR;
    private ArrayList<Movies> movieList;
    private RecyclerView mMvoieRecylerView;
    private MovieAdapter moviesAdapter;
    private List<FavoriteMovie> favMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMvoieRecylerView = (RecyclerView) findViewById(R.id.rv_main);
        mMvoieRecylerView.setLayoutManager(new GridLayoutManager(this, getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 2));
        mMvoieRecylerView.setHasFixedSize(true);

        moviesAdapter = new MovieAdapter(movieList, this, this);
        mMvoieRecylerView.setAdapter(moviesAdapter);

        // favorites database
        favMovies = new ArrayList<FavoriteMovie>();

        setTitle(getString(R.string.app_name) + " - Popular");

        setupViewModel();
    }

    private void loadMovies() {
        makeMovieSearchQuery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular && !currentSort.equals(SORT_POPULAR)) {
            ClearMovieItemList();
            currentSort = SORT_POPULAR;
            setTitle("Popular Movies" + "- Popular");
            loadMovies();
            return true;
        }
        if (id == R.id.action_sort_top_rated && !currentSort.equals(SORT_TOP_RATED)) {
            ClearMovieItemList();
            currentSort = SORT_TOP_RATED;
            setTitle(getString(R.string.app_name) + " - Top rated");
            loadMovies();
            return true;
        }
        if (id == R.id.action_sort_favorite && !currentSort.equals(SORT_FAVORITE)) {
            ClearMovieItemList();
            currentSort = SORT_FAVORITE;
            setTitle(getString(R.string.app_name) + " - Favorite");
            loadMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ClearMovieItemList() {
        if (movieList != null) {
            movieList.clear();
        } else {
            movieList = new ArrayList<Movies>();
        }
    }

    private void makeMovieSearchQuery() {
        if (currentSort.equals(SORT_FAVORITE)) {
            ClearMovieItemList();
            for (int i = 0; i < favMovies.size(); i++) {
                Movies mov = new Movies(
                        String.valueOf(favMovies.get(i).getId()),
                        favMovies.get(i).getTitle(),
                        favMovies.get(i).getReleaseDate(),
                        favMovies.get(i).getVote(),
                        favMovies.get(i).getPopularity(),
                        favMovies.get(i).getSynopsis(),
                        favMovies.get(i).getImage(),
                        favMovies.get(i).getBackdrop()
                );
                movieList.add(mov);
            }
            moviesAdapter.setMovieData(movieList);
        } else {
            String movieQuery = currentSort;
            URL movieSearchUrl = NetworkUtils.buildUrl(movieQuery, getText(R.string.api_key).toString());
            new MoviesQueryTask().execute(movieSearchUrl);
        }
    }

    // AsyncTask to perform query
    public class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                movieList = JsonUtils.parseMoviesJson(searchResults);
                moviesAdapter.setMovieData(movieList);
            }
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favs) {
                if (favs.size() > 0) {
                    favMovies.clear();
                    favMovies = favs;
                }
                for (int i = 0; i < favMovies.size(); i++) {
                    Log.d(TAG, favMovies.get(i).getTitle());
                }
                loadMovies();
            }
        });
    }

    public void OnListItemClick(Movies movieItem) {
        Intent myIntent = new Intent(this, DetailsActivity.class);
        myIntent.putExtra("movieItem", movieItem);
        startActivity(myIntent);
    }
}

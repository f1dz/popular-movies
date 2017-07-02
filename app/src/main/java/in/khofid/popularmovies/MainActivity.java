package in.khofid.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import in.khofid.popularmovies.utilities.Movie;
import in.khofid.popularmovies.utilities.Movies;
import in.khofid.popularmovies.utilities.MoviesAdapter;
import in.khofid.popularmovies.utilities.MoviesJsonUtils;
import in.khofid.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    ProgressBar mLoading;
    TextView mError;
    GridView mMoviesGrid;

    public Activity activity = this;
    static String SORT_POPULAR = "popular";
    static String SORT_RATING = "top_rated";
    String sortBy = SORT_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mError = (TextView) findViewById(R.id.tv_error_message_display);
        mMoviesGrid = (GridView) findViewById(R.id.movies_grid);

        loadMoviesData();

        mMoviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Movies movies = (Movies) mMoviesGrid.getItemAtPosition(pos);
                showDetailMovie(String.valueOf(movies.getMovieId()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sort_by_popular){
            sortBy = SORT_POPULAR;
            loadMoviesData();
            return true;
        }else if(id == R.id.sort_by_top_rated){
            sortBy = SORT_RATING;
            loadMoviesData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMoviesData(){
        showMoviesDataView();
        new FetchMoviesTask().execute();
    }

    private void showMoviesDataView(){
        mError.setVisibility(View.INVISIBLE);
        mMoviesGrid.setVisibility(View.VISIBLE);
    }

    private void showError(){
        mError.setVisibility(View.VISIBLE);
        mMoviesGrid.setVisibility(View.INVISIBLE);
    }

    public void showDetailMovie(String movieId){
        Class movieDetail = MovieDetailActivity.class;
        Intent intent = new Intent(this, movieDetail);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, MoviesAdapter>{

        @Override
        protected MoviesAdapter doInBackground(String... params) {
            if(params.length == 0) {
                URL moviesUrl = NetworkUtils.buildUrl(sortBy);

                try{
                    String jsonMovies = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                    Movies[] simpleJsonMoviesData = MoviesJsonUtils.getSimpleStringFromJson(MainActivity.this,jsonMovies);
                    return new MoviesAdapter(activity, simpleJsonMoviesData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
            if(!isOnline()){
                Toast.makeText(activity, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                activity.finishAffinity();
            }
        }

        @Override
        protected void onPostExecute(MoviesAdapter movies) {
            mLoading.setVisibility(View.INVISIBLE);
            mMoviesGrid.setAdapter(movies);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

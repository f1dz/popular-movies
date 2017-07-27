package in.khofid.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.khofid.popularmovies.data.FavoriteDbHelper;
import in.khofid.popularmovies.utilities.Movies;
import in.khofid.popularmovies.utilities.MoviesAdapter;
import in.khofid.popularmovies.utilities.MoviesJsonUtils;
import in.khofid.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoading;
    @BindView(R.id.tv_error_message_display) TextView mError;
    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    MoviesAdapter mMoviesAdapter;
    Movies[] mMovies;

    public Activity activity = this;
    static String SORT_POPULAR = "popular";
    static String SORT_RATING = "top_rated";
    public String sortBy = SORT_POPULAR;

    private static final String MOVIES_PARCEL = "movies_parcel";

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMoviesAdapter = new MoviesAdapter(this);

        int columnsNumber = getResources().getInteger(R.integer.columns_number);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,columnsNumber));
        mRecyclerView.setAdapter(mMoviesAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIES_PARCEL))
            loadMoviesFromBundle(savedInstanceState);
        else
            loadMoviesData();

        FavoriteDbHelper dbHelper = new FavoriteDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

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
        }else if(id == R.id.show_favorites){
            loadFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteMovies(){
        mMovies = FavoriteDbHelper.favoriteMovies(mDb);
        showMoviesDataView();
        mMoviesAdapter.setMoviesData(mMovies);
        mLoading.setVisibility(View.INVISIBLE);
    }

    private void loadMoviesData(){
        showMoviesDataView();
        new FetchMoviesTask().execute();
    }

    private void loadMoviesFromBundle(Bundle bundle) {
        showMoviesDataView();
        mMovies = (Movies[]) bundle.getParcelableArray(MOVIES_PARCEL);
        mMoviesAdapter.setMoviesData(mMovies);

    }

    private void showMoviesDataView(){
        mError.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(){
        mError.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public void showDetailMovie(String movieId){
        Class movieDetail = MovieDetailActivity.class;
        Intent intent = new Intent(this, movieDetail);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(MOVIES_PARCEL, mMovies);
    }

    @Override
    public void onCLick(Movies movies) {
        showDetailMovie(String.valueOf(movies.movie_id));
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movies[]>{

        @Override
        protected Movies[] doInBackground(String... params) {
            if(params.length == 0) {
                URL moviesUrl = NetworkUtils.buildUrl(sortBy);

                try{
                    String jsonMovies = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                    mMovies = MoviesJsonUtils.getSimpleStringFromJson(MainActivity.this,jsonMovies);
                    return mMovies;
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
            if(!NetworkUtils.isOnline(activity)){
                showError();
                Toast.makeText(activity, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(Movies[] movies) {
            mLoading.setVisibility(View.INVISIBLE);
            if(movies != null){
                mMoviesAdapter.setMoviesData(movies);
            }
        }
    }
}

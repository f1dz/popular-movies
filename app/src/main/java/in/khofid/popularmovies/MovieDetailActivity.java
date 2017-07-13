package in.khofid.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import in.khofid.popularmovies.utilities.Movie;
import in.khofid.popularmovies.utilities.MovieJsonUtils;
import in.khofid.popularmovies.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView mImagePoster;
    ImageView mImageBackdrop;
    TextView mTvMovieTitle;
    TextView mTvReleaseDate;
    TextView mTvRuntime;
    TextView mTvVoteAverage;
    TextView mTvOverview;
    ScrollView mScrollViewDetail;
    ProgressBar mProgressDetail;
    String movieID;

    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mImagePoster = (ImageView) findViewById(R.id.img_poster_detail);
        mImageBackdrop = (ImageView) findViewById(R.id.img_backdrop);
        mTvMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mTvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mTvRuntime = (TextView) findViewById(R.id.tv_runtime);
        mTvVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        mTvOverview = (TextView) findViewById(R.id.tv_overview);
        mScrollViewDetail = (ScrollView) findViewById(R.id.scroll_view_detail);
        mProgressDetail = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        showMovieDetail();

    }

    private void showMovieDetail(){

        mProgressDetail.setVisibility(View.VISIBLE);

        Intent starter = getIntent();

        if(starter != null){
            if(starter.hasExtra(Intent.EXTRA_TEXT)){
                movieID = starter.getStringExtra(Intent.EXTRA_TEXT);
                new FetchMovieTask().execute(movieID);
            }
        }
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie>{

        @Override
        protected Movie doInBackground(String... params) {
            if(params.length != 0) {
                URL moviesUrl = NetworkUtils.buildUrl(params[0]);

                try{
                    String jsonMovie = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                    Movie simpleJsonMovieData = MovieJsonUtils.getSimpleStringFromJson(MovieDetailActivity.this,jsonMovie);
                    return simpleJsonMovieData;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Movie movie) {
            Picasso.with(context).load(movie.poster_path).placeholder(R.drawable.movie_icon).into(mImagePoster);
            Picasso.with(context).load(movie.backdrop_path).placeholder(R.drawable.movie_icon).into(mImageBackdrop);
            mTvMovieTitle.setText(movie.title);
            mTvReleaseDate.setText(movie.release_date);
            mTvRuntime.setText(String.valueOf(movie.runtime) + getString(R.string.minutes));
            mTvVoteAverage.setText(String.valueOf(movie.vote_average) + getString(R.string.vote_per_10));
            mTvOverview.setText(movie.overview);
            mProgressDetail.setVisibility(View.INVISIBLE);
        }
    }
}

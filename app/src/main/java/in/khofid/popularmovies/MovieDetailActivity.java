package in.khofid.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.khofid.popularmovies.utilities.Movie;
import in.khofid.popularmovies.utilities.MovieJsonUtils;
import in.khofid.popularmovies.utilities.NetworkUtils;
import in.khofid.popularmovies.utilities.Video;
import in.khofid.popularmovies.utilities.VideosAdapter;
import in.khofid.popularmovies.utilities.VideosJsonUtils;

public class MovieDetailActivity extends AppCompatActivity implements VideosAdapter.VideosAdapterOnClickHandler{

    @BindView(R.id.img_poster_detail) ImageView mImagePoster;
    @BindView(R.id.img_backdrop) ImageView mImageBackdrop;
    @BindView(R.id.tv_movie_title) TextView mTvMovieTitle;
    @BindView(R.id.tv_release_date) TextView mTvReleaseDate;
    @BindView(R.id.tv_runtime) TextView mTvRuntime;
    @BindView(R.id.tv_vote_average) TextView mTvVoteAverage;
    @BindView(R.id.tv_overview) TextView mTvOverview;
    @BindView(R.id.pb_loading_indicator) ProgressBar mProgressDetail;
    @BindDrawable(R.drawable.movie_icon) Drawable movie_icon;
    @BindView(R.id.rv_videos) RecyclerView rvVideos;

    String movieID;
    static String videos_path = "/videos";

    public Context context = this;
    VideosAdapter mVideosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mVideosAdapter = new VideosAdapter(this);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        rvVideos.setNestedScrollingEnabled(false);
        rvVideos.setAdapter(mVideosAdapter);

        showMovieDetail();
    }

    private void showMovieDetail(){

        mProgressDetail.setVisibility(View.VISIBLE);

        Intent starter = getIntent();

        if(starter != null){
            if(starter.hasExtra(Intent.EXTRA_TEXT)){
                movieID = starter.getStringExtra(Intent.EXTRA_TEXT);
                new FetchMovieTask().execute(movieID);
                new FetchVideosTask().execute(movieID + videos_path);
            }
        }
    }

    @Override
    public void OnClick(Video video) {
        watchYoutubeVideo(video.url);
    }

    public void watchYoutubeVideo(String url){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private class FetchMovieTask extends AsyncTask<String, Void, Movie>{

        @Override
        protected Movie doInBackground(String... params) {
            if(params.length != 0) {
                URL moviesUrl = NetworkUtils.buildUrl(params[0]);

                try{
                    String jsonMovie = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                    return MovieJsonUtils.getSimpleStringFromJson(MovieDetailActivity.this,jsonMovie);
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
            Picasso.with(context).load(movie.poster_path).placeholder(movie_icon).into(mImagePoster);
            Picasso.with(context).load(movie.backdrop_path).placeholder(movie_icon).into(mImageBackdrop);
            mTvMovieTitle.setText(movie.title);
            mTvReleaseDate.setText(movie.release_date);

            String runtime = String.valueOf(movie.runtime) + getString(R.string.minutes);
            mTvRuntime.setText(runtime);

            String vote = String.valueOf(movie.vote_average) + getString(R.string.vote_per_10);
            mTvVoteAverage.setText(vote);
            mTvOverview.setText(movie.overview);
            mProgressDetail.setVisibility(View.INVISIBLE);
        }
    }

    private class FetchVideosTask extends AsyncTask<String, Void, Video[]>{

        @Override
        protected Video[] doInBackground(String... params) {
            if(params.length != 0){
                URL videosUrl = NetworkUtils.buildUrl(params[0]);

                try{
                    String jsonVideos = NetworkUtils.getResponseFromHttpUrl(videosUrl);
                    return VideosJsonUtils.getSimpleStringFromJson(MovieDetailActivity.this, jsonVideos);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Video[] videos) {
            if(null != videos){
                mVideosAdapter.setVideosData(videos);
            }
        }
    }
}

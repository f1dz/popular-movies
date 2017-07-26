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

import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.khofid.popularmovies.utilities.Movie;
import in.khofid.popularmovies.utilities.MovieJsonUtils;
import in.khofid.popularmovies.utilities.NetworkUtils;
import in.khofid.popularmovies.utilities.Review;
import in.khofid.popularmovies.utilities.ReviewAdapter;
import in.khofid.popularmovies.utilities.ReviewJsonUtils;
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
    @BindView(R.id.rv_reviews) RecyclerView rvReviews;
    @BindView(R.id.tv_no_trailers) TextView tvNoTrailers;
    @BindView(R.id.tv_no_reviews) TextView tvNoReviews;

    String movieID;
    static String videos_path = "/videos";
    static String reviews_path = "/reviews";

    public Context context = this;
    VideosAdapter mVideosAdapter;
    ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mVideosAdapter = new VideosAdapter(this);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        rvVideos.setNestedScrollingEnabled(false);
        rvVideos.setAdapter(mVideosAdapter);

        mReviewAdapter = new ReviewAdapter(this);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setNestedScrollingEnabled(false);
        rvReviews.setAdapter(mReviewAdapter);

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
                new FetchReviewsTask().execute(movieID + reviews_path);
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
            tvNoTrailers.setVisibility(View.VISIBLE);
            if(videos.length != 0){
                mVideosAdapter.setVideosData(videos);
                tvNoTrailers.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class FetchReviewsTask extends AsyncTask<String, Void, Review[]>{

        @Override
        protected Review[] doInBackground(String... params) {
            if(params.length != 0){
                URL reviewsUrl = NetworkUtils.buildUrl(params[0]);

                try{
                    String jsonReviews = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                    return ReviewJsonUtils.getSimpleStringFromJson(jsonReviews);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            tvNoReviews.setVisibility(View.VISIBLE);
            if(reviews.length != 0){
                mReviewAdapter.setReviewsData(reviews);
                tvNoReviews.setVisibility(View.INVISIBLE);
            }
        }
    }
}

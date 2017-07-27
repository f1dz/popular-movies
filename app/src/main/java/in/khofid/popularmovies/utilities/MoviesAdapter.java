package in.khofid.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.khofid.popularmovies.R;

/**
 * Created by ofid on 6/20/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private Context mContext;
    private Movies[] mMovies;
    final private MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler{
        void onCLick(Movies movies);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        this.mContext = (Context) clickHandler;
        this.mClickHandler = clickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.movie_image) ImageView moviePoster;
        @BindView(R.id.tv_movie_title) TextView movieTitle;

        public MoviesAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movies movies = mMovies[adapterPosition];
            mClickHandler.onCLick(movies);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int id = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(id, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MoviesAdapterViewHolder holder, int position) {
        final Movies entry = mMovies[position];
        Picasso.with(mContext)
                .load(NetworkUtils.IMG_URL + entry.poster_path)
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.moviePoster, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(NetworkUtils.IMG_URL + entry.poster_path)
                                .placeholder(R.drawable.movie_icon)
                                .error(R.drawable.movie_icon)
                                .into(holder.moviePoster);
                    }
                });
        holder.movieTitle.setText(entry.title);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if(null == mMovies) return 0;
        return mMovies.length;
    }

    public void setMoviesData(Movies[] movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

}

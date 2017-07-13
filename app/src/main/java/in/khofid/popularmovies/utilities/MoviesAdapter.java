package in.khofid.popularmovies.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.khofid.popularmovies.R;

/**
 * Created by ofid on 6/20/17.
 */

public class MoviesAdapter extends BaseAdapter {
    private final Context context;
    private Movies[] movies;

    public MoviesAdapter(Activity context, Movies[] movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int i) {
        return movies[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movies entry = movies[position];
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso
                .with(context)
                .load(entry.poster_path)
                .placeholder(R.drawable.movie_icon)
                .into(imageView);

        TextView movieName = (TextView) convertView.findViewById(R.id.tv_movie_title);
        movieName.setText(entry.title);

        return convertView;
    }

}

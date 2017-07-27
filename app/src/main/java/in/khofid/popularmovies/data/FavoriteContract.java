package in.khofid.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by ofid on 7/26/17.
 */

public class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
    }
}

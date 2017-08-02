package in.khofid.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import in.khofid.popularmovies.utilities.Movie;
import in.khofid.popularmovies.utilities.Movies;

/**
 * Created by ofid on 7/26/17.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "movies.db";

    // Schema version, should be increased when updating table or database
    private static final int DATABASE_VERSION  = 1;

    // Constructor
    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create tables and it's columns
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_POPULARITY + " DOUBLE NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_RUNTIME + " INTEGER NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER + " TEXT NOT NULL," +
                FavoriteContract.FavoriteEntry.COLUMN_BACKDROP + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Get all favorite movies
     * @param db SQLiteDatabase
     * @return Cursor
     */
    public static Cursor getAllFavorites(SQLiteDatabase db){
        return db.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                null, null, null, null, null,
                FavoriteContract.FavoriteEntry._ID
        );
    }

    /**
     * Add new favorite movie
     * @param context Context
     * @param movie Movie
     * @return boolean
     */
    public static boolean addNewFavorite(Context context, Movie movie){
        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.id);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.title);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.vote_average);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_POPULARITY, movie.popularity);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, movie.overview);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RUNTIME, movie.runtime);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.release_date);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER, movie.poster_path);
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP, movie.backdrop_path);

        Uri uri = context.getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, cv);

        return (null != uri);
    }

    /**
     * Delete movie from favorite
     * @param context Context
     * @param id int ID's from database, not movie_id
     * @return boolean
     */
    public static boolean removeFavorite(Context context, int id){
        String movieId = Integer.toString(id);

        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();

        return context.getContentResolver().delete(uri, null, null) != 0;
    }

    /**
     * Get Movie
     * @param context Context
     * @param movieId int
     * @return Cursor
     */
    public static Cursor getMovie(Context context, int movieId){
        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();
        return context.getContentResolver().query(uri, null, null, null, FavoriteContract.FavoriteEntry._ID + " DESC");
    }

    /**
     * Get Favorite movie as Movie
     * @param context Context
     * @param movieId int
     * @return Movie
     */
    public static Movie getMovieData(Context context, int movieId){
        Cursor cursor = getMovie(context, movieId);
        cursor.moveToFirst();
        return new Movie(
                movieId,
                false,
                cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_BACKDROP)),
                "",
                "",
                "",
                cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW)),
                cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POPULARITY)),
                cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER)),
                cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE)),
                cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RUNTIME)),
                "",
                "",
                cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)),
                cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE)),
                0
        );
    }

    /**
     * Is movie favorited
     * @param context Context
     * @param movieId int
     * @return boolean
     */
    public static boolean isFavorited(Context context, int movieId){
        return getMovie(context, movieId).getCount() > 0;
    }

    public static Movies[] favoriteMovies(SQLiteDatabase db){
        Cursor cursor = getAllFavorites(db);
        cursor.moveToFirst();
        Movies[] movies = new Movies[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            movies[i] = new Movies(
                    1,
                    cursor.getInt(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID)),
                    cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)),
                    cursor.getDouble(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POPULARITY)),
                    cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER)),
                    "",
                    cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE))
            );
            cursor.moveToNext();
        }
        return movies;
    }
}

package in.khofid.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
     * @param db SQLiteDatabase
     * @param movie Movie
     * @return long
     */
    public static long addNewFavorite(SQLiteDatabase db, Movie movie){
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
        return db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, cv);
    }

    /**
     * Delete movie from favorite
     * @param db SQLiteDatabase
     * @param id int ID's from database, not movie_id
     * @return boolean
     */
    public static boolean removeFavorite(SQLiteDatabase db, int id){
        return db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + id, null) > 0;
    }

    /**
     * Get Movie
     * @param db SQLiteDatabase
     * @param movieId int
     * @return Cursor
     */
    public static Cursor getMovie(SQLiteDatabase db, int movieId){
        return db.rawQuery("SELECT * FROM " + FavoriteContract.FavoriteEntry.TABLE_NAME +
                " WHERE " + FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + "=" + movieId, null);
    }

    /**
     * Is movie favorited
     * @param db SQLiteDatabase
     * @param movieId int
     * @return boolean
     */
    public static boolean isFavorited(SQLiteDatabase db, int movieId){
        return getMovie(db, movieId).getCount() > 0;
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

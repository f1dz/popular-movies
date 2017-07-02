package in.khofid.popularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ofid on 6/20/17.
 */

public final class MoviesJsonUtils {

    public static Movies[] getSimpleStringFromJson(Context context, String moviesJsonStr) throws JSONException{

        final String RESULT = "results";
        final String ID = "id";
        final String PAGE = "page";
        final String VOTE_AVG = "vote_average";
        final String TITLE = "title";
        final String POPULARITY = "popularity";
        final String POSTER = "poster_path";
        final String ORI_LANG = "original_language";
        final String ORI_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(RESULT);

        Movies[] data = new Movies[moviesArray.length()];

        for(int i = 0; i < moviesArray.length(); i++){
            JSONObject movie = moviesArray.getJSONObject(i);
            data[i] = new Movies(
                    moviesJson.getInt(PAGE),
                    movie.getInt(ID),
                    movie.getDouble(VOTE_AVG),
                    movie.getString(TITLE),
                    movie.getDouble(POPULARITY),
                    movie.getString(POSTER),
                    movie.getString(ORI_LANG),
                    movie.getString(ORI_TITLE),
                    movie.getString(OVERVIEW),
                    movie.getString(RELEASE_DATE)
            );
        }
        return data;

    }
}

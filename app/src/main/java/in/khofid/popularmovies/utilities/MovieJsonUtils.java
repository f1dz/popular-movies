package in.khofid.popularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ofid on 7/2/17.
 */

public final class MovieJsonUtils {

    public static Movie getSimpleStringFromJson(Context context, String jsonStr) throws JSONException{

        JSONObject movieJson = new JSONObject(jsonStr);

        return new Movie(
                movieJson.getInt("id"),
                movieJson.getBoolean("adult"),
                movieJson.getString("backdrop_path"),
                movieJson.getString("homepage"),
                movieJson.getString("imdb_id"),
                movieJson.getString("original_language"),
                movieJson.getString("original_title"),
                movieJson.getString("overview"),
                movieJson.getDouble("popularity"),
                movieJson.getString("poster_path"),
                movieJson.getString("release_date"),
                movieJson.getInt("runtime"),
                movieJson.getString("status"),
                movieJson.getString("tagline"),
                movieJson.getString("title"),
                movieJson.getDouble("vote_average"),
                movieJson.getInt("vote_count")
        );
    }
}

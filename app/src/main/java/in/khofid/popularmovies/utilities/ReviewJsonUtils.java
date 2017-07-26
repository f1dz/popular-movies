package in.khofid.popularmovies.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ofid on 7/26/17.
 */

public final class ReviewJsonUtils {

    public static Review[] getSimpleStringFromJson(String json) throws JSONException{
        final String RESULTS = "results";
        final String ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        JSONObject reviewsObject = new JSONObject(json);
        JSONArray reviewsArray = reviewsObject.getJSONArray(RESULTS);
        Review[] data = new Review[reviewsArray.length()];

        for(int i = 0; i < reviewsArray.length(); i++){
            JSONObject review = reviewsArray.getJSONObject(i);
            data[i] = new Review(
                    review.getString(ID),
                    review.getString(AUTHOR),
                    review.getString(CONTENT),
                    review.getString(URL)
            );
        }
        return data;
    }
}

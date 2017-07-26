package in.khofid.popularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ofid on 7/25/17.
 */

public final class VideosJsonUtils {

    public static Video[] getSimpleStringFromJson(Context context, String jsonStr) throws JSONException{

        final String RESULT = "results";
        final String ID = "id";
        final String ISO_639_1 = "iso_639_1";
        final String ISO_3166_1 = "iso_3166_1";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String TYPE = "type";

        JSONObject videosJson = new JSONObject(jsonStr);
        JSONArray videosArray = videosJson.getJSONArray(RESULT);
        Video[] data = new Video[videosArray.length()];

        for(int i = 0; i < videosArray.length(); i++){
            JSONObject video = videosArray.getJSONObject(i);
            data[i] = new Video(
                    video.getString(ID),
                    video.getString(ISO_639_1),
                    video.getString(ISO_3166_1),
                    video.getString(KEY),
                    video.getString(NAME),
                    video.getString(SITE),
                    video.getInt(SIZE),
                    video.getString(TYPE),
                    video.getString(KEY)
            );
        }
        return data;
    }
}

package in.khofid.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ofid on 6/20/17.
 */

public final class NetworkUtils {

    private static final String API_KEY = "api_key";
    private static final String API_URL = "http://api.themoviedb.org/3/movie";
    private static final String PATH = "/3/movie/";
    public static final String IMG_URL = "http://image.tmdb.org/t/p/w185";
    public static final String IMG_URL_W342 = "http://image.tmdb.org/t/p/w342";

    private static String API = "";


    public static URL buildUrl(String sort){
        Uri builtUri = Uri.parse(API_URL).buildUpon()
                .path(PATH + sort)
                .appendQueryParameter(API_KEY, API)
                .build();
        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

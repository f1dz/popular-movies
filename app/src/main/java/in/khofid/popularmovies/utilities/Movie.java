package in.khofid.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ofid on 6/20/17.
 */

public class Movie implements Parcelable {
    public int id;
    public boolean adult;
    public String backdrop_path;
    public String homepage;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
    public String release_date;
    public int runtime;
    public String status;
    public String tagline;
    public String title;
    public double vote_average;
    public int vote_count;

    public Movie(int id, boolean adult, String backdrop_path, String homepage, String imdb_id, String original_language, String original_title, String overview, double popularity, String poster_path, String release_date, int runtime, String status, String tagline, String title, double vote_average, int vote_count) {
        this.id = id;
        this.adult = adult;
        this.backdrop_path = NetworkUtils.IMG_URL_W342 + backdrop_path;
        this.homepage = homepage;
        this.imdb_id = imdb_id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = NetworkUtils.IMG_URL + poster_path;
        this.release_date = release_date;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.homepage);
        dest.writeString(this.imdb_id);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeDouble(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeString(this.release_date);
        dest.writeInt(this.runtime);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeDouble(this.vote_average);
        dest.writeInt(this.vote_count);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.adult = in.readByte() != 0;
        this.backdrop_path = in.readString();
        this.homepage = in.readString();
        this.imdb_id = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.popularity = in.readDouble();
        this.poster_path = in.readString();
        this.release_date = in.readString();
        this.runtime = in.readInt();
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.vote_average = in.readDouble();
        this.vote_count = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

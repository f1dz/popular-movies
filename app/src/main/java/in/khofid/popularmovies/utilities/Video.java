package in.khofid.popularmovies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ofid on 7/25/17.
 */

public class Video implements Parcelable {
    public String id;
    public String iso_639_1;
    public String iso_3166_1;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;
    private static String _URL = "https://www.youtube.com/watch?v=";
    private static String IMG_URL = "https://img.youtube.com/vi/";
    private static String DEFAULT_QUALITY = "/mqdefault.jpg";
    public String url;
    public String img_path;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso_639_1);
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.img_path);
    }

    public Video(String id, String iso_639_1, String iso_3166_1, String key, String name, String site, int size, String type, String url) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.url = _URL + url;
        this.img_path = IMG_URL + key + DEFAULT_QUALITY;
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.iso_3166_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
        this.url = in.readString();
        this.img_path = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}

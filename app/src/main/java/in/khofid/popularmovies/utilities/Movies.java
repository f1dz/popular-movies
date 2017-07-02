package in.khofid.popularmovies.utilities;

/**
 * Created by ofid on 6/20/17.
 */

public class Movies {
    int page;
    int movie_id;
    double vote_average;
    String title;
    double popularity;
    String poster_path;
    String original_language;
    String original_title;
    String overview;
    String release_date;

    public Movies(int page, int movie_id, double vote_average, String title, double popularity, String poster_path, String original_language, String original_title, String overview, String release_date) {
        this.page = page;
        this.movie_id = movie_id;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = NetworkUtils.IMG_URL + poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMovieId() {
        return movie_id;
    }

    public void setMovieId(int id) {
        this.movie_id = id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}

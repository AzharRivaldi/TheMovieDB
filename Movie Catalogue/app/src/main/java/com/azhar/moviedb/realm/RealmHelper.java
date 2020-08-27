package com.azhar.moviedb.realm;

import android.content.Context;

import com.azhar.moviedb.model.ModelMovie;
import com.azhar.moviedb.model.ModelTV;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class RealmHelper {

    private Context mContext;
    private Realm realm;
    private RealmResults<ModelMovie> modelMovie;
    private RealmResults<ModelTV> modelTV;

    public RealmHelper(Context mContext) {
        this.mContext = mContext;
        Realm.init(mContext);
        realm = Realm.getDefaultInstance();
    }

    public ArrayList<ModelMovie> showFavoriteMovie() {
        ArrayList<ModelMovie> data = new ArrayList<>();
        modelMovie = realm.where(ModelMovie.class).findAll();

        if (modelMovie.size() > 0) {
            for (int i = 0; i < modelMovie.size(); i++) {
                ModelMovie movie = new ModelMovie();
                movie.setId(modelMovie.get(i).getId());
                movie.setTitle(modelMovie.get(i).getTitle());
                movie.setVoteAverage(modelMovie.get(i).getVoteAverage());
                movie.setOverview(modelMovie.get(i).getOverview());
                movie.setReleaseDate(modelMovie.get(i).getReleaseDate());
                movie.setPosterPath(modelMovie.get(i).getPosterPath());
                movie.setBackdropPath(modelMovie.get(i).getBackdropPath());
                movie.setPopularity(modelMovie.get(i).getPopularity());
                data.add(movie);
            }
        }
        return data;
    }

    public ArrayList<ModelTV> showFavoriteTV() {
        ArrayList<ModelTV> data = new ArrayList<>();
        modelTV = realm.where(ModelTV.class).findAll();

        if (modelTV.size() > 0) {
            for (int i = 0; i < modelTV.size(); i++) {
                ModelTV tv = new ModelTV();
                tv.setId(modelTV.get(i).getId());
                tv.setName(modelTV.get(i).getName());
                tv.setVoteAverage(modelTV.get(i).getVoteAverage());
                tv.setOverview(modelTV.get(i).getOverview());
                tv.setReleaseDate(modelTV.get(i).getReleaseDate());
                tv.setPosterPath(modelTV.get(i).getPosterPath());
                tv.setBackdropPath(modelTV.get(i).getBackdropPath());
                tv.setPopularity(modelTV.get(i).getPopularity());
                data.add(tv);
            }
        }
        return data;
    }

    public void addFavoriteMovie(int Id, String Title, double VoteAverage, String Overview,
                            String ReleaseDate, String PosterPath, String BackdropPath, String Popularity) {
        ModelMovie movie = new ModelMovie();
        movie.setId(Id);
        movie.setTitle(Title);
        movie.setVoteAverage(VoteAverage);
        movie.setOverview(Overview);
        movie.setReleaseDate(ReleaseDate);
        movie.setPosterPath(PosterPath);
        movie.setBackdropPath(BackdropPath);
        movie.setPopularity(Popularity);

        realm.beginTransaction();
        realm.copyToRealm(movie);
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
    }

    public void addFavoriteTV(int Id, String Title, double VoteAverage, String Overview,
                                 String ReleaseDate, String PosterPath, String BackdropPath, String Popularity) {
        ModelTV tv = new ModelTV();
        tv.setId(Id);
        tv.setName(Title);
        tv.setVoteAverage(VoteAverage);
        tv.setOverview(Overview);
        tv.setReleaseDate(ReleaseDate);
        tv.setPosterPath(PosterPath);
        tv.setBackdropPath(BackdropPath);
        tv.setPopularity(Popularity);

        realm.beginTransaction();
        realm.copyToRealm(tv);
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
    }

    public void deleteFavoriteMovie(int id) {
        realm.beginTransaction();
        RealmResults<ModelMovie> modelMovie = realm.where(ModelMovie.class).findAll();
        modelMovie.deleteAllFromRealm();
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
    }

    public void deleteFavoriteTV(int id) {
        realm.beginTransaction();
        RealmResults<ModelTV> modelTV = realm.where(ModelTV.class).findAll();
        modelTV.deleteAllFromRealm();
        realm.commitTransaction();

        //Toast.makeText(mContext, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
    }

}

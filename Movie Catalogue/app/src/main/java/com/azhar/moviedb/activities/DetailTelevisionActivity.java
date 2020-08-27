package com.azhar.moviedb.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.moviedb.R;
import com.azhar.moviedb.adapter.TrailerAdapter;
import com.azhar.moviedb.model.ModelTV;
import com.azhar.moviedb.model.ModelTrailer;
import com.azhar.moviedb.networking.ApiEndpoint;
import com.azhar.moviedb.realm.RealmHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class DetailTelevisionActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTitle, tvName, tvRating, tvRelease, tvPopularity, tvOverview;
    ImageView imgCover, imgPhoto;
    RecyclerView rvTrailer;
    MaterialFavoriteButton imgFavorite;
    FloatingActionButton fabShare;
    RatingBar ratingBar;
    String NameFilm, ReleaseDate, Popularity, Overview, Cover, Thumbnail, movieURL;
    int Id;
    double Rating;
    ModelTV modelTV;
    ProgressDialog progressDialog;
    List<ModelTrailer> modelTrailer = new ArrayList<>();
    TrailerAdapter trailerAdapter;
    RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan trailer");

        ratingBar = findViewById(R.id.ratingBar);
        imgCover = findViewById(R.id.imgCover);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgFavorite = findViewById(R.id.imgFavorite);
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvRating = findViewById(R.id.tvRating);
        tvRelease = findViewById(R.id.tvRelease);
        tvPopularity = findViewById(R.id.tvPopularity);
        tvOverview = findViewById(R.id.tvOverview);
        rvTrailer = findViewById(R.id.rvTrailer);
        fabShare = findViewById(R.id.fabShare);

        helper = new RealmHelper(this);

        modelTV = (ModelTV) getIntent().getSerializableExtra("detailTV");
        if (modelTV != null) {

            Id = modelTV.getId();
            NameFilm = modelTV.getName();
            Rating = modelTV.getVoteAverage();
            ReleaseDate = modelTV.getReleaseDate();
            Popularity = modelTV.getPopularity();
            Overview = modelTV.getOverview();
            Cover = modelTV.getBackdropPath();
            Thumbnail = modelTV.getPosterPath();
            movieURL = ApiEndpoint.URLFILM + "" + Id;

            tvTitle.setText(NameFilm);
            tvName.setText(NameFilm);
            tvRating.setText(Rating + "/10");
            tvRelease.setText(ReleaseDate);
            tvPopularity.setText(Popularity);
            tvOverview.setText(Overview);
            tvTitle.setSelected(true);
            tvName.setSelected(true);

            float newValue = (float)Rating;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(newValue / 2);

            Glide.with(this)
                    .load(ApiEndpoint.URLIMAGE + Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            Glide.with(this)
                    .load(ApiEndpoint.URLIMAGE + Thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

            rvTrailer.setHasFixedSize(true);
            rvTrailer.setLayoutManager(new LinearLayoutManager(this));

            getTrailer();

        }

        imgFavorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            Id = modelTV.getId();
                            NameFilm = modelTV.getName();
                            Rating = modelTV.getVoteAverage();
                            Overview = modelTV.getOverview();
                            ReleaseDate = modelTV.getReleaseDate();
                            Thumbnail = modelTV.getPosterPath();
                            Cover = modelTV.getBackdropPath();
                            Popularity = modelTV.getPopularity();
                            helper.addFavoriteTV(Id, NameFilm, Rating, Overview, ReleaseDate, Thumbnail, Cover, Popularity);
                            Snackbar.make(buttonView, modelTV.getName() + " Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            helper.deleteFavoriteTV(modelTV.getId());
                            Snackbar.make(buttonView, modelTV.getName() + " Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String subject = modelTV.getName();
                String description = modelTV.getOverview();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, subject + "\n\n" + description + "\n\n" + movieURL);
                startActivity(Intent.createChooser(shareIntent, "Bagikan dengan :"));
            }
        });

    }

    private void getTrailer() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.TV_VIDEO + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
                .addPathParameter("id", String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTrailer dataApi = new ModelTrailer();
                                dataApi.setKey(jsonObject.getString("key"));
                                dataApi.setType(jsonObject.getString("type"));
                                modelTrailer.add(dataApi);
                                showTrailer();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTelevisionActivity.this,
                                    "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailTelevisionActivity.this,
                                "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showTrailer() {
        trailerAdapter = new TrailerAdapter(DetailTelevisionActivity.this, modelTrailer);
        rvTrailer.setAdapter(trailerAdapter);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

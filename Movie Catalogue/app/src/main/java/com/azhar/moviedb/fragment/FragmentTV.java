package com.azhar.moviedb.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.moviedb.R;
import com.azhar.moviedb.activities.DetailTelevisionActivity;
import com.azhar.moviedb.adapter.TvAdapter;
import com.azhar.moviedb.adapter.TvHorizontalAdapter;
import com.azhar.moviedb.model.ModelTV;
import com.azhar.moviedb.networking.ApiEndpoint;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentTV extends Fragment implements TvHorizontalAdapter.onSelectData, TvAdapter.onSelectData{

    private RecyclerView rvNowPlaying, rvFilmRecommend;
    private TvHorizontalAdapter tvHorizontalAdapter;
    private TvAdapter tvAdapter;
    private ProgressDialog progressDialog;
    private SearchView searchFilm;
    private List<ModelTV> tvPlayNow = new ArrayList<>();
    private List<ModelTV> tvPopular = new ArrayList<>();

    public FragmentTV() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        searchFilm = rootView.findViewById(R.id.searchFilm);
        searchFilm.setQueryHint(getString(R.string.search_film));
        searchFilm.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearchTv(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    getFilmTV();
                return false;
            }
        });

        int searchPlateId = searchFilm.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchFilm.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        rvNowPlaying = rootView.findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rvNowPlaying);

        rvFilmRecommend = rootView.findViewById(R.id.rvFilmRecommend);
        rvFilmRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilmRecommend.setHasFixedSize(true);

        getTvHorizontal();
        getFilmTV();

        return rootView;
    }

    private void setSearchTv(String query) {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.SEARCH_TV
                + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE + ApiEndpoint.QUERY + query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            tvPopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTV dataApi = new ModelTV();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPopular.add(dataApi);
                                showFilmTV();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getTvHorizontal() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.TV_PLAYNOW + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
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
                                ModelTV dataApi = new ModelTV();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPlayNow.add(dataApi);
                                showMovieHorizontal();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getFilmTV() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + ApiEndpoint.TV_POPULAR + ApiEndpoint.APIKEY + ApiEndpoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            tvPopular = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelTV dataApi = new ModelTV();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String datePost = jsonObject.getString("first_air_date");

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setName(jsonObject.getString("name"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setReleaseDate(formatter.format(dateFormat.parse(datePost)));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                tvPopular.add(dataApi);
                                showFilmTV();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMovieHorizontal() {
        tvHorizontalAdapter = new TvHorizontalAdapter(getActivity(), tvPlayNow, this);
        rvNowPlaying.setAdapter(tvHorizontalAdapter);
        tvHorizontalAdapter.notifyDataSetChanged();
    }

    private void showFilmTV() {
        tvAdapter = new TvAdapter(getActivity(), tvPopular, this);
        rvFilmRecommend.setAdapter(tvAdapter);
        tvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelected(ModelTV modelTV) {
        Intent intent = new Intent(getActivity(), DetailTelevisionActivity.class);
        intent.putExtra("detailTV", modelTV);
        startActivity(intent);
    }
}

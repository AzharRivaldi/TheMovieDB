package com.azhar.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.moviedb.R;
import com.azhar.moviedb.model.ModelMovie;
import com.azhar.moviedb.networking.ApiEndpoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<ModelMovie> items;
    private MovieAdapter.onSelectData onSelectData;
    private Context mContext;
    private double Rating;

    public interface onSelectData {
        void onSelected(ModelMovie modelMovie);
    }

    public MovieAdapter(Context context, List<ModelMovie> items, MovieAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_film, parent, false);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        final ModelMovie data = items.get(position);

        Rating = data.getVoteAverage();
        holder.tvTitle.setText(data.getTitle());
        holder.tvRealeseDate.setText(data.getReleaseDate());
        holder.tvDesc.setText(data.getOverview());

        float newValue = (float)Rating;
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize((float) 0.5);
        holder.ratingBar.setRating(newValue / 2);

        Glide.with(mContext)
                .load(ApiEndpoint.URLIMAGE + data.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image)
                        .transform(new RoundedCorners(16)))
                .into(holder.imgPhoto);

        holder.cvFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectData.onSelected(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cvFilm;
        public ImageView imgPhoto;
        public TextView tvTitle;
        public TextView tvRealeseDate;
        public TextView tvDesc;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            cvFilm = itemView.findViewById(R.id.cvFilm);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRealeseDate = itemView.findViewById(R.id.tvRealeseDate);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

}

package com.azhar.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.azhar.moviedb.R;
import com.azhar.moviedb.model.ModelTrailer;

import java.util.List;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<ModelTrailer> items;
    private Context mContext;

    public TrailerAdapter(Context context, List<ModelTrailer> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        final ModelTrailer data = items.get(position);

        holder.btnTrailer.setText(data.getType());
        holder.btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + data.getKey()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public Button btnTrailer;

        public ViewHolder(View itemView) {
            super(itemView);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }
    }

}

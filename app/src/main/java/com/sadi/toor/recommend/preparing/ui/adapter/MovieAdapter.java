package com.sadi.toor.recommend.preparing.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.sadi.toor.recommend.R;
import com.sadi.toor.recommend.model.data.movie.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final OnViewClickLister clickLister;
    @NonNull
    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(@Nullable List<Movie> movies, OnViewClickLister clickLister) {
        this.clickLister = clickLister;
        if (movies == null) {
            return;
        }
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        viewHolder.ratingBar.setOnSmileySelectionListener((smiley, reselected) -> {
            int adapterPosition = viewHolder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(adapterPosition);
                movie.setRating(smiley + 1);
                clickLister.rate(movie);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Movie getMoviewFromPosition(int position) {
        return movies.get(position);
    }

    public interface OnViewClickLister {
        void rate(Movie movie);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_main_movie_pic)
        ImageView imageView;
        @BindView(R.id.tv_movie_title)
        TextView tvTitle;
        @BindView(R.id.tv_movie_year)
        TextView tvYear;
        @BindView(R.id.rate_fragment_bar)
        SmileRating ratingBar;

        MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(Movie data) {
            ratingBar.setNameForSmile(BaseRating.TERRIBLE, R.string.rating_bar_terrible);
            ratingBar.setNameForSmile(BaseRating.BAD, R.string.rating_bar_bad);
            ratingBar.setNameForSmile(BaseRating.OKAY, R.string.rating_bar_normal);
            ratingBar.setNameForSmile(BaseRating.GOOD, R.string.rating_bar_good);
            ratingBar.setNameForSmile(BaseRating.GREAT, R.string.rating_bar_great);
            Glide.with(imageView)
                    .load(data.getLink())
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
            tvTitle.setText(data.getName());
            tvYear.setText(String.valueOf(data.getYear()));
        }
    }
}

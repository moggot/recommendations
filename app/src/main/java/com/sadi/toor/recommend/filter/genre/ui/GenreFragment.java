package com.sadi.toor.recommend.filter.genre.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sadi.toor.recommend.Analytics;
import com.sadi.toor.recommend.R;
import com.sadi.toor.recommend.core.base.BaseFragment;
import com.sadi.toor.recommend.core.base.LoadingStatus;
import com.sadi.toor.recommend.filter.genre.ui.adapter.SelectableAdapter;
import com.sadi.toor.recommend.filter.genre.ui.adapter.SelectableViewHolder;
import com.sadi.toor.recommend.filter.genre.viewmodel.GenreViewModel;
import com.sadi.toor.recommend.model.data.genre.Genre;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;

public class GenreFragment extends BaseFragment<GenreViewModel> implements SelectableViewHolder.OnItemSelectedListener {

    public static final String TAG = "GenreFragment";

    @BindView(R.id.recycler_view_genre)
    RecyclerView recyclerViewGenre;

    private SelectableAdapter adapter;
    private GenreViewModel viewModel;

    public static GenreFragment newInstance() {
        return new GenreFragment();
    }

    @Override
    protected Class<GenreViewModel> getViewModel() {
        return GenreViewModel.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState, GenreViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView();
        viewModel.getGenreList().observe(this, genres -> {
            sharedViewModel.getSelectedGenres().observe(this, selectedGenres -> {
                Set<Genre> genreSet = new HashSet<>(selectedGenres);
                for (Genre genre : genres) {
                    if (genreSet.contains(genre)) {
                        genre.setSelected(true);
                    }
                }
            });
            adapter = new SelectableAdapter(this, genres, true);
            recyclerViewGenre.setAdapter(adapter);
        });
        viewModel.getStatus().observe(this, status -> {
            if (status.getType() == LoadingStatus.ERROR) {
                Bundle bundle = new Bundle();
                bundle.putString(TAG, status.getThrowable().getMessage());
                analytics.logEvent(Analytics.KEY_NETWORK_REQUEST_ERROR, bundle);
                Snackbar.make(view, getString(R.string.error_load_genres), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), action -> viewModel.retryCall())
                        .show();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewGenre.addItemDecoration(decoration);
        recyclerViewGenre.setLayoutManager(layoutManager);
    }

    @Override
    protected int getTitle() {
        return R.string.fragment_title_genre;
    }

    @Override
    protected String getFragmentTag() {
        return TAG;
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.genre_fragment;
    }

    @Override
    public void onItemSelected(Genre item) {
        sharedViewModel.putGenres(adapter.getSelectedItems());
    }
}

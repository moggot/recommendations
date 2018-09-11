package com.sadi.toor.recommend.recommendation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.sadi.toor.recommend.R;
import com.sadi.toor.recommend.core.base.BaseFragment;
import com.sadi.toor.recommend.model.data.Wish;
import com.sadi.toor.recommend.model.data.movie.Movie;
import com.sadi.toor.recommend.model.data.movie.Movies;
import com.sadi.toor.recommend.model.data.recommendations.Recommendations;
import com.sadi.toor.recommend.player.PlayerActivity;
import com.sadi.toor.recommend.recommendation.ui.adapter.RecommendAdapter;
import com.sadi.toor.recommend.recommendation.viewmodel.RecommendViewModel;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import butterknife.BindView;

public class RecommendFragment extends BaseFragment<RecommendViewModel> implements RecommendAdapter.OnViewClickLister {

    @BindView(R.id.rv_rec)
    RecyclerView rvRec;
    @BindView(R.id.rec_btn_rate_again)
    Button btnRateAgain;
    @BindView(R.id.rec_btn_filter)
    AppCompatImageView btnFilter;
    @BindView(R.id.progress_view)
    FrameLayout progress;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState, RecommendViewModel viewModel) {
        sharedViewModel.getSelectedMovies().observe(this, movieList -> {
            Movies movies = new Movies();
            movies.setMovies(movieList);
            viewModel.sendUserMovies(new Wish(movies.toString()));
        });
        viewModel.getRecommendations().observe(this, this::initRecyclerView);
        viewModel.getStatus().observe(this, status -> {
            switch (status) {
                case START_LOADING:
                    progress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progress.setVisibility(View.GONE);
                    break;
                case ERROR:
                    progress.setVisibility(View.GONE);
                    Snackbar.make(view, getString(R.string.error_load_rec), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.retry), action -> viewModel.retryCall())
                            .show();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown progress status = " + status.name());
            }
        });
        btnRateAgain.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
        btnFilter.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.filterFragment,
                    null,
                    new NavOptions.Builder()
                            .setEnterAnim(R.anim.push_up_in)
                            .setExitAnim(R.anim.push_up_out)
                            .setPopEnterAnim(R.anim.push_down_in)
                            .setPopExitAnim(R.anim.push_down_out)
                            .build());
        });

    }

    private void initRecyclerView(Recommendations recommendations) {
        RecommendAdapter adapter = new RecommendAdapter(recommendations, this);
        rvRec.setAdapter(adapter);

        ((SimpleItemAnimator) rvRec.getItemAnimator()).setSupportsChangeAnimations(false);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        SnapHelper snapHelper = new PagerSnapHelper();
        rvRec.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(rvRec);

        rvRec.setLayoutManager(linearLayoutManager);
    }


    @Override
    protected Class<RecommendViewModel> getViewModel() {
        return RecommendViewModel.class;
    }

    @Override
    protected int getTitle() {
        return R.string.recommend_title;
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.recommend_fragment;
    }

    @Override
    public void showTrailer(Movie movie) {
        startActivity(new Intent(getActivity(), PlayerActivity.class));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
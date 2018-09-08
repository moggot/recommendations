package com.sadi.toor.recommend.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.sadi.toor.recommend.viewmodel.RecommendViewModel;
import com.sadi.toor.recommend.di.scope.ViewModelKey;
import com.sadi.toor.recommend.viewmodel.FactoryViewModel;
import com.sadi.toor.recommend.viewmodel.GenreViewModel;
import com.sadi.toor.recommend.preparing.viewmodel.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GenreViewModel.class)
    abstract ViewModel bindGenreViewModel(GenreViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecommendViewModel.class)
    abstract ViewModel bindRecommendViewModel(RecommendViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}

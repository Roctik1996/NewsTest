package com.roctik.test.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.roctik.test.model.News;
import com.roctik.test.provider.ProviderModule;
import com.roctik.test.view.ViewGetNews;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class NewsPresenter extends MvpPresenter<ViewGetNews> {
    private static final String TAG = NewsPresenter.class.getSimpleName();

    public NewsPresenter() {
    }

    public void getNews(int page) {
        getViewState().showProgress(true);
        ProviderModule.getUserProvider().getNews(page).enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(@NotNull Call<List<News>> call, @NotNull Response<List<News>> response) {
                if (response.body() != null) {
                    getViewState().getNews(response.body());
                    getViewState().showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                getViewState().showError(t.getMessage());
                getViewState().showProgress(false);
            }
        });
    }
}

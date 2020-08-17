package com.roctik.test.view;

import com.arellomobile.mvp.MvpView;
import com.roctik.test.model.News;

import java.util.List;

public interface ViewGetNews extends MvpView {
    void getNews(List<News> news);

    void showProgress(boolean isLoading);

    void showError(String error);
}

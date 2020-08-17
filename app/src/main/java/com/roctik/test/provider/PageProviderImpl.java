package com.roctik.test.provider;

import com.roctik.test.api.Backend;
import com.roctik.test.model.News;
import com.roctik.test.network.NetworkModule;

import java.util.List;

import retrofit2.Call;

public class PageProviderImpl {
    private Backend mBackendService;

    PageProviderImpl() {
        initNetworkModule();
    }

    private void initNetworkModule() {
        mBackendService = NetworkModule.getBackEndService();
    }

    public Call<List<News>> getNews(int page) {
        return mBackendService.getNews(page);
    }

}

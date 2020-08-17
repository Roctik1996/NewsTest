package com.roctik.test.api;

import com.roctik.test.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Backend {

    @GET(".")
    Call<List<News>> getNews(@Query("page") int page);
}

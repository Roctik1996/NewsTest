package com.roctik.test.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.tabs.TabLayout;
import com.roctik.test.R;
import com.roctik.test.model.News;
import com.roctik.test.presenter.NewsPresenter;
import com.roctik.test.ui.adapter.NewsAdapter;
import com.roctik.test.view.ViewGetNews;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements ViewGetNews, NewsAdapter.ItemClick {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private FrameLayout frame;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> data = new ArrayList<>();
    private List<News> stories = new ArrayList<>();
    private List<News> favourites = new ArrayList<>();
    private List<News> video = new ArrayList<>();
    private List<News> top = new ArrayList<>();
    private TabLayout tabLayout;
    private List<News> listFiltered = new ArrayList<>();
    int i = 1;
    boolean check = true;
    boolean isScrolling = false;

    @InjectPresenter
    NewsPresenter newsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress);
        frame = findViewById(R.id.frame);
        tabLayout = findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);

        newsPresenter.getNews(i);
        newsAdapter = new NewsAdapter(stories, top, MainActivity.this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (check&&isScrolling) {
                    i++;
                    newsPresenter.getNews(i);
                    isScrolling=false;
                }
            }

        });
        initTab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem mSearch = menu.findItem(R.id.appSearchBar);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initTab() {
        TabLayout.Tab storiesTab = tabLayout.newTab();
        storiesTab.setText("STORIES");
        tabLayout.addTab(storiesTab);

        TabLayout.Tab videoTab = tabLayout.newTab();
        videoTab.setText("VIDEO");
        tabLayout.addTab(videoTab);

        TabLayout.Tab favTab = tabLayout.newTab();
        favTab.setText("FAVOURITES");
        tabLayout.addTab(favTab);

        tabLayout.selectTab(storiesTab, true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        newsAdapter = new NewsAdapter(stories, top, MainActivity.this);
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setAdapter(newsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        break;
                    case 1:
                        newsAdapter = new NewsAdapter(video, top, MainActivity.this);
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setAdapter(newsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                        break;
                    case 2:
                        newsAdapter = new NewsAdapter(favourites, top, MainActivity.this);
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setAdapter(newsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void getNews(List<News> news) {
        data.clear();
        data.addAll(news);
        for (int j = 0; j < data.size(); j++) {
            if (data.get(j).getType().equals("strories"))
                stories.add(data.get(j));

            if (data.get(j).getType().equals("video"))
                video.add(data.get(j));

            if (data.get(j).getType().equals("favourites"))
                favourites.add(data.get(j));

            if (data.get(j).getTop().equals("1"))
                top.add(data.get(j));
        }
        newsAdapter.notifyDataSetChanged();
        if (news.isEmpty())
            check = false;
    }

    @Override
    public void showProgress(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        frame.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        if (text.isEmpty()) {
            listFiltered.clear();
            listFiltered = data;
        } else {
            ArrayList<News> filteredList = new ArrayList<>();
            for (News row : data) {
                if (row.getTitle() != null) {
                    if (row.getTitle().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(row);
                        break;
                    }
                }

            }
            listFiltered = filteredList;
        }

        //calling a method of the adapter class and passing the filtered list
        newsAdapter.filterList(listFiltered);
    }
}
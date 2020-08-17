package com.roctik.test.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;
import com.roctik.test.R;
import com.roctik.test.model.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClick {
        void onClick(String url);
    }

    ItemClick itemClick;

    public static final int VIEW_TYPE_NEWS_TOP = 1;
    public static final int VIEW_TYPE_NEWS_OTHER = 2;

    private List<News> data;
    private List<News> top;
    private TopAdapter topAdapter;

    public NewsAdapter(List<News> data, List<News> top, ItemClick itemClick) {
        this.data = data;
        this.top = top;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_NEWS_TOP) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_top_news, parent, false);
            return new NewsTopViewHolder(view);
        } else if (viewType == VIEW_TYPE_NEWS_OTHER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            return new NewsViewHolder(view);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {

            case VIEW_TYPE_NEWS_TOP: {
                NewsTopViewHolder newsTopViewHolder = (NewsTopViewHolder) viewHolder;
                if (top != null && !top.isEmpty()) {
                    topAdapter = new TopAdapter(top);
                    newsTopViewHolder.pageIndicatorView.setCount(top.size());
                    newsTopViewHolder.pagerTop.setAdapter(topAdapter);
                    newsTopViewHolder.pagerTop.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            newsTopViewHolder.pageIndicatorView.setSelection(position);
                        }
                    });
                }
            }
            break;
            case VIEW_TYPE_NEWS_OTHER: {
                NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
                viewHolder.itemView.setOnClickListener(view -> itemClick.onClick(data.get(i).getClickUrl()));
                if (data != null) {
                    newsViewHolder.title.setText("" + data.get(i - 1).getTitle());
                    if (data.get(i - 1).getImg() != null) {
                        newsViewHolder.image.setVisibility(View.VISIBLE);
                        Glide.with(newsViewHolder.image)
                                .load(data.get(i - 1).getImg())
                                .into(newsViewHolder.image);
                    } else
                        newsViewHolder.image.setVisibility(View.GONE);
                    Spannable word = new SpannableString(data.get(i - 1).getClickUrl() + "");
                    word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    newsViewHolder.info.setText(word);
                    Spannable wordTwo = new SpannableString(" - " + data.get(i - 1).getTime());
                    wordTwo.setSpan(new ForegroundColorSpan(Color.GRAY), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    newsViewHolder.info.append(wordTwo);

                }
            }
            break;
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    public void filterList(List<News> filteredData) {
        this.data = filteredData;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_NEWS_TOP;
        }
        if (position > 0) {
            return VIEW_TYPE_NEWS_OTHER;
        } else return 0;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView title, info;
        ImageView image;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            info = itemView.findViewById(R.id.item_info);
            image = itemView.findViewById(R.id.item_image);

        }
    }

    static class NewsTopViewHolder extends RecyclerView.ViewHolder {

        ViewPager2 pagerTop;
        PageIndicatorView pageIndicatorView;

        NewsTopViewHolder(@NonNull View itemView) {
            super(itemView);
            pagerTop = itemView.findViewById(R.id.pager_top);
            pageIndicatorView = itemView.findViewById(R.id.pageIndicatorView);
        }
    }
}

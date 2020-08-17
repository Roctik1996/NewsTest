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

import com.bumptech.glide.Glide;
import com.roctik.test.R;
import com.roctik.test.model.News;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHolder> {

    private List<News> data;

    public TopAdapter(List<News> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pager, viewGroup, false);
        return new TopViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TopViewHolder topViewHolder, int i) {
        topViewHolder.title.setText(data.get(i).getTitle());
            Glide.with(topViewHolder.image)
                    .load(data.get(i).getImg()).error(R.drawable.no_image)
                    .into(topViewHolder.image);
        Spannable word = new SpannableString(data.get(i).getClickUrl()+"");
        word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        topViewHolder.info.setText(word);
        Spannable wordTwo = new SpannableString(" - " + data.get(i).getTime());
        wordTwo.setSpan(new ForegroundColorSpan(Color.GRAY), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        topViewHolder.info.append(wordTwo);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {

        TextView title, info;
        ImageView image;

        TopViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            info = itemView.findViewById(R.id.item_info);
            image = itemView.findViewById(R.id.item_image);

        }
    }
}

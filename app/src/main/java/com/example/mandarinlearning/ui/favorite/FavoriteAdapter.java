package com.example.mandarinlearning.ui.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.FavoriteItemBinding;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<WordLookup> wordLookupData;
    private IFavoriteFragmentView favoriteFragmentMvpView;
    private FavoriteListener listener;

    public FavoriteAdapter(ArrayList<WordLookup> wordLookupData, FavoriteListener listener, IFavoriteFragmentView cb) {
        this.wordLookupData = wordLookupData;
        this.favoriteFragmentMvpView = cb;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FavoriteItemBinding binding = FavoriteItemBinding.inflate(layoutInflater, parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        WordLookup wordLookup = wordLookupData.get(position);
        if (wordLookup == null) return;
        //maybe crash
        holder.binding.hanzi.setText(wordLookup.getSimplified());
        if (wordLookup.getEntries() != null && wordLookup.getEntries().size() > 0) {
            holder.binding.pinyin.setText(wordLookup.getEntries().get(0).getPinyin());
            holder.binding.popularRank.setText("#" + wordLookup.getRank());
            holder.binding.mean.setText(wordLookup.getEntries().get(0).getDefinitionsString());
            holder.binding.hskLevel.setText("HSK "+wordLookup.getHsk());
        } else {
            holder.binding.hskLevel.setVisibility(View.GONE);
            holder.binding.popularRank.setVisibility(View.GONE);
            holder.binding.mean.setVisibility(View.GONE);
            holder.binding.pinyin.setVisibility(View.GONE);
            holder.binding.error.setVisibility(View.VISIBLE);
        }

        holder.binding.save.setOnClickListener(v -> {
            //bind
        });
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClicked(wordLookup);
            }
        });
        holder.binding.save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
    }

    @Override
    public int getItemCount() {
        if (wordLookupData == null) return 0;
        return wordLookupData.size();
    }

    public void setWordLookupData(ArrayList<WordLookup> wordLookupData) {
        this.wordLookupData = wordLookupData;
        notifyDataSetChanged();
    }

    public interface FavoriteListener {
        void onFavoriteClicked(WordLookup wordLookup);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        FavoriteItemBinding binding;

        public FavoriteViewHolder(@NonNull FavoriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

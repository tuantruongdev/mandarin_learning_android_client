package com.example.mandarinlearning.ui.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.SearchHistoryItemBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<WordHistory> wordHistoryData;
    private DictionaryFragmentMvpView detailCharacterActivityMvpView;
    private HistoryListener listener;
    private WordDao wordDao;

    public HistoryAdapter(ArrayList<WordHistory> wordHistoryData,WordDao wordDao,HistoryListener listener, DictionaryFragmentMvpView cb) {
        this.wordHistoryData = wordHistoryData;
        this.wordDao =wordDao;
        this.detailCharacterActivityMvpView = cb;
        this.listener =listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchHistoryItemBinding binding = SearchHistoryItemBinding.inflate(layoutInflater, parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        WordHistory wordLookup = wordHistoryData.get(position);
        if (wordLookup == null) return;
        //maybe crash
        holder.binding.pinyin.setText(wordLookup.getPinyin());
        holder.binding.hanzi.setText(wordLookup.getSimplified());
        holder.binding.mean.setText(wordLookup.getDefinition());
        holder.binding.save.setOnClickListener(v -> {
            //bind
        });
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onHistoryClicked(wordLookup);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (wordHistoryData == null) return 0;
        return wordHistoryData.size();
    }

    public void setWordHistoryData(ArrayList<WordHistory> wordHistoryData) {
        this.wordHistoryData = wordHistoryData;
        notifyDataSetChanged();
    }

    private void checkSaved() {
//        if (Re.checkIfInDb(wordDao)) {
//            binding.save.setImageResource(R.drawable.ic_baseline_bookmark_24);
//            binding.save.setColorFilter(getResources().getColor(R.color.yellow));
//            return;
//        }
//        binding.save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
    }

    public interface HistoryListener {
        void onHistoryClicked(WordHistory wordHistory);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        SearchHistoryItemBinding binding;

        public HistoryViewHolder(@NonNull SearchHistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

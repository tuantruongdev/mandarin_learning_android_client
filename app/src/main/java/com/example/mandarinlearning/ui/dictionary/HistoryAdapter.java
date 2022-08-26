package com.example.mandarinlearning.ui.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.SearchHistoryItemBinding;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<WordLookup> wordHistoryData;
    private IDictionaryFragmentView dictionaryFragmentMvpView;
    private HistoryListener listener;

    public HistoryAdapter(ArrayList<WordLookup> wordHistoryData, HistoryListener listener, IDictionaryFragmentView cb) {
        this.wordHistoryData = wordHistoryData;
        this.dictionaryFragmentMvpView = cb;
        this.listener = listener;
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
        WordLookup wordLookup = wordHistoryData.get(position);
        if (wordLookup == null) return;
        //maybe crash
        if (wordLookup.getEntries() != null && wordLookup.getEntries().size() > 0) {
            holder.binding.pinyin.setText(wordLookup.getEntries().get(0).getPinyin());
            holder.binding.mean.setText(wordLookup.getEntries().get(0).getDefinitionsString());
        }
        holder.binding.hanzi.setText(wordLookup.getSimplified());
        holder.binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictionaryFragmentMvpView.saveCharacter(wordLookup);
                holder.binding.save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                if (checkSaved(wordLookup.getSimplified())) {
                    holder.binding.save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    holder.binding.save.setColorFilter(dictionaryFragmentMvpView.getColorResources(R.color.yellow));
                }
            }
        });

        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onHistoryClicked(wordLookup);
            }
        });
        holder.binding.save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
        if (checkSaved(wordLookup.getSimplified())) {
            holder.binding.save.setImageResource(R.drawable.ic_baseline_bookmark_24);
            holder.binding.save.setColorFilter(dictionaryFragmentMvpView.getColorResources(R.color.yellow));
        }

    }

    @Override
    public int getItemCount() {
        if (wordHistoryData == null) return 0;
        return wordHistoryData.size();
    }

    public void setWordHistoryData(ArrayList<WordLookup> wordHistoryData) {
        this.wordHistoryData = wordHistoryData;
        notifyDataSetChanged();
    }

    private boolean checkSaved(String character) {
        // presenter needed
        return dictionaryFragmentMvpView.onCheckSaved(character);
    }

    public interface HistoryListener {
        void onHistoryClicked(WordLookup wordHistory);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        SearchHistoryItemBinding binding;

        public HistoryViewHolder(@NonNull SearchHistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

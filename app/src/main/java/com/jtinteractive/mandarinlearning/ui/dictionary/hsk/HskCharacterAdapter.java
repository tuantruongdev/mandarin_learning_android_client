package com.jtinteractive.mandarinlearning.ui.dictionary.hsk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtinteractive.mandarinlearning.databinding.HskCharacterItemBinding;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class HskCharacterAdapter extends RecyclerView.Adapter<HskCharacterAdapter.HskViewHolder> {
    private ArrayList<String> hskData;
    private HskCharacterListener listener;

    public HskCharacterAdapter(ArrayList<String> hskData, HskCharacterListener listener) {
        this.hskData = hskData;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HskCharacterItemBinding binding = HskCharacterItemBinding.inflate(layoutInflater, parent, false);
        return new HskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HskViewHolder holder, int position) {
        String character = hskData.get(position);
        if (character == null) return;
        holder.binding.character.setText(character);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCharacterClicked(character);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (hskData == null) return 0;
        return hskData.size();
    }

    public void setHskData(ArrayList<String> hskData) {
        this.hskData = hskData;
        notifyDataSetChanged();
    }

    public interface HskCharacterListener {
        void onCharacterClicked(String character);
    }

    public class HskViewHolder extends RecyclerView.ViewHolder {
        HskCharacterItemBinding binding;

        public HskViewHolder(@NonNull HskCharacterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

package com.jtinteractive.mandarinlearning.ui.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtinteractive.mandarinlearning.data.remote.model.Entry;
import com.jtinteractive.mandarinlearning.databinding.DefinitionsItemBinding;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefViewHolder> {
    private ArrayList<Entry> definitionsData;

    public DefinitionAdapter(ArrayList<Entry> definitionsData) {
        this.definitionsData = definitionsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DefinitionsItemBinding binding = DefinitionsItemBinding.inflate(layoutInflater, parent, false);
        return new DefViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DefViewHolder holder, int position) {
        Entry definition = definitionsData.get(position);
        if (definition == null) return;
        holder.binding.pinyin.setText(definition.getPinyin());
        holder.binding.defs.setText(definition.getDefinitionsString());
    }

    @Override
    public int getItemCount() {
        if (definitionsData == null) return 0;
        return definitionsData.size();
    }

    public void setDefinitionsData(ArrayList<Entry> definitionsData) {
        this.definitionsData = definitionsData;
        notifyDataSetChanged();
    }

    public class DefViewHolder extends RecyclerView.ViewHolder {
        DefinitionsItemBinding binding;

        public DefViewHolder(@NonNull DefinitionsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

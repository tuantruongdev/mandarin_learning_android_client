package com.example.mandarinlearning.ui.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.databinding.ExampleItemBinding;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleDetail> exampleDetailsData;
    private IDetailCharacterView detailCharacterActivityMvpView;

    public ExampleAdapter(ArrayList<ExampleDetail> exampleDetailsData, IDetailCharacterView cb) {
        this.exampleDetailsData = exampleDetailsData;
        this.detailCharacterActivityMvpView = cb;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ExampleItemBinding binding = ExampleItemBinding.inflate(layoutInflater, parent, false);
        return new ExampleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleDetail exampleDetail = exampleDetailsData.get(position);
        if (exampleDetail == null) return;
        holder.binding.pinyin.setText(exampleDetail.getPinyin());
        holder.binding.hanzi.setText(exampleDetail.getHanzi());
        holder.binding.mean.setText(exampleDetail.getTranslation());
        holder.binding.soundPlay.setOnClickListener(v -> detailCharacterActivityMvpView.playMediaPlayer(exampleDetail.getAudio()));
    }

    @Override
    public int getItemCount() {
        if (exampleDetailsData == null) return 0;
        return exampleDetailsData.size();
    }

    public void setExampleDetailsData(ArrayList<ExampleDetail> exampleDetailsData) {
        this.exampleDetailsData = exampleDetailsData;
        notifyDataSetChanged();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        ExampleItemBinding binding;

        public ExampleViewHolder(@NonNull ExampleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

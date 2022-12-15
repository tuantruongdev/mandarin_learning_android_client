package com.example.mandarinlearning.ui.dictionary.ocr;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinlearning.data.remote.model.Translate;
import com.example.mandarinlearning.databinding.TranslateItemBinding;
import com.example.mandarinlearning.ui.detail.IDetailCharacterView;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder> {
    private ArrayList<Translate> translateData;
    private IDetailCharacterView detailCharacterActivityMvpView;

    public TranslateAdapter(ArrayList<Translate> translateData/*, IDetailCharacterView cb*/) {
        this.translateData = translateData;
        //this.detailCharacterActivityMvpView = cb;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TranslateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TranslateItemBinding binding = TranslateItemBinding.inflate(layoutInflater, parent, false);
        return new TranslateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TranslateViewHolder holder, int position) {
        Translate translate = translateData.get(position);
        if (translate == null) return;
        holder.binding.mean.setText(translate.getTranslated());
        String[] listText = translate.getOriginalText().split("\r");
        //remove view prevent duplicate
        holder.binding.hanzi.removeAllViews();
        for (int i = 0; i < listText.length; i++) {
            OcrTextView custom = new OcrTextView(holder.itemView.getContext());
            custom.setCharacter(listText[i]);
            holder.binding.hanzi.addView(custom);
        }
    }

    @Override
    public int getItemCount() {
        if (translateData == null) return 0;
        return translateData.size();
    }

    public void setTranslateData(ArrayList<Translate> translateData) {
        this.translateData = translateData;
        notifyDataSetChanged();
    }

    public class TranslateViewHolder extends RecyclerView.ViewHolder {
        TranslateItemBinding binding;

        public TranslateViewHolder(@NonNull TranslateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

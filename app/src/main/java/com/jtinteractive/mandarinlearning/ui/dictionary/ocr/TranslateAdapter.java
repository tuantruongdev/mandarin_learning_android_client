package com.jtinteractive.mandarinlearning.ui.dictionary.ocr;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.data.remote.model.TranslateRequest;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;
import com.jtinteractive.mandarinlearning.databinding.TranslateItemBinding;
import com.jtinteractive.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.jtinteractive.mandarinlearning.ui.detail.IDetailCharacterView;
import com.jtinteractive.mandarinlearning.ui.main.MainActivity;
import com.jtinteractive.mandarinlearning.utils.ApplicationHelper;
import com.jtinteractive.mandarinlearning.utils.NotificationHelper;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.TranslateViewHolder> implements OcrTextView.IOcrTextView, ITranslateAdapter,IOcrFragment {
    private ArrayList<TranslateRequest> translateData;
    private IDetailCharacterView detailCharacterActivityMvpView;
    private View popupView;
    private OcrFragmentPresenter ocrFragmentPresenter;
    private Context context;
    private PopupWindow popupWindow;
    private View view;
    private View itemView;

    public TranslateAdapter(ArrayList<TranslateRequest> translateData/*, IDetailCharacterView cb*/) {
        this.translateData = translateData;
        ocrFragmentPresenter = new OcrFragmentPresenter(this);
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
        context = holder.binding.getRoot().getContext();
        TranslateRequest translate = translateData.get(position);
        if (translate == null) return;
        holder.binding.mean.setText(translate.getTranslated());
        String[] listText = translate.getOriginalText().split("\r");
        //remove view prevent duplicate
        holder.binding.hanzi.removeAllViews();
        for (int i = 0; i < listText.length; i++) {
            OcrTextView custom = new OcrTextView(holder.itemView.getContext(), this);
            custom.setCharacter(listText[i]);
            holder.binding.hanzi.addView(custom);
        }
        ocrFragmentPresenter.setAdapterCallBack(this);
    }

    @Override
    public int getItemCount() {
        if (translateData == null) return 0;
        return translateData.size();
    }

    public void setTranslateData(ArrayList<TranslateRequest> translateData) {
        this.translateData = translateData;
        notifyDataSetChanged();
    }

    @Override
    public void onTextClicked(View view, View itemView, String character) {
        showPopupWindow(view, itemView, character);
    }

    @Override
    public void onTranslateResponse(WordLookup wordLookup) {
        Log.d(TAG, "onTranslateResponse: " + wordLookup.getSimplified());
        TextView pinyin = popupView.findViewById(R.id.pinyin_prv);
        TextView mean = popupView.findViewById(R.id.mean_prv);

        if (context.getClass().equals(MainActivity.class)) {
            ((MainActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pinyin.setText(wordLookup.getEntries().get(0).getPinyin());
                    mean.setText(wordLookup.getEntries().get(0).getSomeDefinitions());
//                    int[] location = new int[2];
//                    itemView.getLocationOnScreen(location);
//                    int x2 = popupView.getMeasuredWidth();
//                    int a = popupView.getWidth();
//                    int b = popupView.getHeight();
//                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - popupWindow.getWidth() / 2, location[1] - popupView.getMeasuredHeight());

                }
            });
        }
    }

    @Override
    public void onErrorResponse() {
        NotificationHelper.showSnackBar(view, 2, context.getResources().getText(R.string.error_network).toString());
    }


    public class TranslateViewHolder extends RecyclerView.ViewHolder {
        TranslateItemBinding binding;

        public TranslateViewHolder(@NonNull TranslateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void showPopupWindow(View view, View itemView, String characterStr) {
        this.view = view;
        this.itemView = itemView;
        // Inflate the popup window layout
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.character_popup, null);
        // ImageView triangle =popupView.findViewById(R.id.triangle_view);
        TextView character = popupView.findViewById(R.id.character_prv);
        View divide = popupView.findViewById(R.id.divide_prv);

        // Create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set the animation style
        popupWindow.setAnimationStyle(R.style.pop_anim);

        // Get the location of the item view on the screen
        int[] location = new int[2];
        itemView.getLocationOnScreen(location);
        popupView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int x2 = popupView.getMeasuredWidth();
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - x2 / 2, location[1] - popupView.getMeasuredHeight());

//        int startPopupX = location[0] - x2/2;
//        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) triangle.getLayoutParams();
//        marginParams.setMargins(location[0] -startPopupX, 0,0 , 0);
        divide.getLayoutParams().width = (int) (x2 - x2 * 0.2);
        character.setText(characterStr);
        ocrFragmentPresenter.lookup(characterStr);
        // Dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ocrFragmentPresenter.getWordLookup() == null || !TextUtils.equals(ocrFragmentPresenter.getWordLookup().getSimplified(), characterStr)) {
                    NotificationHelper.showSnackBar(view, 1, ApplicationHelper.getInstance().getContext().getResources().getText(R.string.please_wait).toString());
                    return;
                }
                DetailCharacterActivity.starter(context, ocrFragmentPresenter.getWordLookup());
            }
        });
    }

}

interface ITranslateAdapter {
    void onTranslateResponse(WordLookup wordLookup);
    void onErrorResponse();
}

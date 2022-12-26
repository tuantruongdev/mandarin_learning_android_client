package com.example.mandarinlearning.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mandarinlearning.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NotificationHelper {
    public static final int NOTI_SUCCESS = 0;
    public static final int NOTI_WARM = 1;
    public static final int NOTI_ERROR = 2;

    public static void showSnackBar(View v, int type, String text) {
        Snackbar mSnackBar = Snackbar.make(v, "TOP SNACKBAR", Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        TextView mainTextView = (TextView) (mSnackBar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        mainTextView.setText(text);
       // mainTextView.setAllCaps(true);
     //   mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mainTextView.setTextSize(15);
  //      mainTextView.setTypeface(null, Typeface.BOLD);
        //params.gravity = Gravity.BOTTOM;
        params.bottomMargin = getNavigationBarHeight(ApplicationHelper.getInstance().getContext(), 1) + 50;
        view.setLayoutParams(params);
        switch (type) {
            case NOTI_SUCCESS:
                view.setBackgroundColor(ApplicationHelper.getInstance().getContext().getColor(R.color.green_success));
                break;
            case NOTI_WARM:
                view.setBackgroundColor(ApplicationHelper.getInstance().getContext().getColor(R.color.yellow_warn));
                break;
            case NOTI_ERROR:
                view.setBackgroundColor(ApplicationHelper.getInstance().getContext().getColor(R.color.red_down));
                break;
        }
        mainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackBar.dismiss();
            }
        });

        mSnackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        mSnackBar.show();
    }

    private static int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

}

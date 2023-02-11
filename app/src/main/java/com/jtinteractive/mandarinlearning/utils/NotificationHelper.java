package com.jtinteractive.mandarinlearning.utils;

import android.graphics.Point;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jtinteractive.mandarinlearning.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NotificationHelper {
    public static final int NOTI_SUCCESS = 0;
    public static final int NOTI_WARM = 1;
    public static final int NOTI_ERROR = 2;

    public static void showSnackBar(View v, int type, String text) {
        showSnackBar(v, type, text, "", null);
    }

    public static void showSnackBar(View v, int type, String text, String actionName, View.OnClickListener cb) {
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
        //   params.bottomMargin = getNavigationBarHeight(ApplicationHelper.getInstance().getContext(), 1) + 150;
        Point h = ApplicationHelper.getNavigationBarSize(ApplicationHelper.getInstance().getContext());
        params.bottomMargin = h.y + 80;
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
        if (cb != null) {
            mSnackBar.setAction(actionName, cb);
        }
        mSnackBar.show();
    }


}

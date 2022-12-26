package com.example.mandarinlearning.utils;

import android.app.Activity;
import android.content.Context;

import com.example.mandarinlearning.R;

public class ApplicationHelper {
    private static ApplicationHelper instance;
    private volatile Context applicationContext;

    private ApplicationHelper() {
    }

    public static ApplicationHelper getInstance() {
        if (instance == null) {
            instance = new ApplicationHelper();
        }
        return instance;
    }

    public Context getContext() {
        return applicationContext;
    }

    public void init(Context context){
        this.applicationContext = context;
    }

    public static void overrideAnimation(Activity activity, int type){
       switch (type){
           case 0:
               activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
           case 1:
               activity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
               break;
       }
    }

}

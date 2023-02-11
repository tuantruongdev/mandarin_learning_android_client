package com.jtinteractive.mandarinlearning.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.data.remote.model.LocalUser;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;

public class ApplicationHelper {
    private static ApplicationHelper instance;
    private volatile Context applicationContext;
    private LocalUser localUser;


    public static ApplicationHelper getInstance() {
        if (instance == null) {
            instance = new ApplicationHelper();
        }
        return instance;
    }

    private ApplicationHelper() {
        localUser = null;
    }

    public LocalUser getLocalUser() {
        return localUser;
    }

    public void setLocalUser(LocalUser localUser) {
        this.localUser = localUser;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = ApplicationHelper.getInstance().getContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("user", gson.toJson(localUser)).apply();
    }

    public Context getContext() {
        return applicationContext;
    }

    public void init(Context context) {
        this.applicationContext = context;
    }

    public static void overrideAnimation(Activity activity, int type) {
        switch (type) {
            case 0:
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case 1:
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 2:
                activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }
    }


    private static int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    public static boolean isAlphaNumeric(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i)) && s.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

}

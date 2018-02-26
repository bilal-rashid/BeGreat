package com.guards.attendance.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guards.attendance.R;

import java.util.ArrayList;

public class ActivityUtils {

    public static void startActivity(Activity context, Class<?> class_, boolean isFinish) {
        Intent intent = new Intent(context, class_);
        context.startActivity(intent);
        if (isFinish)
            context.finish();
    }

//    public static void launchLogin(Activity activity, int REQUEST_EXIT) {
//        activity.startActivityForResult(new Intent(activity, LoginActivity.class), REQUEST_EXIT);
//        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }

    public static void startActivityForResult(Activity context, Class<?> class_, String fragmentName, Bundle bundle, int REQUEST_EXIT) {
        Intent intent = new Intent(context, class_);
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);
        context.startActivityForResult(intent, REQUEST_EXIT);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void startActivity(Activity context, Class<?> class_, String fragmentName, Bundle bundle) {
        Intent intent = new Intent(context, class_);
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public static void startActivity(Activity context, Class<?> class_, Bundle bundle) {
        Intent intent = new Intent(context, class_);
        intent.putExtra(Constants.DATA, bundle);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public static void startActivity(Context context, Class<?> class_, String fragmentName, Bundle bundle) {
        Intent intent = new Intent(context, class_);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> class_, String fragmentName, Bundle bundle, boolean isUpdateCurrent) {
        Intent intent = new Intent(context, class_);
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);
        context.startActivity(intent);
    }
    public static void startAlarmActivity(Context context, Class<?> class_, String fragmentName, Bundle bundle, boolean isUpdateCurrent) {
        Intent intent = new Intent(context, class_);
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }
    public static void centerToolbarTitle(@NonNull final Toolbar toolbar, Boolean padding) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER);
            if(padding) {
                titleView.setPadding(0, 0, AppUtils.dpToPx(65), 0);
            }
            else {
                titleView.setPadding(0, 0, AppUtils.dpToPx(20), 0);
            }
            if (titleView.getLayoutParams() instanceof Toolbar.LayoutParams) {
                final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                toolbar.requestLayout();
                Typeface exo2 = Typeface.createFromAsset(toolbar.getContext().getAssets(), "RobotoRegular.ttf");
                titleView.setTypeface(exo2);
                titleView.setTextColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorToolBarText));
            }
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }
    public static void centerToolbarTitleAdmin(@NonNull final Toolbar toolbar, Boolean padding) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER);
            if(padding) {
                titleView.setPadding(0, 0, AppUtils.dpToPx(25), 0);
            }
            else {
                titleView.setPadding(AppUtils.dpToPx(30), 0, 0, 0);
            }
            if (titleView.getLayoutParams() instanceof Toolbar.LayoutParams) {
                final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                toolbar.requestLayout();
                Typeface exo2 = Typeface.createFromAsset(toolbar.getContext().getAssets(), "RobotoRegular.ttf");
                titleView.setTypeface(exo2);
                titleView.setTextColor(ContextCompat.getColor(toolbar.getContext(), R.color.colorToolBarText));
            }
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }


    public static void startHomeActivity(Context context, Class<?> class_, String fragmentName) {
        Intent intent = new Intent(context, class_);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        context.startActivity(intent);
    }

    public static void startWifiSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

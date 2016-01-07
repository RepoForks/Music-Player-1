package ua.edu.cdu.fotius.lisun.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

public class IndeterminateProgressBarManager {

    private final String TAG = getClass().getSimpleName();

    private ProgressBar mProgressBar;
    private RelativeLayout.LayoutParams mProgressBarParams;

    private WeakReference<ViewGroup> mRootViewReference;

    public IndeterminateProgressBarManager(Context context) {
        initProgressBar(context);
        initProgressBarParams();
    }

    private void initProgressBar(Context context) {
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        mProgressBar.setIndeterminate(true);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
    }

    private void initProgressBarParams() {
        mProgressBarParams =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mProgressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    public void setRoot(ViewGroup root) {
        if(!(root instanceof RelativeLayout)) {
            throw new RuntimeException("You should provide RelativeLayout as root view " +
                    "for this View");
        }
        mRootViewReference = new WeakReference<ViewGroup>(root);
    }

    public void start() {
        ViewGroup root = retrieveRoot();
        root.addView(mProgressBar, mProgressBarParams);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void stop() {
        ViewGroup root = retrieveRoot();
        root.removeView(mProgressBar);
    }

    private ViewGroup retrieveRoot() {
        ViewGroup root;
        if((mRootViewReference != null) && ((root = mRootViewReference.get()) != null)) {
            return root;
        } else {
            throw new RuntimeException("You should provide fresh root view " +
                    "(with a help of setRoot(ViewGroup root) method) " +
                    "in which ProgressBar will be added.");
        }
    }
}

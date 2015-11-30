package ua.edu.cdu.fotius.lisun.musicplayer.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class OnOpenCloseNavigationViewClickListener implements View.OnClickListener{

    private DrawerLayout mNavigationViewDrawer;
    //TODO: maybe it's not needed
    private NavigationView mNavigationView;

    public OnOpenCloseNavigationViewClickListener(final DrawerLayout drawer,
                                                  final NavigationView navigationView) {
        mNavigationViewDrawer = drawer;
        mNavigationView = navigationView;
    }

    @Override
    public void onClick(View v) {
        mNavigationViewDrawer.openDrawer(GravityCompat.START);
    }
}

package com.trackme.trackmeapplication.home.userHome;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.account.network.AccountNetworkImp;
import com.trackme.trackmeapplication.account.network.AccountNetworkInterface;
import com.trackme.trackmeapplication.baseUtility.BaseActivityDelegate;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.service.health.HealthService;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 *  Delegate class that performs the creation of all menu (navigation view and navigation Bar)
 *  (delegate pattern)
 *
 * @author Mattia Tibaldi
 * @see BaseActivityDelegate
 */
public class UserHomeDelegate extends BaseActivityDelegate<
        UserHomeContract.UserHomeView,
        UserHomePresenter> implements
        NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public void onCreate(UserHomePresenter presenter) {
        super.onCreate(presenter);

        configureDrawer();
    }

    /**
     * Configure the drawer menu.
     */
    private void configureDrawer() {
        View navHeader = navigationView.getHeaderView(0);
        TextView headerUsername = navHeader.findViewById(R.id.nav_header_user_name);
        TextView headerSSN = navHeader.findViewById(R.id.nav_header_user_sn);
        TextView circle = navHeader.findViewById(R.id.textViewCircle);

        String token = mPresenter.getView().getToken();
        AccountNetworkInterface accountNetwork = AccountNetworkImp.getInstance();

        if (token != null && !token.isEmpty()) {
            try {
                User user = accountNetwork.getUser(token);
                headerUsername.setText(user.extractName());
                circle.setText(user.extractName().substring(0, 1));
                String ssn = "SSN: " + user.getSsn();
                headerSSN.setText(ssn);
                SharedPreferences sp = this.mPresenter.getView().getActivity()
                        .getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = df.format(user.getBirthDate());
                sp.edit().putString(Constant.BIRTH_DATE_KEY, formattedDate).apply();
            } catch (UserNotFoundException e) {
                mPresenter.getView().showMessage(mPresenter.getView().getActivity().getString(R.string.impossible_to_find_user_detail));
            } catch (ConnectionException e) {
                mPresenter.onConnectionError();
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mPresenter.getView().getActivity(),
                drawerLayout,
                toolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        MenuItem healthSwitch = navigationView.getMenu().getItem(4);
        MenuItem locationSwitch = navigationView.getMenu().getItem(3);
        healthSwitch.setActionView(R.layout.switch_item);
        locationSwitch.setActionView(R.layout.switch_item);
        Switch health = healthSwitch.getActionView().findViewById(R.id.switch_on_off);
        Switch location = locationSwitch.getActionView().findViewById(R.id.switch_on_off);

        health.setOnCheckedChangeListener((compoundButton, b) -> mPresenter.onHealthSwitch(b));
        location.setOnCheckedChangeListener((compoundButton, b) -> mPresenter.onLocationSwitch(b));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean check;
        switch (item.getItemId()) {
            case R.id.nav_profile:
                mPresenter.onProfileSelected();
                break;
            case R.id.nav_settings:
                mPresenter.onSettingsSelected();
                break;
            case R.id.nav_logout:
                mPresenter.onLogoutSelected();
                break;
        }
        closeDrawer();
        return true;
    }

    /**
     * Close the navigation view drawer.
     *
     * @return true if the operation can be performed, false otherwise
     */
    public boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Configure the toolBar menu.
     */
    public void configureToolbar(){
        UserPageAdapter pageAdapter = new UserPageAdapter(mPresenter.getView().getActivity().getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }

}

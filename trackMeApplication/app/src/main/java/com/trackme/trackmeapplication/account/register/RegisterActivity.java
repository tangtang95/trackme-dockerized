package com.trackme.trackmeapplication.account.register;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.trackme.trackmeapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RegisterActivity loads the layout of the Registration pages and it shows its main
 * menu.
 *
 * @author Mattia Tibaldi
 */
public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;

    /**
     * Load the layout and it instantiates the page adapter for managing the viewPager and the
     * tabLayout
     *
     * @param savedInstanceState the last saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        RegisterPageAdapter pageAdapter = new RegisterPageAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}

package com.ospinet.app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;




public class ShareMainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private ShareTabsPagerAdapter mAdapter;
	private ActionBar actionBar;
    ProgressDialog dialog;

	// Tab titles
	private String[] tabs = { "Share Records", "Share Files" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.share_activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pagers);
		actionBar = getActionBar();
		mAdapter = new ShareTabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
	//	actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
    public void onBackPressed() {
        Intent i = new Intent(ShareMainActivity.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}

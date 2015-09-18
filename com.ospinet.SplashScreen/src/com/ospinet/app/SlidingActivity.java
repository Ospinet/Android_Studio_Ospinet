package com.ospinet.app;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class SlidingActivity extends Activity  {

    SlidingPaneLayout mSlidingPanel;
    ListView mMenuList;
   // ImageView mMenuIcons;
    ImageView appImage;
    TextView TitleText;

   // String [] MenuTitles = new String[]{"Home","Settings","Records","Share", "Search", "Help", "Logout"};
   // Integer[] MenuIcons = new Integer[]{R.drawable.home_32, R.drawable.settings_32, R.drawable.records, R.drawable.share_32, R.drawable.search_32, R.drawable.help_32, R.drawable.logout_32};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
        mSlidingPanel = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
      //  mMenuIcons = (ImageView)findViewById(R.id.MenuIcons);
        appImage = (ImageView)findViewById(android.R.id.home);

       TitleText = (TextView)findViewById(android.R.id.title);

      //  mMenuList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,MenuTitles));

        mSlidingPanel.setPanelSlideListener(panelListener);
        mSlidingPanel.setParallaxDistance(200);

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


    }


    PanelSlideListener panelListener = new PanelSlideListener(){

        @Override
        public void onPanelClosed(View arg0) {
            // TODO Auto-genxxerated method stub
            // getActionBar().setTitle(getString(R.string.app_name));
            appImage.animate().rotation(0);
        }

        @Override
        public void onPanelOpened(View arg0) {
            // TODO Auto-generated method stub
            getActionBar().setTitle("OSPINET");
            appImage.animate().rotation(90);
        }

        @Override
        public void onPanelSlide(View arg0, float arg1) {
            // TODO Auto-generated method stub

        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mSlidingPanel.isOpen()){
                    appImage.animate().rotation(0);
                    mSlidingPanel.closePane();
                    getActionBar().setTitle(getString(R.string.app_name));
                }
                else{
                    appImage.animate().rotation(90);
                    mSlidingPanel.openPane();
                    getActionBar().setTitle("OSPINET");
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
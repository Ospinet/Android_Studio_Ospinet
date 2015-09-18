package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

public class PreMemberHome extends Activity {
    ProgressDialog dialog;
    private SlidingPaneLayout mSlidingLayout;

    ImageView appImage;
    SlidingPaneLayout mSlidingPanel;
    TextView txtname;
    ImageView imageView_round;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_sliding);

        dialog = new ProgressDialog(PreMemberHome.this);
        txtname = (TextView) findViewById(R.id.txtName);
        imageView_round = (ImageView) findViewById(R.id.imageView_round);
        mSlidingPanel = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        appImage = (ImageView)findViewById(android.R.id.home);
        mSlidingPanel.setPanelSlideListener(panelListener);
        mSlidingPanel.setParallaxDistance(200);

        SharedPreferences myPrefs = PreMemberHome.this
                .getSharedPreferences("remember", MODE_PRIVATE);
        String fname = myPrefs.getString("fname", null);
        String lname = myPrefs.getString("lname", null);
        String profile_pic = myPrefs.getString("profile_pic", "null");
        String type = myPrefs.getString("type", null);
        txtname.setText(fname +" " + lname);
        String profile_image = (profile_pic +"_250." + type);
        AQuery androidAQuery = new AQuery(
                PreMemberHome.this);

        if(profile_pic == null || profile_pic.equals("null") || profile_pic.equals("")){
            androidAQuery.id(imageView_round).image(
                    "http://ospinet.com/assets/images/people/250/default_avatar_250x250.png", false, false,0, 0);   //"http://ospinet.com/assets/images/people/250/default_avatar_250x250.png"
        }else{
            androidAQuery.id(imageView_round).image(
                    "http://ospinet.com/profile_pic/member_pic_250/" + profile_image, false, false,0, 0);   //"http://ospinet.com/profile_pic/member_pic_250/" + profile_image;
        }
        showActionBar();
        new GetNotificationsCount().execute();
        new GetFriendRequestCount().execute();

        getActionBar().setIcon(getResources().getDrawable(R.drawable.menu_icon));
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        side_menu();

    }

    SlidingPaneLayout.PanelSlideListener panelListener = new SlidingPaneLayout.PanelSlideListener(){

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

    public class GetFriendRequestCount extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String Friend_Request_count = "";
            try {
                ArrayList<NameValuePair> Friend_Request = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = PreMemberHome.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                Friend_Request.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_request_count",
                                Friend_Request);
                Friend_Request_count = response.toString();

            } catch (Exception io) {

            }
            return Friend_Request_count;
        }
        protected void onPostExecute(String Friend_Request_count) {
            if(!Friend_Request_count.replace("\n"," ").equals("0")){
                TextView friend_count = (TextView) findViewById(R.id.actionbar_notifcation_textview);
                friend_count.setVisibility(View.VISIBLE);
                friend_count.setText(Friend_Request_count);
            }
        }

    }

    public class GetNotificationsCount extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String Count = "";
            try {
                ArrayList<NameValuePair> Notification_count = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = PreMemberHome.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                Notification_count.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_notification_count",
                                Notification_count);
                Count = response.toString();

            } catch (Exception io) {

            }
            return Count;
        }
        protected void onPostExecute(String Count) {
            if(!Count.replace("\n","").equals("0")){
                TextView notification_count = (TextView) findViewById(R.id.actionbar_notifcation_textview2);
                notification_count.setVisibility(View.VISIBLE);
                notification_count.setText(Count);
            }
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(PreMemberHome.this,LoginActivity.class);
        this.startActivity(i);
        finish();

    }
    public void showMembers(View v)
    {
        try
        {
            Intent intent = new Intent(PreMemberHome.this, Member_Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);//finish();

        }
        catch(Exception ex)
        {

        }
    }
    public void records(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, RecordsList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("EXIT", true);

            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
    public void contacts(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, ContactsList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("EXIT", true);

            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
    public void Search(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, SearchMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
    public void Profile(View v)
    {
        try
        {
            Intent intent = new Intent(PreMemberHome.this, Profile_view.class);
            PreMemberHome.this.startActivity(intent);

        }
        catch(Exception ex)
        {
            System.out.println("Error");
        }
    }
    public void Share(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, ShareMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
    public void side_menu(){
        findViewById(R.id.side_navigation_menu_item8).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, PreMemberHome.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item7).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, Profile_view.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item7).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, Profile_view.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item2).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, RecordsList.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item5).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, ShareMainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item6).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, SearchMainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item3).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, help.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
        findViewById(R.id.side_navigation_menu_item1).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(PreMemberHome.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        PreMemberHome.this.startActivity(i);
                    }
                });
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu1, null);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
        ImageButton menu = (ImageButton) v.findViewById(R.id.options);
        menu.setVisibility(View.INVISIBLE);
        ImageButton search_contacts = (ImageButton) v.findViewById(R.id.search_contacts);
        search_contacts.setVisibility(View.INVISIBLE);
        ImageButton imgbell = (ImageButton) v.findViewById(R.id.notifications);
        ImageButton imgfriend = (ImageButton) v.findViewById(R.id.friendrequest);

        TextView friend_count = (TextView) findViewById(R.id.actionbar_notifcation_textview);
        friend_count.setVisibility(View.INVISIBLE);

        TextView notification_count = (TextView) findViewById(R.id.actionbar_notifcation_textview2);
        notification_count.setVisibility(View.INVISIBLE);

        imgfriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PreMemberHome.this, Friend_requests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                PreMemberHome.this.startActivity(intent);

            }
        });

        imgbell.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PreMemberHome.this, Notifications_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                PreMemberHome.this.startActivity(intent);

            }
        });
        imgAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PreMemberHome.this, MemberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                PreMemberHome.this.startActivity(intent);

            }
        });

        ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

        imgLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(PreMemberHome.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        txtLogoName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(PreMemberHome.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    }


}
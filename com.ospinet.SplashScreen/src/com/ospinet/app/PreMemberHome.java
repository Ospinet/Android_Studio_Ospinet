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
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class PreMemberHome extends Activity implements ISideNavigationCallback {
    private SideNavigationView sideNavigationView;
    ProgressDialog dialog;

    TextView txtname;
    ImageView imageView_round;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.home_new);

        dialog = new ProgressDialog(PreMemberHome.this);
        txtname = (TextView) findViewById(R.id.txtName);
        imageView_round = (ImageView) findViewById(R.id.imageView_round);
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

        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);
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
            if(!Friend_Request_count.replace("\n","").equals("0")){
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
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(PreMemberHome.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                PreMemberHome.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(PreMemberHome.this, Member_Home.class);
                PreMemberHome.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(PreMemberHome.this, com.ospinet.app.help.class);
                PreMemberHome.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(PreMemberHome.this, PreMemberHome.class);
                PreMemberHome.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(PreMemberHome.this, ShareMainActivity.class);
                PreMemberHome.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(PreMemberHome.this, SearchMainActivity.class);
                PreMemberHome.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
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
    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu1, null);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
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
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
                rel.bringChildToFront(sideNavigationView);
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
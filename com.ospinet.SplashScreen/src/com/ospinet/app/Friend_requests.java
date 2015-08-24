package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Friend_requests extends Activity implements ISideNavigationCallback {
    ProgressDialog dialog;
    ListView FriendRequestList;
    TextView txtNoRec;
    Button Confirm,Ignore;
    private SideNavigationView sideNavigationView;
    ArrayList<Friend_request_notification> req_notify;
    public static Friend_Request_NotificationAdapter rad;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.friend_request);
        showActionBar();
        FriendRequestList = (ListView) findViewById(R.id.FriendRequestList);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(SideNavigationView.Mode.LEFT);

        dialog = new ProgressDialog(Friend_requests.this);
        txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
        txtNoRec.setVisibility(View.INVISIBLE);

        //super.onCreate(savedInstanceState);
        req_notify = new ArrayList<Friend_request_notification>();
        new Notifications().execute();
    }

    public class Notifications extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Please Wait..");
                dialog.show();
                dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String NotifyString = "";
            try {
                ArrayList<NameValuePair> notify_request = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Friend_requests.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                notify_request.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_Friend_request",
                                notify_request);
                NotifyString = response.toString();
            } catch (Exception io) {

            }
            return NotifyString;

        }
        @Override
        protected void onPostExecute(String NotifyString) {
            if (dialog.isShowing())
                dialog.dismiss();
            JSONObject jsonResponse = null;
            try {
                String id="";
                String type = "";
                String user_id ="";
                String profile ="";
                String fname = "";
                String lname = "";
                jsonResponse = new JSONObject(NotifyString);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse
                        .optJSONArray("result");
                if(jsonMainNode!=null)
                {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                    	
                            JSONArray jsonChildArray = jsonMainNode.getJSONArray(i);
                            if(jsonChildArray!=null)
                            {
                            JSONObject jsonChildNode = jsonChildArray.getJSONObject(i);
                            if(jsonChildNode!=null)
                            {
                            id = jsonChildNode.optString("id");
                            type = jsonChildNode.optString("type");
                            user_id =  jsonChildNode.optString("userid");
                            profile =  jsonChildNode.optString("profile_pic");
                            fname = jsonChildNode.optString("fname");
                            lname = jsonChildNode.optString("lname");
                            Friend_request_notification r = new Friend_request_notification();
                            r.settype(type);
                            r.setuser_id(user_id);
                            r.setprofile(profile);
                            r.setid(id);
                            r.setfname(fname);
                            r.setlname(lname);
                            req_notify.add(r);
                            flag=1;
                            }
                            }
                        }
                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        FriendRequestList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        FriendRequestList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    FriendRequestList.setVisibility(View.GONE);
                }
                rad = new Friend_Request_NotificationAdapter(Friend_requests.this,
                        req_notify);

                FriendRequestList.setAdapter(rad);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Friend_requests.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }
    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu4, null);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);
        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        TextView txtEdit = (TextView) v.findViewById(R.id.txtEdit);
        txtEdit.setVisibility(View.INVISIBLE);

        txtLogoName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Friend_requests.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        imgMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
                rel.bringChildToFront(sideNavigationView);

            }
        });

    }
    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(Friend_requests.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Friend_requests.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Friend_requests.this, Member_Home.class);
                Friend_requests.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Friend_requests.this, help.class);
                Friend_requests.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(Friend_requests.this, PreMemberHome.class);
                Friend_requests.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(Friend_requests.this, ShareMainActivity.class);
                Friend_requests.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(Friend_requests.this, SearchMainActivity.class);
                Friend_requests.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }

}

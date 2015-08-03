package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

public class Notifications_Details extends Activity implements ISideNavigationCallback {
    ProgressDialog dialog;
    ListView NotificationList;
    TextView txtNoRec;
    private SideNavigationView sideNavigationView;
    ArrayList<notification> arrnotify;
    public static NotificationAdapter rad;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.notifications);
        showActionBar();
        NotificationList = (ListView) findViewById(R.id.NotificationList);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(SideNavigationView.Mode.LEFT);

        dialog = new ProgressDialog(Notifications_Details.this);
        txtNoRec = (TextView) findViewById(R.id.txt_home_norec);

        //super.onCreate(savedInstanceState);
        arrnotify = new ArrayList<notification>();
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
                ArrayList<NameValuePair> notify = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Notifications_Details.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                notify.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_notifications",
                                notify);
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
                String type_id = "";
                String from_user_id ="";
                String member_record_id ="";
                String email = "";
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
                            type_id = jsonChildNode.optString("type_id");
                            from_user_id =  jsonChildNode.optString("from_user_id");
                            member_record_id =  jsonChildNode.optString("member_record_id");
                            email = jsonChildNode.optString("email");
                            fname = jsonChildNode.optString("fname");
                            lname = jsonChildNode.optString("lname");
                            notification r = new notification();
                            r.settype_id(type_id);
                            r.setfrom_user_id(from_user_id);
                            r.setmember_record_id(member_record_id);
                            r.setid(id);
                            r.setemail(email);
                            r.setfname(fname);
                            r.setlname(lname);
                            arrnotify.add(r);
                            flag=1;
                            }
                            }
                        }
                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        NotificationList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        NotificationList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    NotificationList.setVisibility(View.GONE);
                }
                rad = new NotificationAdapter(Notifications_Details.this,
                        arrnotify);

                NotificationList.setAdapter(rad);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Notifications_Details.this, PreMemberHome.class);
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
        TextView edit=(TextView) v.findViewById(R.id.txtEdit);
        edit.setVisibility(View.INVISIBLE);

        ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

        imgLogo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Notifications_Details.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        txtLogoName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Notifications_Details.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

    }
    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(Notifications_Details.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Notifications_Details.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Notifications_Details.this, Member_Home.class);
                Notifications_Details.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Notifications_Details.this, com.ospinet.app.help.class);
                Notifications_Details.this.startActivity(help);

                break;

            default:
                return;
        }
        // finish();
    }

}

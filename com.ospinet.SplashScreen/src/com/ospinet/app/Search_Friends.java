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
import android.widget.EditText;
import android.widget.ExpandableListView;
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

public class Search_Friends extends Activity implements ISideNavigationCallback {
    ProgressDialog dialog;
    ListView FriendsList;
    TextView txtNoRec;
    public static String search_string;
    public static EditText search_txt;
    Button Confirm,Ignore;
    private SideNavigationView sideNavigationView;
    ArrayList<Friend_search> friend_search;
    public static Search_Friend_Adapter rad;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.search_friends);
        showActionBar();
        FriendsList = (ExpandableListView) findViewById(R.id.FriendsList);
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(SideNavigationView.Mode.LEFT);

        dialog = new ProgressDialog(Search_Friends.this);
        txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
        search_txt = (EditText) findViewById(R.id.search);
        search_string = search_txt.getText().toString();

        //super.onCreate(savedInstanceState);
        friend_search = new ArrayList<Friend_search>();
        new search_friends().execute();
    }

    public class search_friends extends AsyncTask<String, String, String> {

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
                ArrayList<NameValuePair> friend_search = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Search_Friends.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                friend_search.add(new BasicNameValuePair("user_id",userId));
                friend_search.add(new BasicNameValuePair("search",search_string));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/search_friends",
                                friend_search);
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
                String login_status ="";
                String profile ="";
                String fname = "";
                String lname = "";
                String ns = "";
                String uid = "";
                String email = "";
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
                            profile =  jsonChildNode.optString("profile_pic");
                            fname = jsonChildNode.optString("fname");
                            lname = jsonChildNode.optString("lname");
                            login_status = jsonChildNode.optString("login_status");
                            ns = jsonChildNode.optString("ns");
                            uid = jsonChildNode.optString("uid");
                            email = jsonChildNode.optString("email");
                            Friend_search r = new Friend_search();
                            r.settype(type);
                            r.setprofile(profile);
                            r.setid(id);
                            r.setfname(fname);
                            r.setlname(lname);
                            r.setemail(email);
                            r.setlogin_status(login_status);
                            r.setns(ns);
                            r.setuid(uid);
                            friend_search.add(r);
                            flag=1;
                            }
                            }
                        }
                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        FriendsList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        FriendsList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    FriendsList.setVisibility(View.GONE);
                }
                rad = new Search_Friend_Adapter(Search_Friends.this,
                        friend_search);

                FriendsList.setAdapter(rad);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Confirm = (Button) findViewById(R.id.btnConfirm);
            Ignore = (Button) findViewById(R.id.btnIgnore);

            Confirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(Search_Friends.this, "CONFIRM", Toast.LENGTH_LONG).show();
                }
            });

            Ignore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(Search_Friends.this, "IGNORE", Toast.LENGTH_LONG).show();                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Search_Friends.this, PreMemberHome.class);
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
                Intent i = new Intent(Search_Friends.this,PreMemberHome.class);
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
                Intent i = new Intent(Search_Friends.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Search_Friends.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Search_Friends.this, Member_Home.class);
                Search_Friends.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Search_Friends.this, help.class);
                Search_Friends.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(Search_Friends.this, PreMemberHome.class);
                Search_Friends.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(Search_Friends.this, ShareMainActivity.class);
                Search_Friends.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(Search_Friends.this, SearchMainActivity.class);
                Search_Friends.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }

}

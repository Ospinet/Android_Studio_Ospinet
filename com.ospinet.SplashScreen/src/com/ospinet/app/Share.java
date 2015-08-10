package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;


public class Share extends  Activity implements ISideNavigationCallback {
	private SideNavigationView sideNavigationView;
    String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname,toemail,friends_ids;
    public static String FriendsIds;
    Button btnCancel, btnShare;
    public static String user_id;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.share);
        showActionBar();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

        toemail = getIntent().getStringExtra("friends_email_ids");
        friends_ids = getIntent().getStringExtra("friends_ids");
        recId = getIntent().getStringExtra("record_id");
        memfname = getIntent().getStringExtra("member_fname");
        memlname = getIntent().getStringExtra("member_lname");
        memid = getIntent().getStringExtra("member_id");
        recDesc = getIntent().getStringExtra("record_desc");
        recDate = getIntent().getStringExtra("record_date");
        recTitle = getIntent().getStringExtra("record_title");
        recTag = getIntent().getStringExtra("record_tag");
        SharedPreferences myPrefs = Share.this
                .getSharedPreferences("remember", MODE_PRIVATE);
        user_id = myPrefs.getString("userid", null);
        String fname = myPrefs.getString("fname", null);
        String lname = myPrefs.getString("lname", null);

        if(toemail != null && friends_ids != null){
            // String[] splits = toemail.split(";");
            final EditText ToEmail = (EditText) findViewById(R.id.ToEmail);
            ToEmail.setText(toemail);

            if (friends_ids.endsWith(",")) {
                FriendsIds = friends_ids.substring(0, friends_ids.length() - 1);
            }
        }else
        {
            Toast.makeText(Share.this, "Select contacts from contacts list", Toast.LENGTH_LONG).show();
        }
        final EditText YourEmail = (EditText) findViewById(R.id.YourEmail);
        YourEmail.setText(fname +" " + lname);

        final TextView titleToChange = (TextView) findViewById(R.id.RTitle);
        titleToChange.setText(recTitle);

        final TextView tagToChange = (TextView) findViewById(R.id.RTags);
        tagToChange.setText(recTag);

        final TextView descToChange = (TextView) findViewById(R.id.RDesc);
        descToChange.setText(recDesc);

        final TextView dateToChange = (TextView) findViewById(R.id.RDate);
        dateToChange.setText(recDate);

        final TextView ownerToChange = (TextView) findViewById(R.id.OwnerName);
        ownerToChange.setText(memfname +' '+ memlname);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnShare = (Button) findViewById(R.id.btnShare);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent records = new Intent(Share.this, ShareMainActivity.class);
                Share.this.startActivity(records);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toemail != null && FriendsIds != null)
                {
                    new Share_record().execute();
                }
                else
                {
                    Toast.makeText(Share.this, "Select at lest one contact", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onBackPressed() {
        Intent i = new Intent(Share.this, ShareMainActivity.class);
        this.startActivity(i);
        finish();
    }

    public class Share_record extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String retstring = "";
            try {
                ArrayList<NameValuePair> share_record = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Share.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                share_record.add(new BasicNameValuePair("from_id",userId));
                share_record.add(new BasicNameValuePair("to_id",FriendsIds));
                share_record.add(new BasicNameValuePair("record_id",recId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/share_records",
                                share_record);
                retstring = response.toString();

            } catch (Exception io) {

            }
            return retstring;
        }
        protected void onPostExecute(String retstring) {

            Toast.makeText(Share.this, retstring, Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(Share.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Share.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Share.this, Member_Home.class);
                Share.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Share.this, com.ospinet.app.help.class);
                Share.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(Share.this, PreMemberHome.class);
                Share.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(Share.this, ShareMainActivity.class);
                Share.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(Share.this, SearchMainActivity.class);
                Share.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflator.inflate(R.layout.menu6, null);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowHomeEnabled (false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setCustomView(v);
    TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);
    ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
    TextView txtSelectContact = (TextView) v.findViewById(R.id.txtSelectContact);

        txtSelectContact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Share.this, ContactsMainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                i.putExtra("friends_email_ids",toemail);
                i.putExtra("friends_ids",friends_ids);
                i.putExtra("record_id",recId);
                i.putExtra("member_fname",memfname);
                i.putExtra("member_lname",memlname);
                i.putExtra("member_id",memid);
                i.putExtra("record_desc",recDesc);
                i.putExtra("record_date", recDate);
                i.putExtra("record_title",recTitle);
                i.putExtra("record_tag",recTag);
                Share.this.startActivity(i);
            }
        });

    txtLogoName.setOnClickListener(new OnClickListener() {

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(Share.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
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

    }

}



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
import android.view.Window;
import android.view.View.OnClickListener;
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


public class help extends  Activity implements ISideNavigationCallback {
	private SideNavigationView sideNavigationView;
    EditText To_Email, edtEmail, Description;
    Button Cancel, Send;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.help);
        showActionBar();
        new GetNotificationsCount().execute();
        new GetFriendRequestCount().execute();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

        To_Email = (EditText) findViewById(R.id.edtTo_Email);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        Description = (EditText) findViewById(R.id.editDescription);
        Cancel = (Button) findViewById(R.id.btnCancel);
        Send = (Button) findViewById(R.id.btnSend);
        SharedPreferences myPrefs = help.this
                .getSharedPreferences("remember", MODE_PRIVATE);
        String From_email = myPrefs.getString("email", null);
        edtEmail.setText(From_email);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent records = new Intent(help.this, Member_Home.class);
                help.this.startActivity(records);
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation val = new Validation();
                if(val.Is_Valid_Email(edtEmail) && !Description.getText().toString().trim().equals(""))
                {
                    Toast.makeText(help.this, "Problem in network.. Try after some time", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(help.this, "Please enter your email address and Problem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onBackPressed() {
        Intent i = new Intent(help.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(help.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                help.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(help.this, Member_Home.class);
                help.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(help.this, help.class);
                help.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(help.this, PreMemberHome.class);
                help.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(help.this, ShareMainActivity.class);
                help.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(help.this, SearchMainActivity.class);
                help.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
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
    ImageButton imgbell = (ImageButton) v.findViewById(R.id.notifications);
    ImageButton imgfriend = (ImageButton) v.findViewById(R.id.friendrequest);
    ImageButton search_contacts = (ImageButton) v.findViewById(R.id.search_contacts);
    search_contacts.setVisibility(View.INVISIBLE);
    imgAdd.setOnClickListener(new OnClickListener() {

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent intent = new Intent(help.this, MemberActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra("EXIT", true);
    		help.this.startActivity(intent);
            
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
        imgfriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(help.this, Friend_requests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                help.this.startActivity(intent);

            }
        });

        imgbell.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(help.this, Notifications_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                help.this.startActivity(intent);

            }
        });
    TextView friend_count = (TextView) findViewById(R.id.actionbar_notifcation_textview);
    friend_count.setVisibility(View.INVISIBLE);

    TextView notification_count = (TextView) findViewById(R.id.actionbar_notifcation_textview2);
    notification_count.setVisibility(View.INVISIBLE);

    ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
    TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

    imgLogo.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(help.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
    txtLogoName.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(help.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });



    }
    public class GetFriendRequestCount extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String Friend_Request_count = "";
            try {
                ArrayList<NameValuePair> Friend_Request = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = help.this
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
                SharedPreferences myPrefs =help.this
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

}



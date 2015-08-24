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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactsList extends Activity implements ISideNavigationCallback {
	ListView contactList;
	ProgressDialog dialog;
    ArrayList<contact> arrcontacts;
    public static ContactAdapter rad;
	TextView txtNoRec;
	private SideNavigationView sideNavigationView;

	Button btnNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.contacts);
		showActionBar();
        new GetNotificationsCount().execute();
        new GetFriendRequestCount().execute();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

		dialog = new ProgressDialog(ContactsList.this);
        contactList = (ListView) findViewById(R.id.contactList);
		txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
		txtNoRec.setVisibility(View.GONE);
        btnNew= (Button) findViewById(R.id.btnNewAdd);
		btnNew.setVisibility(View.GONE);
        arrcontacts = new ArrayList<contact>();
		new Loadcontacts().execute();
	}

	public void AddMember(View v) {
		try {
			Intent intent = new Intent(ContactsList.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			ContactsList.this.startActivity(intent);
			//finish();
		} catch (Exception ex) {

		}
	}

    public class Loadcontacts extends AsyncTask<String, String, String> {
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
            String retstring = "";
            try {
                ArrayList<NameValuePair> allcontacts = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = ContactsList.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                allcontacts.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_contacts",
                                allcontacts);
                retstring = response.toString();
            } catch (Exception io) {

            }
            return retstring;

        }

        @Override
        protected void onPostExecute(String retstring) {
            if (dialog.isShowing())
                dialog.dismiss();
            JSONObject jsonResponse = null;
            try {
                String Friend_id="";
                String First_Name = "";
                String Last_Name ="";
                String Email ="";
                jsonResponse = new JSONObject(retstring);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse
                        .optJSONArray("result");
                if(jsonMainNode!=null)
                {
                    for (int i = 0; i < jsonMainNode.length(); i++) {

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Friend_id = jsonChildNode.optString("friend_id");
                        First_Name = jsonChildNode.optString("fname");
                        Last_Name = jsonChildNode.optString("lname");
                        Email =  jsonChildNode.optString("email");
                        contact r = new contact();
                        r.setfriend_id(Friend_id);
                        r.setFname(First_Name);
                        r.setLname(Last_Name);
                        r.setemail(Email);
                        arrcontacts.add(r);
                        flag=1;
                    }

                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        btnNew.setVisibility(View.VISIBLE);
                        contactList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        btnNew.setVisibility(View.GONE);
                        contactList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    btnNew.setVisibility(View.VISIBLE);
                    contactList.setVisibility(View.GONE);
                }
                rad = new ContactAdapter(ContactsList.this,
                        arrcontacts);

                contactList.setAdapter(rad);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	public class DeleteMember extends AsyncTask<String, String, String> {

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
			String retstring = "";
			String member_id = params[0];
			try {

				SharedPreferences myPrefs = ContactsList.this
						.getSharedPreferences("remember", MODE_PRIVATE);
				String user_id = myPrefs.getString("userid", null);
				ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
				loginParam.add(new BasicNameValuePair("member_id", member_id));

				String response = CustomHttpClient
						.executeHttpPost(
								"http://ospinet.com/app_ws/android_app_fun/delete_member",
								loginParam);
				retstring = response.toString();

			} catch (Exception io) {

			}
			return retstring;

		}

		@Override
		protected void onPostExecute(String retstring) {
			if (dialog.isShowing())
				dialog.dismiss();

			if (retstring.contains("success = 1")) {
				Toast.makeText(ContactsList.this, "Member deleted successfully",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(ContactsList.this, ContactsList.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("EXIT", true);

				ContactsList.this.startActivity(i);

			}

		}
	}

	public void new_member(View v)
	{
		try {
			Intent intent = new Intent(ContactsList.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			ContactsList.this.startActivity(intent);
			//finish();
		} catch (Exception ex) {

		}
	}
	
	@Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(ContactsList.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                ContactsList.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(ContactsList.this, ContactsList.class);
                ContactsList.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(ContactsList.this, help.class);
                ContactsList.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(ContactsList.this, PreMemberHome.class);
                ContactsList.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(ContactsList.this, ShareMainActivity.class);
                ContactsList.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(ContactsList.this, SearchMainActivity.class);
                ContactsList.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(ContactsList.this,PreMemberHome.class);
		this.startActivity(i);
		finish();

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
    imgAdd.setVisibility(View.INVISIBLE);
    ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
    ImageButton imgSearch_contacts = (ImageButton) v.findViewById(R.id.search_contacts);
    ImageButton imgbell = (ImageButton) v.findViewById(R.id.notifications);
    ImageButton imgfriend = (ImageButton) v.findViewById(R.id.friendrequest);
    imgSearch_contacts.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(ContactsList.this, Search_Friends.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			ContactsList.this.startActivity(intent);
            
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
                Intent intent = new Intent(ContactsList.this, Friend_requests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                ContactsList.this.startActivity(intent);

            }
        });

        imgbell.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ContactsList.this, Notifications_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                ContactsList.this.startActivity(intent);

            }
        });

	ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
	TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);
	
	imgLogo.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(ContactsList.this,PreMemberHome.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
		}
	});
	txtLogoName.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(ContactsList.this,PreMemberHome.class);
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
                SharedPreferences myPrefs = ContactsList.this
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
                friend_count.setText(Friend_Request_count);
            }else{
                TextView friend_count = (TextView) findViewById(R.id.actionbar_notifcation_textview);
                friend_count.setVisibility(View.GONE);
            }
        }

    }

    public class GetNotificationsCount extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String Count = "";
            try {
                ArrayList<NameValuePair> Notification_count = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = ContactsList.this
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
                notification_count.setText(Count);
            }else{
                TextView notification_count = (TextView) findViewById(R.id.actionbar_notifcation_textview2);
                notification_count.setVisibility(View.INVISIBLE);
            }
        }

    }
	 
}

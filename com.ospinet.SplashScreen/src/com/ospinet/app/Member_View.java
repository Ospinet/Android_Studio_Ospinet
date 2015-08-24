package com.ospinet.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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



import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class Member_View extends Activity implements ISideNavigationCallback {
	String memid;
	ProgressDialog dialog;
	ArrayList<member> arrMember;
	EditText edtFname, edtLname, edtEmail, edtAge;
	Button btnAdd, btnCancel, btnMale, btnFemale, btnRecord;
	String id, fname, lname, age, gender, email;
	private SideNavigationView sideNavigationView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.member_view);
		showActionBar();
		 sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
	        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
	        sideNavigationView.setMenuClickCallback(this);
	        sideNavigationView.setMode(Mode.LEFT);

		dialog = new ProgressDialog(Member_View.this);
		arrMember = new ArrayList<member>();
		memid = getIntent().getStringExtra("member_id");
		edtFname = (EditText) findViewById(R.id.edtFname);
		edtLname = (EditText) findViewById(R.id.edtlname);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtAge = (EditText) findViewById(R.id.edtAge);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnMale = (Button) findViewById(R.id.btnMale);
		btnFemale = (Button) findViewById(R.id.btnFemale);
		btnRecord = (Button) findViewById(R.id.btnRecords);
		new GetMemberDetails().execute();
	}

	public class GetMemberDetails extends AsyncTask<String, String, String> {

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

				String response = CustomHttpClient
						.executeHttpGet("http://ospinet.com/app_ws/android_app_fun/get_members_details?member_id="
								+ memid);
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
				jsonResponse = new JSONObject(retstring);

				JSONArray jsonMainNode = jsonResponse
						.optJSONArray("member_detail");
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					id = jsonChildNode.optString("id");
					fname = jsonChildNode.optString("fname");
					lname = jsonChildNode.optString("lname");
					gender = jsonChildNode.optString("gender");
					age = jsonChildNode.optString("age");
					email = jsonChildNode.optString("email");

					// code to set text and button
					edtFname.setText(fname);
					edtLname.setText(lname);
					edtEmail.setText(email);
					edtAge.setText(age);
					if (gender.toLowerCase().equals("male")) {
						btnMale.setBackgroundColor(Color.rgb(72, 173, 227));
                        btnMale.setTextColor(Color.WHITE);
                        btnFemale.setTextColor(Color.BLACK);
						btnFemale.setBackgroundResource(R.drawable.button_colour);

					} else {
						btnFemale.setBackgroundColor(Color.rgb(72, 173, 227));
                        btnFemale.setTextColor(Color.WHITE);
                        btnMale.setTextColor(Color.BLACK);
						btnMale.setBackgroundResource(R.drawable.button_colour);

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(Member_View.this, Member_Home.class);
		this.startActivity(i);
	//	finish();
	}

	public void viewRecords(View v)
	{
		try
		{
			Intent i = new Intent(Member_View.this, Records_Home.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("EXIT", true);
			
			i.putExtra("member_id", memid);
			Member_View.this.startActivity(i);
	
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
    imgAdd.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Member_View.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Member_View.this.startActivity(intent);
            
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
			Intent intent = new Intent(Member_View.this, Friend_requests.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Member_View.this.startActivity(intent);

		}
	});

	imgbell.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Member_View.this, Notifications_Details.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Member_View.this.startActivity(intent);

		}
	});
    ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
	TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);
	
	imgLogo.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Member_View.this,PreMemberHome.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
		}
	});
	txtLogoName.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Member_View.this,PreMemberHome.class);
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
                        Intent i = new Intent(Member_View.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        Member_View.this.startActivity(i);

                        break;

                    case R.id.side_navigation_menu_item2:
                        Intent records = new Intent(Member_View.this, Member_Home.class);
                        Member_View.this.startActivity(records);

                        break;

                    case R.id.side_navigation_menu_item3:
                        Intent help = new Intent(Member_View.this, help.class);
                        Member_View.this.startActivity(help);

                        break;

                    case R.id.side_navigation_menu_item4:
                        Intent home = new Intent(Member_View.this, PreMemberHome.class);
                        Member_View.this.startActivity(home);

                        break;

                    case R.id.side_navigation_menu_item5:
                        Intent share = new Intent(Member_View.this, ShareMainActivity.class);
                        Member_View.this.startActivity(share);

                        break;

                    case R.id.side_navigation_menu_item6:
                        Intent search = new Intent(Member_View.this, SearchMainActivity.class);
                        Member_View.this.startActivity(search);

                        break;

                    default:
                        return;
                }
                // finish();
            }

}

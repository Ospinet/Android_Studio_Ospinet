package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
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

public class RecordsList extends Activity implements ISideNavigationCallback {
    private SideNavigationView sideNavigationView;
    String memid;
    ArrayList<record> arrrecords;
    public static RecordAdapter rad;
    ProgressDialog dialog;
    ListView recordList;
    TextView txtNoRec;

    public static View rootView;

	Button btnNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.records);
		showActionBar();
        new GetNotificationsCount().execute();
        new GetFriendRequestCount().execute();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

		dialog = new ProgressDialog(RecordsList.this);
        recordList = (ListView) findViewById(R.id.recordList);
		txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
		btnNew= (Button) findViewById(R.id.btnNewAdd);
		txtNoRec.setVisibility(View.GONE);
		btnNew.setVisibility(View.GONE);
        arrrecords = new ArrayList<record>();
        new Loadrecord().execute();
	}

	public void AddMember(View v) {
		try {
			Intent intent = new Intent(RecordsList.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			RecordsList.this.startActivity(intent);
			//finish();
		} catch (Exception ex) {

		}
	}

    public class Loadrecord extends AsyncTask<String, String, String> {

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
                ArrayList<NameValuePair> allrecords = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = RecordsList.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                allrecords.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_all_records",
                                allrecords);
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
                String id="";
                String Description = "";
                String Date ="";
                String Title ="";
                String Tag = "";
                String member_id = "";
                String AttachmentPath = "";
                String fname ="";
                String lname="";
                jsonResponse = new JSONObject(retstring);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse.optJSONArray("member_records");
                if(jsonMainNode!=null)
                {
                    for (int i = 0; i < jsonMainNode.length(); i++) {

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        id = jsonChildNode.optString("id");
                        member_id = jsonChildNode.optString("member_id");
                        Description = jsonChildNode.optString("description");
                        Title =  jsonChildNode.optString("title");
                        Date =  jsonChildNode.optString("date");
                        Tag = jsonChildNode.optString("tagname");
                        AttachmentPath = jsonChildNode.optString("filename");
                        fname = jsonChildNode.optString("fname");
                        lname = jsonChildNode.optString("lname");
                        record r = new record();
                        r.setdescription(Description);
                        r.setattachment_path(AttachmentPath);
                        r.setid(id);
                        r.setrecord_date(Date);
                        r.settag(Tag);
                        r.settitle(Title);
                        r.setmember_id(member_id);
                        r.setFname(fname);
                        r.setLname(lname);
                        arrrecords.add(r);
                        flag=1;
                    }

                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        btnNew.setVisibility(View.VISIBLE);
                        recordList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        btnNew.setVisibility(View.GONE);
                        recordList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    btnNew.setVisibility(View.VISIBLE);
                    recordList.setVisibility(View.GONE);
                }
                rad = new RecordAdapter(RecordsList.this,
                        arrrecords);

                recordList.setAdapter(rad);
                recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView txtId = (TextView) arg1.findViewById(R.id.txtId);
                        TextView txtMemID = (TextView) arg1.findViewById(R.id.txtMemID);
                        TextView txtDescription = (TextView) arg1.findViewById(R.id.txtDescription);
                        TextView txtDate = (TextView) arg1.findViewById(R.id.txtDate);
                        TextView txtTitle = (TextView) arg1.findViewById(R.id.txtTitle);
                        TextView txtTag = (TextView) arg1.findViewById(R.id.txtTag);
                        TextView txtFname = (TextView) arg1.findViewById(R.id.txtFname);
                        TextView txtLname = (TextView) arg1.findViewById(R.id.txtLname);

                        final String recId = txtId.getText().toString();
                        final String recMemid = txtMemID.getText().toString();
                        final String recDesc = txtDescription.getText().toString();
                        final String recDate = txtDate.getText().toString();
                        final String recTitle = txtTitle.getText().toString();
                        final String recTag = txtTag.getText().toString();
                        final String recFname = txtFname.getText().toString();
                        final String recLname = txtLname.getText().toString();
                        final Dialog builder = new Dialog(RecordsList.this);

                        Intent i = new Intent(RecordsList.this, Record_Details.class);
                        i.putExtra("record_id", recId);
                        i.putExtra("member_id", recMemid);
                        i.putExtra("record_desc", recDesc);
                        i.putExtra("record_date", recDate);
                        i.putExtra("record_title", recTitle);
                        i.putExtra("record_tag", recTag);
                        i.putExtra("member_fname", recFname);
                        i.putExtra("member_lname", recLname);
                        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);
                        builder.show();
                        RecordsList.this.startActivity(i);
                    }

                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	public void new_member(View v)
	{
		try {
			Intent intent = new Intent(RecordsList.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			RecordsList.this.startActivity(intent);
			//finish();
		} catch (Exception ex) {

		}
	}
	
	@Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(RecordsList.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                RecordsList.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(RecordsList.this, RecordsList.class);
                RecordsList.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(RecordsList.this, help.class);
                RecordsList.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(RecordsList.this, PreMemberHome.class);
                RecordsList.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(RecordsList.this, ShareMainActivity.class);
                RecordsList.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(RecordsList.this, SearchMainActivity.class);
                RecordsList.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(RecordsList.this,PreMemberHome.class);
		this.startActivity(i);
		finish();

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
    imgAdd.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(RecordsList.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			RecordsList.this.startActivity(intent);
            
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
			Intent i = new Intent(RecordsList.this,PreMemberHome.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
		}
	});
	txtLogoName.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(RecordsList.this,PreMemberHome.class);
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
                SharedPreferences myPrefs = RecordsList.this
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
                SharedPreferences myPrefs = RecordsList.this
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

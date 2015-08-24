package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.ospinet.app.R;

public class Member_Home extends Activity implements ISideNavigationCallback {
	ListView memberList;
	ProgressDialog dialog;
	ArrayList<member> arrMember;
	TextView txtNoRec;
	private SideNavigationView sideNavigationView;

	Button btnNew;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.member);
		showActionBar();
        new GetNotificationsCount().execute();
        new GetFriendRequestCount().execute();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

		dialog = new ProgressDialog(Member_Home.this);
		memberList = (ListView) findViewById(R.id.memberList);
		txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
		btnNew= (Button) findViewById(R.id.btnNewAdd);
		txtNoRec.setVisibility(View.GONE);
		btnNew.setVisibility(View.GONE);
		arrMember = new ArrayList<member>();
		new GetMembers().execute();
	}

	public void AddMember(View v) {
		try {
			Intent intent = new Intent(Member_Home.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Member_Home.this.startActivity(intent);
			//finish();
		} catch (Exception ex) {

		}
	}

	public class GetMembers extends AsyncTask<String, String, String> {

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

				SharedPreferences myPrefs = Member_Home.this
						.getSharedPreferences("remember", MODE_PRIVATE);
				String user_id = myPrefs.getString("userid", null);
				String response = CustomHttpClient
						.executeHttpGet("http://ospinet.com/app_ws/android_app_fun/get_members?user_id="
								+ user_id);
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

				JSONArray jsonMainNode = jsonResponse.optJSONArray("members");
				int flag=0;
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String id = jsonChildNode.optString("id");
					String name = jsonChildNode.optString("name");
					String gender = jsonChildNode.optString("gender");
					String age = jsonChildNode.optString("age");
					String birth_info = jsonChildNode.optString("birth_info");
					String birth_day = jsonChildNode.optString("birth_day");
					String birth_month = jsonChildNode.optString("birth_month");
					String birth_year = jsonChildNode.optString("birth_year");
					String email = jsonChildNode.optString("email");
					String profile_pic = jsonChildNode.optString("profile_pic");
					String type = jsonChildNode.optString("type");
					member m = new member();

					m.setMemberId(Integer.parseInt(id));
					m.setAge(age);
					m.setBirth_Day(birth_day);
					m.setBirth_Info(birth_info);
					m.setBirth_Month(birth_month);
					m.setBirth_Year(birth_year);
					m.setGender(gender);
					m.setMemberName(name);
					m.setEmail(email);
					m.setProfile_Pic(profile_pic);
					m.setType(type);
					arrMember.add(m);
					flag=1;
				}
				if(flag==0)
				{
					txtNoRec.setVisibility(View.VISIBLE);
					btnNew.setVisibility(View.VISIBLE);
					memberList.setVisibility(View.GONE);
				}
				else
				{
					txtNoRec.setVisibility(View.GONE);
					btnNew.setVisibility(View.GONE);
					memberList.setVisibility(View.VISIBLE);
				}
				MemberAdapter mad = new MemberAdapter(Member_Home.this,
						arrMember);

				memberList.setAdapter(mad);
				memberList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						TextView txtId = (TextView) arg1
								.findViewById(R.id.txtId);
						final String memId = txtId.getText().toString();
						
						final Dialog builder = new Dialog(Member_Home.this);

						final View view = Member_Home.this.getLayoutInflater().inflate(
								R.layout.member_options, null);
						builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
						builder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
						builder.setContentView(view);
						Button btnDelete = (Button) builder.findViewById(R.id.btn_Delete);
						Button btnEdit = (Button) builder.findViewById(R.id.btn_Edit);
						Button btnView = (Button) builder.findViewById(R.id.btn_view);
						
						builder.show();
						
						
						
						btnDelete.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								AlertDialog.Builder deleteConfirmDialog = new AlertDialog.Builder(
										Member_Home.this);
								deleteConfirmDialog
										.setTitle("Delete Member");
								// myAlertDialog.setMessage("Please enter new password");
								deleteConfirmDialog.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface arg0,
													int arg1) {
												new DeleteMember().execute(memId);
											}
										});
								deleteConfirmDialog.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface arg0,
													int arg1) {

											}
										});
								deleteConfirmDialog.show();

							}

						});
						btnEdit.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(Member_Home.this, Member_Edit.class);
								i.putExtra("member_id", memId);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("EXIT", true);
								
								Member_Home.this.startActivity(i);
				
							}
						});
						btnView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								/*Intent i = new Intent(Member_Home.this, Member_View.class);
								i.putExtra("member_id", memId);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("EXIT", true);
								
								Member_Home.this.startActivity(i);
				*/
								Intent i = new Intent(Member_Home.this, Records_Home.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("EXIT", true);
								
								i.putExtra("member_id", memId);
								Member_Home.this.startActivity(i);
				
							}
						});
					/*	btnRecords.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(Member_Home.this, Records_Home.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								i.putExtra("EXIT", true);
								
								i.putExtra("member_id", memId);
								Member_Home.this.startActivity(i);
				
							}
						});
*/					}

				});

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

				SharedPreferences myPrefs = Member_Home.this
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
				Toast.makeText(Member_Home.this, "Member deleted successfully",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(Member_Home.this, Member_Home.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("EXIT", true);
				
				Member_Home.this.startActivity(i);
				
			}

		}
	}

	public void new_member(View v)
	{
		try {
			Intent intent = new Intent(Member_Home.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Member_Home.this.startActivity(intent);
			//finish();
		} catch (Exception ex) {

		}
	}
	
	@Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(Member_Home.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Member_Home.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Member_Home.this, Member_Home.class);
                Member_Home.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Member_Home.this, help.class);
                Member_Home.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(Member_Home.this, PreMemberHome.class);
                Member_Home.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(Member_Home.this, ShareMainActivity.class);
                Member_Home.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(Member_Home.this, SearchMainActivity.class);
                Member_Home.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(Member_Home.this,PreMemberHome.class);
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
	ImageButton search_contacts = (ImageButton) v.findViewById(R.id.search_contacts);
	search_contacts.setVisibility(View.INVISIBLE);
    ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
    ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
	ImageButton imgbell = (ImageButton) v.findViewById(R.id.notifications);
	ImageButton imgfriend = (ImageButton) v.findViewById(R.id.friendrequest);
    imgAdd.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Member_Home.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Member_Home.this.startActivity(intent);
            
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
				Intent intent = new Intent(Member_Home.this, Friend_requests.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				Member_Home.this.startActivity(intent);

			}
		});

	imgbell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Member_Home.this, Notifications_Details.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				Member_Home.this.startActivity(intent);

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
			Intent i = new Intent(Member_Home.this,PreMemberHome.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
		}
	});
	txtLogoName.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Member_Home.this,PreMemberHome.class);
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
                SharedPreferences myPrefs = Member_Home.this
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
                SharedPreferences myPrefs = Member_Home.this
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

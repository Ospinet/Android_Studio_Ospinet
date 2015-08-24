package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class MemberActivity extends Activity implements ISideNavigationCallback {
	String memid;
	EditText edtFname, edtLname, edtEmail, edtAge;
	Button btnAdd, btnCancel,  btnDob, btnAge, btnUnborn;
	ImageView btnMale, btnFemale;
	static ProgressDialog dialogP;
	Spinner spYear, spMonth, spDay;
	String birth_info = "";
	String gender = "";
	String arr2[];
	int res = 0;
	String id, fname, lname, age, gen, email;
	private SideNavigationView sideNavigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.member_edit);
		showActionBar();
        new GetNotificationsCount().execute();
        new GetFriendRequestCount().execute();
	    sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

		memid = getIntent().getStringExtra("member_id");
		dialogP = new ProgressDialog(MemberActivity.this);
		edtFname = (EditText) findViewById(R.id.edtFname);
		edtLname = (EditText) findViewById(R.id.edtlname);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtAge = (EditText) findViewById(R.id.edtAge);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnMale = (ImageView) findViewById(R.id.btnMale);
		btnFemale = (ImageView) findViewById(R.id.btnFemale);
		btnDob = (Button) findViewById(R.id.btnDOB);
		btnAge = (Button) findViewById(R.id.btnAge);
		btnUnborn = (Button) findViewById(R.id.btnUnborn);

		spYear = (Spinner) findViewById(R.id.spinYear);
		spMonth = (Spinner) findViewById(R.id.spinMonth);
		spDay = (Spinner) findViewById(R.id.spinDay);
///////////////////// get member details here
		
		spYear.setVisibility(View.INVISIBLE);
		spMonth.setVisibility(View.INVISIBLE);
		spDay.setVisibility(View.INVISIBLE);
		edtAge.setVisibility(View.INVISIBLE);
		Resources res2 = getResources();
		ArrayAdapter<String> adapYear = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				res2.getStringArray(R.array.spinyr));
		adapYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spMonth.setEnabled(false);
		spDay.setEnabled(false);
		
		spYear.setAdapter(adapYear);
		spYear.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (spYear.getSelectedItemPosition() != 0) {
					spMonth.setEnabled(true);
					res = CalculateLeapYear(spYear.getSelectedItem().toString()); // res
																					// =0
																					// not
																					// leap
																					// year,
																					// res
																					// =
																					// 1
																					// leap
																					// year
					String arr[] = getResources()
							.getStringArray(R.array.spinmon);
					ArrayAdapter<String> adapMonth = new ArrayAdapter<String>(
							MemberActivity.this,
							android.R.layout.simple_spinner_item, arr);
					adapMonth
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spMonth.setAdapter(adapMonth);
					spMonth.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							if (spMonth.getSelectedItemPosition() != 0) {
								spDay.setEnabled(true);
								if (spMonth.getSelectedItemPosition() == 2) {
									if (res == 1) {
										// leap year
										arr2 = getResources().getStringArray(
												R.array.spinfebLeap);
									} else {
										arr2 = getResources().getStringArray(
												R.array.spinfeb);
									}
								} else {
									if (spMonth.getSelectedItemPosition() == 1
											|| spMonth.getSelectedItemPosition() == 3
											|| spMonth.getSelectedItemPosition() == 5
											|| spMonth.getSelectedItemPosition() == 7
											|| spMonth.getSelectedItemPosition() == 8
											|| spMonth.getSelectedItemPosition() == 10
											|| spMonth.getSelectedItemPosition() == 12) {
										arr2 = getResources().getStringArray(
												R.array.spindays);
									} else {
										arr2 = getResources().getStringArray(
												R.array.spindays30);
									}
								}
								ArrayAdapter<String> adapDays = new ArrayAdapter<String>(
										MemberActivity.this,
										android.R.layout.simple_spinner_item, arr2);
								adapDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								spDay.setAdapter(adapDays);

							} else {
								String arr2[] = new String[0];
								ArrayAdapter<String> adapDays2 = new ArrayAdapter<String>(
										MemberActivity.this,
										android.R.layout.simple_spinner_item, arr2);
								adapDays2
										.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								spDay.setAdapter(adapDays2);
								spDay.setEnabled(false);
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
									}

				else {
					String arr[] = new String[0];
					ArrayAdapter<String> adapMonth2 = new ArrayAdapter<String>(
							MemberActivity.this,
							android.R.layout.simple_spinner_item, arr);
					adapMonth2
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spMonth.setAdapter(adapMonth2);
					spMonth.setEnabled(false);
					spDay.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
new GetMemberDetails().execute();
	}
	
	public int CalculateLeapYear(String yr) {
		int res = 0;
		try {
			int numYr = Integer.parseInt(yr);
			if (numYr % 4 == 0 && numYr % 100 == 0 && numYr % 400 == 0) {
				res = 1;
			} else if (numYr % 4 == 0 && numYr % 100 == 0) {
				res = 0;
			} else if (numYr % 4 == 0) {
				res = 1;
			} else {
				res = 0;
			}
		} catch (Exception ex) {

		}
		return res;
	}

	public void update_member(View v) {
		if (edtFname.getText().toString().equals("")) {
			edtFname.requestFocus();
			Toast.makeText(MemberActivity.this, "Please enter First name",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (edtLname.getText().toString().equals("")) {
			edtLname.requestFocus();
			Toast.makeText(MemberActivity.this, "Please enter Last name",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (gender.equals("")) {

			Toast.makeText(MemberActivity.this, "Please select the gender",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (birth_info.equals("")) {
			Toast.makeText(MemberActivity.this, "Please select Dob/Age/Unborn",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (birth_info.equals("age") && edtAge.getText().equals("")) {
			edtAge.requestFocus();
			Toast.makeText(MemberActivity.this, "Please enter Age",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ((birth_info.equals("dob") || birth_info.equals("unborn"))
				&& spYear.getSelectedItemPosition() == 0) {
			spYear.requestFocus();
			Toast.makeText(MemberActivity.this, "Please select year",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ((birth_info.equals("dob") || birth_info.equals("unborn"))
				&& spMonth.getSelectedItemPosition() == 0) {
			spMonth.requestFocus();
			Toast.makeText(MemberActivity.this, "Please select month",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ((birth_info.equals("dob") || birth_info.equals("unborn"))
				&& spDay.getSelectedItemPosition() == 0) {
			spDay.requestFocus();
			Toast.makeText(MemberActivity.this, "Please select day",
					Toast.LENGTH_LONG).show();
			return;
		}
		new UpdateMember().execute();
	}

	public void cancel(View v) {
		// settext = ''
		edtFname.setText("");
		edtLname.setText("");
		edtEmail.setText("");
		edtAge.setText("");

		// set button color = blue
		btnAge.setBackgroundResource(R.drawable.button_custom);
		btnDob.setBackgroundResource(R.drawable.button_custom);
		btnUnborn.setBackgroundResource(R.drawable.button_custom);
		btnMale.setBackgroundResource(R.drawable.maleselector);
		btnFemale.setBackgroundResource(R.drawable.femaleselector);

        btnAge.setTextColor(Color.WHITE);
        btnDob.setTextColor(Color.WHITE);
        btnUnborn.setTextColor(Color.WHITE);
        //btnMale.setTextColor(Color.WHITE);
        //btnFemale.setTextColor(Color.WHITE);

		// reset visibility
		spYear.setVisibility(View.INVISIBLE);
		spMonth.setVisibility(View.INVISIBLE);
		spDay.setVisibility(View.INVISIBLE);
		edtAge.setVisibility(View.INVISIBLE);

		Intent i = new Intent(MemberActivity.this, Member_Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("EXIT", true);
		this.startActivity(i);
		//finish();
		
	}

	public class UpdateMember extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogP.setMessage("Please Wait..");
			dialogP.show();
			dialogP.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String retstring = "";
			try {

				String fname = edtFname.getText().toString();
				String lname = edtLname.getText().toString();
				// String gender = edtGender.getText().toString();
				String email = edtEmail.getText().toString();
				//String age = edtAge.getText().toString();
				// String birth_info = edtBirthInfo.getText().toString();

				String day = null;
				String month = null;
				String year = null;
				String age = null;
				if(spDay.getVisibility() == View.VISIBLE)
				{
					day = spDay.getSelectedItem().toString();
					month = spMonth.getSelectedItemPosition()+"";
					year = spYear.getSelectedItem().toString();
				}
				else
				{
					age = edtAge.getText().toString();
				}
				
				SharedPreferences myPrefs = MemberActivity.this
						.getSharedPreferences("remember", MODE_PRIVATE);
				String userid = myPrefs.getString("userid", null);
				ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
				loginParam.add(new BasicNameValuePair("userid", userid));
				loginParam.add(new BasicNameValuePair("member_id", memid));
				loginParam.add(new BasicNameValuePair("fname", fname));
				loginParam.add(new BasicNameValuePair("lname", lname));
				loginParam.add(new BasicNameValuePair("gender", gender));
				loginParam.add(new BasicNameValuePair("email", email));
				loginParam.add(new BasicNameValuePair("age", age));
				loginParam
						.add(new BasicNameValuePair("birth_info", birth_info));
				loginParam.add(new BasicNameValuePair("birth_day", day));
				loginParam
						.add(new BasicNameValuePair("birth_month", month));
				loginParam
						.add(new BasicNameValuePair("birth_year", year));

				String response = CustomHttpClient.executeHttpPost(
						"http://ospinet.com/app_ws/android_app_fun/add_member",
						loginParam);

				retstring = response.toString();
			} catch (Exception io) {

			}
			return retstring;

		}

		@Override
		protected void onPostExecute(String sJson) {
			if (dialogP.isShowing())
				dialogP.dismiss();
			try {
				JSONObject jsonResponse = new JSONObject(sJson);
				String jsonMainNode = jsonResponse.getString("success");
				if (jsonMainNode.equals("1")) {
					Toast.makeText(MemberActivity.this,
							"Member updated successfully", Toast.LENGTH_LONG)
							.show();
					Intent i = new Intent(MemberActivity.this,
							Member_Home.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("EXIT", true);
					
					MemberActivity.this.startActivity(i);
					finish();
				} else {
					Toast.makeText(MemberActivity.this,
							"Some problem. Please try again.",
							Toast.LENGTH_LONG).show();

				}
			} catch (Exception ex) {

			}
		}
	}

	public void maleClicked(View v) {
		try {
			btnMale.setBackgroundResource(R.drawable.malepressed);
           // btnMale.setTextColor(Color.WHITE);
			btnFemale.setBackgroundResource(R.drawable.femaleselector);
            //btnFemale.setTextColor(Color.WHITE);
			gender = "male";
		} catch (Exception ex) {

		}
	}

	public void femaleClicked(View v) {
		try {
			btnFemale.setBackgroundResource(R.drawable.femalepressed);
         //   btnFemale.setTextColor(Color.WHITE);
			btnMale.setBackgroundResource(R.drawable.maleselector);
         //   btnMale.setTextColor(Color.WHITE);
			gender = "female";
		} catch (Exception ex) {

		}
	}

	public void dobClicked(View v) {
		try {
			btnDob.setBackgroundResource(R.drawable.button_custom_two);
            btnDob.setTextColor(Color.WHITE);
			btnAge.setBackgroundResource(R.drawable.button_custom);
            btnAge.setTextColor(Color.WHITE);
			btnUnborn.setBackgroundResource(R.drawable.button_custom);
            btnUnborn.setTextColor(Color.WHITE);

			spYear.setVisibility(View.VISIBLE);
			spMonth.setVisibility(View.VISIBLE);
			spDay.setVisibility(View.VISIBLE);
			edtAge.setVisibility(View.INVISIBLE);

			birth_info = "dob";

		} catch (Exception ex) {

		}
	}

	public void ageClicked(View v) {
		try {
			btnAge.setBackgroundResource(R.drawable.button_custom_two);
            btnAge.setTextColor(Color.WHITE);
			btnDob.setBackgroundResource(R.drawable.button_custom);
            btnDob.setTextColor(Color.WHITE);
			btnUnborn.setBackgroundResource(R.drawable.button_custom);
            btnUnborn.setTextColor(Color.WHITE);

			spYear.setVisibility(View.INVISIBLE);
			spMonth.setVisibility(View.INVISIBLE);
			spDay.setVisibility(View.INVISIBLE);
			edtAge.setVisibility(View.VISIBLE);

			birth_info = "age";
		} catch (Exception ex) {

		}
	}

	public void unbornClicked(View v) {
		try {
			btnUnborn.setBackgroundResource(R.drawable.button_custom_two);
            btnUnborn.setTextColor(Color.WHITE);
			btnAge.setBackgroundResource(R.drawable.button_custom);
            btnAge.setTextColor(Color.WHITE);
			btnDob.setBackgroundResource(R.drawable.button_custom);
            btnDob.setTextColor(Color.WHITE);

			spYear.setVisibility(View.VISIBLE);
			spMonth.setVisibility(View.VISIBLE);
			spDay.setVisibility(View.VISIBLE);
			edtAge.setVisibility(View.INVISIBLE);
			birth_info = "unborn";
		} catch (Exception ex) {

		}
	}

	public class GetMemberDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogP.setMessage("Please Wait..");
			dialogP.show();
			dialogP.setCancelable(false);
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
			if (dialogP.isShowing())
				dialogP.dismiss();
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
					gen = jsonChildNode.optString("gender");
					age = jsonChildNode.optString("age");
					email = jsonChildNode.optString("email");

					// code to set text and button
					edtFname.setText(fname);
					edtLname.setText(lname);
					edtEmail.setText(email);
					edtAge.setText(age);
					if (gen.toLowerCase().equals("male")) {
						btnMale.setBackgroundResource(R.drawable.malepressed);
                        //btnMale.setTextColor(Color.WHITE);
						btnFemale.setBackgroundResource(R.drawable.femaleselector);
                        //btnFemale.setTextColor(Color.WHITE);
						gender = "male";

					} else {
						btnFemale.setBackgroundResource(R.drawable.femalepressed);
                    //    btnFemale.setTextColor(Color.WHITE);
						btnMale.setBackgroundResource(R.drawable.maleselector);
                      //  btnMale.setTextColor(Color.WHITE);
						gender = "female";
					}
					birth_info = "age";
					btnAge.setBackgroundResource(R.drawable.button_custom_two);
                    btnAge.setTextColor(Color.WHITE);
					edtAge.setVisibility(View.VISIBLE);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(MemberActivity.this, Member_Home.class);
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
        imgAdd.setVisibility(View.INVISIBLE);
        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        ImageButton imgbell = (ImageButton) v.findViewById(R.id.notifications);
        ImageButton imgfriend = (ImageButton) v.findViewById(R.id.friendrequest);
    imgAdd.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MemberActivity.this, MemberActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			MemberActivity.this.startActivity(intent);
            
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
                Intent intent = new Intent(MemberActivity.this, Friend_requests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                MemberActivity.this.startActivity(intent);

            }
        });

        imgbell.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MemberActivity.this, Notifications_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                MemberActivity.this.startActivity(intent);

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
			Intent i = new Intent(MemberActivity.this,PreMemberHome.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(i);
		}
	});
	txtLogoName.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(MemberActivity.this,PreMemberHome.class);
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
                Intent i = new Intent(MemberActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                MemberActivity.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(MemberActivity.this, Member_Home.class);
                MemberActivity.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(MemberActivity.this, help.class);
                MemberActivity.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(MemberActivity.this, PreMemberHome.class);
                MemberActivity.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(MemberActivity.this, ShareMainActivity.class);
                MemberActivity.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(MemberActivity.this, SearchMainActivity.class);
                MemberActivity.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }
    public class GetFriendRequestCount extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub
            String Friend_Request_count = "";
            try {
                ArrayList<NameValuePair> Friend_Request = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = MemberActivity.this
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
                SharedPreferences myPrefs = MemberActivity.this
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


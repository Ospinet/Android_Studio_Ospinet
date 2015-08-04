package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.ospinet.app.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Record_Edit extends Activity implements ISideNavigationCallback {
	String recId;
	static ProgressDialog dialog;
	ArrayList<record> arrRecord;

	EditText edtTitle, edtTags, edtDescription;
	static String picturePath;
	byte[] encodeByte;
	byte[] encodeByte1;
	int flag = 0;

	private final int TAKE_PICTURE = 0;
	private final int RESULT_LOAD_IMAGE = 1;
	Button btnEdit, btnCancel;
	Spinner spYear, spMonth, spDay;
	String birth_info = "";
	String gender = "";
	String arr2[];
	int res = 0;
	String memid = "";
	ImageView imgFile;
	private SideNavigationView sideNavigationView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.record_update);
		showActionBar();
		 sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
	        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
	        sideNavigationView.setMenuClickCallback(this);
	        sideNavigationView.setMode(Mode.LEFT);

		dialog = new ProgressDialog(Record_Edit.this);
		edtTitle = (EditText) findViewById(R.id.edtTitle);
		edtTags = (EditText) findViewById(R.id.edtTags);
		edtDescription = (EditText) findViewById(R.id.edtDescription);
		btnEdit = (Button) findViewById(R.id.btnEdit);
		imgFile = (ImageView) findViewById(R.id.imgFile);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		memid = getIntent().getStringExtra("member_id");
		recId = getIntent().getStringExtra("record_id");

		new GetRecordDetails().execute();
	}

	public class GetRecordDetails extends AsyncTask<String, String, String> {

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
				String id = "";
				String Description = "";
				String Date = "";
				String Title = "";
				String Tag = "";
				String AttachmentPath = "";
				jsonResponse = new JSONObject(retstring);

				JSONArray jsonMainNode = jsonResponse
						.optJSONArray("member_detail");
				for (int i = 0; i < jsonMainNode.length(); i++) {
					if (i != 0) {
						JSONArray jArray = jsonMainNode.getJSONArray(i);
						for (int j = 0; j < jArray.length(); j++) {
							JSONObject jsonChildNode = jArray.getJSONObject(j);
							id = jsonChildNode.optString("id");
							Description = jsonChildNode
									.optString("description");
							Title = jsonChildNode.optString("title");
							Date = jsonChildNode.optString("date");
							Tag = jsonChildNode
									.optString("tagname");
							AttachmentPath = jsonChildNode
									.optString("filename");
							if (id.equals(recId)) {
								edtDescription.setText(Description);
								edtTags.setText(Tag);
								edtTitle.setText(Title);
								AQuery androidAQuery = new AQuery(
										Record_Edit.this);

								androidAQuery.id(imgFile).image(
										"http://www.ospinet.com/media_files/"
												+ AttachmentPath, false, false,
										100, 0);
							}
						}
					} else {
						JSONObject jsonChildNode = jsonMainNode
								.getJSONObject(i);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void Edit_Record(View v) {
		if (edtTitle.getText().toString().equals("")) {
			edtTitle.requestFocus();
			Toast.makeText(Record_Edit.this, "Please enter title",
					Toast.LENGTH_LONG).show();
			return;
		}
		new UpdateRecordAsync().execute();
	}

	public class UpdateRecordAsync extends AsyncTask<String, String, String> {

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

				String title = edtTitle.getText().toString();
				String tagname = edtTags.getText().toString();
				String description = edtDescription.getText().toString();

				ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
				loginParam.add(new BasicNameValuePair("member_id", memid));
				loginParam.add(new BasicNameValuePair("record_id", recId));
				loginParam.add(new BasicNameValuePair("title", title));

				loginParam.add(new BasicNameValuePair("tagname", tagname));
				loginParam.add(new BasicNameValuePair("description",
						description));

				String response = CustomHttpClient.executeHttpPost(
						"http://ospinet.com/app_ws/android_app_fun/add_record",
						loginParam);

				retstring = response.toString();
			} catch (Exception io) {
				String a = "";
			}
			return retstring;

		}

		@Override
		protected void onPostExecute(String sJson) {
			if (dialog.isShowing())
				dialog.dismiss();
			try {
				/*
				 * JSONObject jsonResponse = new JSONObject(sJson); String
				 * jsonMainNode = jsonResponse.getString("success");
				 */
				if (sJson.contains("\"success\":1")) {
					Toast.makeText(Record_Edit.this,
							"Record updated successfully", Toast.LENGTH_LONG)
							.show();
					Intent i = new Intent(Record_Edit.this, Record_Edit.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("member_id", memid);
					i.putExtra("EXIT", true);

					Record_Edit.this.startActivity(i);
					// finish();
				} else {
					Toast.makeText(Record_Edit.this,
							"Some problem. Please try again.",
							Toast.LENGTH_LONG).show();

				}
			} catch (Exception ex) {

			}
		}
	}

	public void cancel(View v) {
		Intent intent = new Intent(Record_Edit.this, Records_Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("member_id", memid);
		intent.putExtra("EXIT", true);
		Record_Edit.this.startActivity(intent);

	}

	private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflator.inflate(R.layout.menu2, null);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowHomeEnabled (false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setCustomView(v);
    ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
    ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
    SearchView search = (SearchView) v.findViewById(R.id.search);
    search.setVisibility(View.INVISIBLE);
    imgAdd.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Record_Edit.this, RecordActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			intent.putExtra("member_id", memid);
			Record_Edit.this.startActivity(intent);
            
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
    		Intent i = new Intent(Record_Edit.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
    txtLogoName.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(Record_Edit.this,PreMemberHome.class);
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
                        Intent i = new Intent(Record_Edit.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("EXIT", true);

                        Record_Edit.this.startActivity(i);

                        break;

                    case R.id.side_navigation_menu_item2:
                        Intent records = new Intent(Record_Edit.this, Member_Home.class);
                        Record_Edit.this.startActivity(records);

                        break;

                    case R.id.side_navigation_menu_item3:
                        Intent help = new Intent(Record_Edit.this, help.class);
                        Record_Edit.this.startActivity(help);

                        break;

                    case R.id.side_navigation_menu_item4:
                        Intent home = new Intent(Record_Edit.this, PreMemberHome.class);
                        Record_Edit.this.startActivity(home);

                        break;

                    case R.id.side_navigation_menu_item5:
                        Intent share = new Intent(Record_Edit.this, ShareMainActivity.class);
                        Record_Edit.this.startActivity(share);

                        break;

                    case R.id.side_navigation_menu_item6:
                        Intent search = new Intent(Record_Edit.this, SearchMainActivity.class);
                        Record_Edit.this.startActivity(search);

                        break;

                    default:
                        return;
                }
                // finish();
            }

			public void onBackPressed() {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(Record_Edit.this, Records_Home.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("member_id", memid);
				intent.putExtra("EXIT", true);
				Record_Edit.this.startActivity(intent);
			}
}

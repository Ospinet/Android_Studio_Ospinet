package com.ospinet.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class Record_View extends Activity implements ISideNavigationCallback {
	String recId, memid;
	ProgressDialog dialog;
	ArrayList<record> arrRecord;
	EditText edtTitle, edtTags, edtDescription;
	Button btnBack;
	ImageView imgFile;
	ImageView img;
	static String path = "";
	private SideNavigationView sideNavigationView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.record_view);
		showActionBar();
		  sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
	        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
	        sideNavigationView.setMenuClickCallback(this);
	        sideNavigationView.setMode(Mode.LEFT);

		recId = getIntent().getStringExtra("record_id");
		memid = getIntent().getStringExtra("member_id");
        System.out.println(recId);
        System.out.println(memid);
		dialog = new ProgressDialog(Record_View.this);
		edtTitle = (EditText) findViewById(R.id.edtTitle);
		edtTags = (EditText) findViewById(R.id.edtTags);
		edtDescription = (EditText) findViewById(R.id.edtDescription);
		btnBack = (Button) findViewById(R.id.btnBack);
		imgFile = (ImageView) findViewById(R.id.imgFile);
		arrRecord = new ArrayList<record>();
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
                System.out.println(retstring);
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
							Description = jsonChildNode.optString("description");
							Title = jsonChildNode.optString("title");
							Date = jsonChildNode.optString("date");
							Tag = jsonChildNode.optString("tagname");
							AttachmentPath = jsonChildNode.optString("filename");
							if (id.equals(recId)) {
								edtDescription.setText(Description);
								edtTags.setText(Tag);
								edtTitle.setText(Title);
								AQuery androidAQuery = new AQuery(
										Record_View.this);

								androidAQuery.id(imgFile).image(
										"http://www.ospinet.com/media_files/"
												+ AttachmentPath, false, false,0, 0);
								path = "http://www.ospinet.com/media_files/"
										+ AttachmentPath;
							}
						}
					} else {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Back_Record(View v) {
		Intent i = new Intent(Record_View.this, Records_Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("member_id", memid);
		i.putExtra("EXIT", true);

		Record_View.this.startActivity(i);
	}

	public void showImage(View v) 
	{
		try 
		{
			final Dialog builder = new Dialog(Record_View.this);
			final View view = getLayoutInflater().inflate(
					R.layout.popupimage, null);
			builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
			builder.setContentView(view);
			ImageView imgClose = (ImageView) view.findViewById(R.id.imgCose);
			ImageView imgDownload = (ImageView) view.findViewById(R.id.imgDownload);
			img = (ImageView) view.findViewById(R.id.img);
			AQuery androidAQuery = new AQuery(
					Record_View.this);

			androidAQuery.id(img).image(
					path, false, false,
					0, 0);

			imgClose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					builder.hide();
				}
			});
			imgDownload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new DownloadImage().execute();
					builder.hide();
				}
			});
			builder.show();
		} 
		catch (Exception ex) 
		{

		}
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
			Intent intent = new Intent(Record_View.this, RecordActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			intent.putExtra("member_id", memid);
			Record_View.this.startActivity(intent);
            
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
    		Intent i = new Intent(Record_View.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
    txtLogoName.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(Record_View.this,PreMemberHome.class);
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
                    Intent i = new Intent(Record_View.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("EXIT", true);

                    Record_View.this.startActivity(i);

                    break;

                case R.id.side_navigation_menu_item2:
                    Intent records = new Intent(Record_View.this, Member_Home.class);
                    Record_View.this.startActivity(records);

                    break;

                case R.id.side_navigation_menu_item3:
                    Intent help = new Intent(Record_View.this, help.class);
                    Record_View.this.startActivity(help);

                    break;

                case R.id.side_navigation_menu_item4:
                    Intent home = new Intent(Record_View.this, PreMemberHome.class);
                    Record_View.this.startActivity(home);

                    break;

                case R.id.side_navigation_menu_item5:
                    Intent share = new Intent(Record_View.this, ShareMainActivity.class);
                    Record_View.this.startActivity(share);

                    break;

                case R.id.side_navigation_menu_item6:
                    Intent search = new Intent(Record_View.this, SearchMainActivity.class);
                    Record_View.this.startActivity(search);

                    break;

                default:
                    return;
            }
            // finish();
        }

		public class DownloadImage extends AsyncTask<String, String, String> {

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
					BitmapDrawable btmpDr = (BitmapDrawable) img.getDrawable();
					Bitmap bmp = btmpDr.getBitmap();

					/*File sdCardDirectory = Environment.getExternalStorageDirectory();*/
					
					    File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Ospinet");
					    sdCardDirectory.mkdirs();
					    Random random = new Random();
					    String imageNameForSDCard = "image_" + String.valueOf(random.nextInt(1000)) + System.currentTimeMillis() + ".jpg";

					    File image = new File(sdCardDirectory, imageNameForSDCard);
					    FileOutputStream outStream;

					    outStream = new FileOutputStream(image);
					    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
					    /* 100 to keep full quality of the image */
					    outStream.flush();
					    outStream.close();

					    //Refreshing SD card
					    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
					
				
				} catch (Exception io) {

				}
				return retstring;

			}

			@Override
			protected void onPostExecute(String retstring) {
				Toast.makeText(Record_View.this, "Image downloaded successfully", Toast.LENGTH_LONG).show();
				
				if (dialog.isShowing())
					dialog.dismiss();
				
				
			}
		}
		public void onBackPressed() {
			// TODO Auto-generated method stub
			Intent i = new Intent(Record_View.this, Records_Home.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("member_id", memid);
			i.putExtra("EXIT", true);

			Record_View.this.startActivity(i);

		}
}

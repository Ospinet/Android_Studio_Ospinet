package com.ospinet.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class Share extends Activity {

	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname,toemail,friends_ids;
    EditText YourEmail,ToEmail;
    Button btnCancel, btnShare;
    public static String user_id;
    public static String FriendsIds;
    ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.share);

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

    public void GetContacts(View v)
    {
        try
        {
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
        catch(Exception ex)
        {

        }
    }
    public class Share_record extends AsyncTask<String, String, String>{
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
}

package com.ospinet.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Record_Details extends Activity {
	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.record_details);

		recId = getIntent().getStringExtra("record_id");
        memfname = getIntent().getStringExtra("member_fname");
        memlname = getIntent().getStringExtra("member_lname");
		memid = getIntent().getStringExtra("member_id");
        recDesc = getIntent().getStringExtra("record_desc");
        recDate = getIntent().getStringExtra("record_date");
        recTitle = getIntent().getStringExtra("record_title");
        recTag = getIntent().getStringExtra("record_tag");

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
	}

    public void onBackPressed() {
        Intent i = new Intent(Record_Details.this, SearchMainActivity.class);
        this.startActivity(i);
        finish();
    }
}

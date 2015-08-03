package com.ospinet.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Friend_requests extends Activity {
	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.friend_request);
	}

    public void onBackPressed() {
        Intent i = new Intent(Friend_requests.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }
}

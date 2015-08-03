package com.ospinet.app;

import java.lang.reflect.Member;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.ospinet.app.R;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
	}
	public void Members(View v)
	{
		Intent i = new Intent(HomeActivity.this,Member_Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("EXIT", true);
		
		this.startActivity(i);
	}
}

package com.ospinet.app;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import com.ospinet.app.R;

public class SplashScreen extends Activity {
	@Override
		protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			/*if (getIntent().getBooleanExtra("EXIT", false)) {
			    finish();
			}*/
			//requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.home_new);

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					startActivity(new Intent(SplashScreen.this,
							login_2.class));
					//finish();
				}
			}, 5000);
		}
	}



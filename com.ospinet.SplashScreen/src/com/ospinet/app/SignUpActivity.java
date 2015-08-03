
package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Dialog;

import com.ospinet.app.R;

public class SignUpActivity extends Activity {
	EditText edtFirstName, edtLastName, edtEmail, edtPassword, edtConfirmEmail, edtConfirmPassword;
	String response;
	ProgressDialog dialog;
	String fname,lname,email,password;
	String loginmsg = "";
	Validation valid =new Validation();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
	edtFirstName = (EditText) findViewById(R.id.edt_signup_firstname);
	edtLastName = (EditText) findViewById(R.id.edt_signup_lastname);
	edtPassword = (EditText) findViewById(R.id.edt_signup_password);
	edtEmail = (EditText) findViewById(R.id.edt_signup_email);
	edtConfirmEmail = (EditText) findViewById(R.id.edt_signup_reemail);
	edtConfirmPassword = (EditText) findViewById(R.id.edt_signup_repassword);
	dialog = new ProgressDialog(SignUpActivity.this);
	
	}

    public void PrivacyPolicy(View v)
    {
        try
        {
            final Dialog builder = new Dialog(SignUpActivity.this);
            final View view = SignUpActivity.this.getLayoutInflater().inflate(
                    R.layout.privacy_policy, null);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.setContentView(view);

            Button btnOK = (Button) builder.findViewById(R.id.btnOK);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    builder.hide();
                }
            });

            builder.show();
        }
        catch(Exception ex)
        {

        }
    }
	
	public void register(View v)
	{
		Validation valid= new Validation();
		if(!edtEmail.getText().toString().equals(edtConfirmEmail.getText().toString()))
		{
			Toast.makeText(SignUpActivity.this, "Confirm Email failed", Toast.LENGTH_LONG).show();
			edtEmail.requestFocus();
			return;
		}
	
		if(!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))
		{
			Toast.makeText(SignUpActivity.this, "Confirm Password failed", Toast.LENGTH_LONG).show();
			edtPassword.requestFocus();
			return;
		}
		if(valid.Is_Valid_Empty(edtFirstName) && valid.Is_Valid_Empty(edtLastName) && valid.Is_Valid_Email(edtEmail) && valid.Is_Valid_Empty(edtPassword) && valid.Is_Valid_Empty(edtConfirmEmail) && valid.Is_Valid_Empty(edtConfirmPassword))
		{ 	
			new Register().execute();
		}
			}
	
	
	public class Register extends AsyncTask<String, String, String> {

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

			try {
				fname=edtFirstName.getText().toString();
				lname=edtLastName.getText().toString();
				email=edtEmail.getText().toString();
				password=edtPassword.getText().toString();
				ArrayList<NameValuePair> signupParam = new ArrayList<NameValuePair>();
				signupParam.add(new BasicNameValuePair("fname", fname));
				signupParam.add(new BasicNameValuePair("lname", lname));
				signupParam.add(new BasicNameValuePair("email", email));
				signupParam.add(new BasicNameValuePair("passwd", password));
				signupParam.add(new BasicNameValuePair("login_status","mailsent"));
				
				response = CustomHttpClient.executeHttpPost(
						"http://ospinet.com/app_ws/android_app_fun/signup",
						signupParam);
				String retstring = response.toString();
				JSONObject jsonResponse = new JSONObject(retstring);
				JSONArray jsonMainNode = jsonResponse.optJSONArray("signupmsg");
				 for (int i = 0; i < jsonMainNode.length(); i++) {
					    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						String msg = jsonChildNode.optString("signupstatus");
							loginmsg = msg;					
					   }

			} catch (Exception io) {

			}
			return null;

		}

		@Override
		protected void onPostExecute(String sJson) {
			if (dialog.isShowing())
				dialog.dismiss();
			Toast.makeText(SignUpActivity.this, loginmsg,
					Toast.LENGTH_LONG).show();
			if(loginmsg.contains("Welcome"))
			{
				Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				SignUpActivity.this.startActivity(intent);
			}
			else if(loginmsg.contains("Email Address already exist"))
			{
				edtEmail.requestFocus();
			}
		}

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(SignUpActivity.this, login_2.class);
	    this.startActivity(i);
	    finish();
	}
}

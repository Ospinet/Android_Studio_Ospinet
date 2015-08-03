package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ospinet.app.R;

public class LoginActivity extends Activity {
    EditText edtUsername, edtPassword;
    String response;
    CheckBox chkRemember;
    ProgressDialog dialog;
    String loginmsg = "";

    Button login;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        edtUsername = (EditText) findViewById(R.id.edt_login_username);
        edtPassword = (EditText) findViewById(R.id.edt_login_password);
        chkRemember = (CheckBox) findViewById(R.id.chk_login_remember);
        login = (Button) findViewById(R.id.btn_login_login);
        dialog = new ProgressDialog(LoginActivity.this);
        SharedPreferences myPrefs = this.getSharedPreferences("remember",
                MODE_WORLD_READABLE);
        boolean isRemember = myPrefs.getBoolean("REMEMBER", false);
        if (!isRemember) {
            // do nothing
            edtUsername.setText("");
            edtPassword.setText("");
            chkRemember.setChecked(false);
        }
        else
        {
            final String email = myPrefs.getString("email", "");
            final String password = myPrefs.getString("password", "");

            edtUsername.setText(email);
            edtPassword.setText(password);
            chkRemember.setChecked(true);
        }

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Validation valid=new Validation();

                if(valid.Is_Valid_Empty(edtUsername) && valid.Is_Valid_Empty(edtPassword))
                {
                    new LoginAsync().execute();

                }else
                {
                    Toast.makeText(getApplicationContext(), "Entered Valid Data",1000).show();

                }
            }
        });

    }



    /*	public void login(View v)
        {

               if(valid.Is_Valid_Empty(edtUsername) && valid.Is_Valid_Empty(edtPassword))
               {

                   new LoginAsync().execute();

               }
               else
               {
                   Toast.makeText(getApplicationContext(), "Enter Values", 1000).show();
               }

        }
    */
    public void signup(View v) {
        try {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("EXIT", true);
            LoginActivity.this.startActivity(intent);
        } catch (Exception ex) {

        }
    }

    public class LoginAsync extends AsyncTask<String, String, String> {

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
                String email = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
                //loginParam.add(new BasicNameValuePair("email", email));
                loginParam.add(new BasicNameValuePair("email", email));
                loginParam.add(new BasicNameValuePair("password", password));
                response = CustomHttpClient.executeHttpPost(
                        "http://ospinet.com/app_ws/android_app_fun/login",
                        loginParam);
				/*response = CustomHttpClient.executeHttpPost(
						"http://www.ospinet.com/andriod_app_fun/login",
						loginParam);*/
                String retstring = response.toString();
                JSONObject jsonResponse = new JSONObject(retstring);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("loginmsg");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String msg = jsonChildNode.optString("loginstatus");
                    String fname = jsonChildNode.optString("fname");
                    String lname = jsonChildNode.optString("lname");
                    String emailId = jsonChildNode.optString("email");
                    String userid = jsonChildNode.optString("userid");
                    String roleid = jsonChildNode.optString("roleid");
                    String profile_pic = jsonChildNode.optString("profile_pic");
                    String type = jsonChildNode.optString("type");
                    String gender = jsonChildNode.optString("gender");
                    String age = jsonChildNode.optString("age");

                    // Log.d("msg",msg);
                    if (msg.equals("1")) {
                        loginmsg = "Invalid username/password";
                    } else if (msg.equals("2")) {
                        SharedPreferences myPrefs = LoginActivity.this
                                .getSharedPreferences("remember", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = myPrefs.edit();
                        prefsEditor.putString("email", edtUsername.getText()
                                .toString());
                        prefsEditor.putString("password", edtPassword.getText()
                                .toString());
                        prefsEditor.putBoolean("REMEMBER",
                                chkRemember.isChecked());
                        prefsEditor.putString("msg", msg);
                        prefsEditor.putString("fname", fname);
                        prefsEditor.putString("lname", lname);
                        prefsEditor.putString("userid", userid);
                        prefsEditor.putString("roleid", roleid);
                        prefsEditor.putString("type", type);
                        prefsEditor.putString("profile_pic", profile_pic);
                        prefsEditor.putString("gender", gender);
                        prefsEditor.putString("age", age);
                        prefsEditor.commit();
                        loginmsg = "Login successful";
                        Intent intent = new Intent(LoginActivity.this, PreMemberHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);//finish();

                    } else if (msg.equals("3")) {
                        loginmsg = "Incorrect username or Password. Please click forgot password link to regenerate password.";
                    } else if (msg.equals("4")) {
                        loginmsg = "Please click on the link sent to the registered email address and complete registration before login.";
                    } else if (msg.equals("5")) {
                        loginmsg = "Set Password";
                    }
                }
            } catch (Exception io) {

                loginmsg = "Check your Internet connection";
            }
            return null;
        }
        @Override
        protected void onPostExecute(String sJson) {
            if (dialog.isShowing())
                dialog.dismiss();
            Toast.makeText(LoginActivity.this, loginmsg, Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void forgotPwd(View v)
    {
        try
        {
            final Dialog builder = new Dialog(LoginActivity.this);

            final View view = LoginActivity.this.getLayoutInflater().inflate(
                    R.layout.forgotpassword, null);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.setContentView(view);
            final EditText edtEmail = (EditText) builder.findViewById(R.id.edtEmail);
            Button btnSubmit = (Button) builder.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Validation val = new Validation();
                    if(val.Is_Valid_Email(edtEmail) && !edtEmail.getText().toString().trim().equals(""))
                    {
                        LoginActivity.this.sendPassword(edtEmail.getText().toString());
                        builder.hide();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Please enter the email address", Toast.LENGTH_LONG).show();
                        builder.show();
                    }
                }
            });
            Button btnCancel = (Button) builder.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new OnClickListener() {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent i = new Intent(LoginActivity.this, login_2.class);
        this.startActivity(i);
        finish();
    }
    public void sendPassword(final String emailID)
    {
        AsyncTask<Void, Void, String> ForgotPasswordTask = new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Please Wait..");
                dialog.show();
                dialog.setCancelable(false);		}

            @Override
            protected String doInBackground(Void... args) {
                // do background work here

                String returnValue = "";
                try
                {
                    ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
                    //loginParam.add(new BasicNameValuePair("email", email));
                    loginParam.add(new BasicNameValuePair("email", emailID));
                    response = CustomHttpClient.executeHttpPost(
                            "http://ospinet.com/app_ws/android_app_fun/forgotpassword_mailsent",
                            loginParam);
					/*response = CustomHttpClient.executeHttpPost(
							"http://www.ospinet.com/andriod_app_fun/login",
							loginParam);*/
                    returnValue = response.toString();


                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return returnValue;

            }

            @Override
            protected void onPostExecute(String aunthenticated) {
                // do UI work here
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(LoginActivity.this, aunthenticated, Toast.LENGTH_LONG)
                        .show();

            }
        };
        ForgotPasswordTask.execute((Void[])null);
    }
}
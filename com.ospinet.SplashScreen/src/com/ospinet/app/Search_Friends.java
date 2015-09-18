package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search_Friends extends Activity implements ISideNavigationCallback {
    public  String to_userid;

    private SideNavigationView sideNavigationView;
    ProgressDialog dialog;
    ListView FriendsList;
    TextView txtNoRec;
    ImageButton Send;
    public static String search_string;
    public static EditText search_txt;

    ArrayList<Friend_search> friend_search;
    public static Search_Friend_Adapter rad;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.search_friends);
        showActionBar();
        FriendsList = (ListView) findViewById(R.id.FriendsList);
        dialog = new ProgressDialog(Search_Friends.this);
        txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
        txtNoRec.setVisibility(View.INVISIBLE);
        search_txt = (EditText) findViewById(R.id.search);

        //super.onCreate(savedInstanceState);
        friend_search = new ArrayList<Friend_search>();
        search_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    search_string = search_txt.getText().toString();
                    new search_friends().execute();
                    return false;
                } else {
                    txtNoRec.setVisibility(View.VISIBLE);
                    return false;
                }
            }
        });

    }

    public class search_friends extends AsyncTask<String, String, String> {

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
            String SearchString = "";
            try {
                ArrayList<NameValuePair> friend_search = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Search_Friends.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                friend_search.add(new BasicNameValuePair("user_id",userId));
                friend_search.add(new BasicNameValuePair("search",search_string));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/search_friends",
                                friend_search);
                SearchString = response.toString();

            } catch (Exception io) {

            }
            return SearchString;

        }
        @Override
        protected void onPostExecute(String SearchString) {
            if (dialog.isShowing())
                dialog.dismiss();
            JSONObject jsonResponse = null;
            try {
                String id="";
                String type = "";
                String login_status ="";
                String profile ="";
                String fname = "";
                String lname = "";
                String ns = "";
                String uid = "";
                String email = "";
                String send_request = "";
                jsonResponse = new JSONObject(SearchString);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse.optJSONArray("result");
                friend_search.clear();
                if(jsonMainNode != null)
                {
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONArray jArray = jsonMainNode.getJSONArray(i);
                        for (int j = 0; j < jArray.length(); j++) {
                            JSONObject jsonChildNode = jArray.getJSONObject(j);

                            id = jsonChildNode.optString("id");
                            type = jsonChildNode.optString("type");
                            profile = jsonChildNode.optString("profile_pic");
                            fname = jsonChildNode.optString("fname");
                            lname = jsonChildNode.optString("lname");
                            login_status = jsonChildNode.optString("login_status");
                            ns = jsonChildNode.optString("ns");
                            send_request = jsonChildNode.optString("ns");
                            uid = jsonChildNode.optString("uid");
                            email = jsonChildNode.optString("email");
                            Friend_search r = new Friend_search();
                            r.settype(type);
                            r.setprofile(profile);
                            r.setid(id);
                            r.setfname(fname);
                            r.setlname(lname);
                            r.setemail(email);
                            r.setlogin_status(login_status);
                            r.setns(ns);
                            r.setsend_request(send_request);
                            r.setuid(uid);
                            friend_search.add(r);
                            flag = 1;
                        }

                        if (flag == 0) {
                            txtNoRec.setVisibility(View.VISIBLE);
                            FriendsList.setVisibility(View.GONE);
                        } else {
                            txtNoRec.setVisibility(View.GONE);
                            FriendsList.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    FriendsList.setVisibility(View.GONE);
                }
                rad = new Search_Friend_Adapter(Search_Friends.this,
                        friend_search);

                FriendsList.setAdapter(rad);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Search_Friends.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }
    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu4, null);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);
        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        TextView txtEdit = (TextView) v.findViewById(R.id.txtEdit);
        txtEdit.setVisibility(View.INVISIBLE);

        txtLogoName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Search_Friends.this, PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        imgMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
                rel.bringChildToFront(sideNavigationView);

            }
        });
    }
    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(Search_Friends.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Search_Friends.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Search_Friends.this, Member_Home.class);
                Search_Friends.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Search_Friends.this, help.class);
                Search_Friends.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(Search_Friends.this, PreMemberHome.class);
                Search_Friends.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(Search_Friends.this, ShareMainActivity.class);
                Search_Friends.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(Search_Friends.this, SearchMainActivity.class);
                Search_Friends.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }



    public class Search_Friend_Adapter extends BaseAdapter {
        private ArrayList<Friend_search> friend_search;
        private LayoutInflater mInflater;
        private Context mContext;

        public Search_Friend_Adapter(Context context, ArrayList<Friend_search> results) {
            friend_search = results;
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return friend_search.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return friend_search.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.search_friends_row, null);

                holder = new ViewHolder();

                holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
                holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
                holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);
                holder.txtType = (TextView) convertView.findViewById(R.id.txtType);
                holder.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
                holder.txtNs = (TextView) convertView.findViewById(R.id.txtNs);
                holder.txtLogin_status = (TextView) convertView.findViewById(R.id.txtLogin_status);
                holder.txtUid = (TextView) convertView.findViewById(R.id.txtUid);
                holder.txtProfile = (RoundedImageView) convertView.findViewById(R.id.imgProfile);
                holder.Send_request =(ImageButton) convertView.findViewById(R.id.Send_request);

                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtId.setText(friend_search.get(position).getid()+"");
            holder.txtFname.setText(friend_search.get(position).getfname());
            holder.txtLname.setText(friend_search.get(position).getlname());
            holder.txtType.setText(friend_search.get(position).gettype());
            holder.txtEmail.setText(friend_search.get(position).getemail());
            holder.txtNs.setText(friend_search.get(position).getns());
            holder.txtLogin_status.setText(friend_search.get(position).getlogin_status());
            holder.txtUid.setText(friend_search.get(position).getuid());
            //  holder.txtProfile.setText(req_notify.get(position).getprofile());
            AQuery androidAQuery = new AQuery(
                    mContext);

            holder.Send_request.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    if(friend_search.get(position).getsend_request() == null || friend_search.get(position).getsend_request().equals("null"))
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                mContext);

                        // set title
                        alertDialogBuilder.setTitle("Confirmation!");
                        alertDialogBuilder.setIcon(R.drawable.applogo);
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("You are sending friend request to " + friend_search.get(position).getfname() + " " + friend_search.get(position).getlname())
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        holder.Send_request.setImageResource(R.drawable.user_ok);
                                        to_userid = friend_search.get(position).getid();
                                        new send_request().execute();
                                    /*notifyDataSetChanged();*/
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                }
            });


            if(friend_search.get(position).getsend_request() == null || friend_search.get(position).getsend_request().equals("null")){
                androidAQuery.id(holder.Send_request).image(R.drawable.add_user);

            }else{
                androidAQuery.id(holder.Send_request).image(R.drawable.user_ok);
             /*   holder.Send_request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                mContext);

                        // set title
                        alertDialogBuilder.setTitle("Information!");
                        alertDialogBuilder.setIcon(R.drawable.applogo);
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("You already send friend request to " + friend_search.get(position).getfname() + " " + friend_search.get(position).getlname() + " and it is pending")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                });
    */
            }


            if(friend_search.get(position).getprofile() == null || friend_search.get(position).getprofile().equals("null") || friend_search.get(position).getprofile().equals("")){
                androidAQuery.id(holder.txtProfile).image(
                        "http://ospinet.com/assets/images/people/250/default_avatar_250x250.png", false, false,0, 0);   //"http://ospinet.com/assets/images/people/250/default_avatar_250x250.png"
            }else{
                androidAQuery.id(holder.txtProfile).image(
                        "http://ospinet.com/profile_pic/member_pic_250/" + friend_search.get(position).getprofile() +"_250." + friend_search.get(position).gettype(), false, false,0, 0);   //"http://ospinet.com/profile_pic/member_pic_250/" + profile_image;
            }
            return convertView;
        }
        class ViewHolder {
            TextView txtId;
            TextView txtFname;
            TextView txtLname;
            TextView txtType;
            TextView txtEmail;
            TextView txtNs;
            TextView txtLogin_status;
            TextView txtUid;
            ImageButton Send_request;
            RoundedImageView txtProfile;
        }
    }

    public class send_request extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String SendRequest = "";
            try {
                ArrayList<NameValuePair> send = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Search_Friends.this.getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                send.add(new BasicNameValuePair("user_id",userId));
                send.add(new BasicNameValuePair("to_userid",to_userid));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/send_friend_request",
                                send);
                SendRequest = response.toString();
            } catch (Exception io) {

            }
            return SendRequest;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            new search_friends().execute();

        }
    }

}


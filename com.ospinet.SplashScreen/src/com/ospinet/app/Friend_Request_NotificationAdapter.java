package com.ospinet.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import com.androidquery.AQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Friend_Request_NotificationAdapter extends BaseAdapter {
    private static ArrayList<Friend_request_notification> req_notify;
    private LayoutInflater mInflater;
    private Context mContext;
    public static  String userid;
    public static Button btnConfirm;
    public static Button btnIgnore;

    public Friend_Request_NotificationAdapter(Context context, ArrayList<Friend_request_notification> results) {
        req_notify = results;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return req_notify.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return req_notify.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.friend_request_row, null);

            holder = new ViewHolder();


            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
            holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);
            holder.txtType = (TextView) convertView.findViewById(R.id.txtType);
            holder.txtUser_id = (TextView) convertView.findViewById(R.id.txtUser_id);
            holder.txtProfile = (RoundedImageView) convertView.findViewById(R.id.imgProfile);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtId.setText(req_notify.get(position).getid()+"");
        holder.txtFname.setText(req_notify.get(position).getfname());
        holder.txtLname.setText(req_notify.get(position).getlname());
        holder.txtType.setText(req_notify.get(position).gettype());
        holder.txtUser_id.setText(req_notify.get(position).getuser_id());
      //  holder.txtProfile.setText(req_notify.get(position).getprofile());
        AQuery androidAQuery = new AQuery(
                mContext);

        Button btnConfirm = (Button) convertView.findViewById(R.id.btnConfirm);
        Button btnIgnore = (Button) convertView.findViewById(R.id.btnIgnore);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userid = req_notify.get(position).getuser_id();
                    new accept_request().execute();
                }
            });

        btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ignore_request().execute();
            }
        });

        if(req_notify.get(position).getprofile() == null || req_notify.get(position).getprofile().equals("null") || req_notify.get(position).getprofile().equals("")){
            androidAQuery.id(holder.txtProfile).image(
                    "http://ospinet.com/assets/images/people/250/default_avatar_250x250.png", false, false,0, 0);   //"http://ospinet.com/assets/images/people/250/default_avatar_250x250.png"
        }else{
            androidAQuery.id(holder.txtProfile).image(
                    "http://ospinet.com/profile_pic/member_pic_250/" + req_notify.get(position).getprofile() +"_250." + req_notify.get(position).gettype(), false, false,0, 0);   //"http://ospinet.com/profile_pic/member_pic_250/" + profile_image;
        }
        return convertView;

    }
    static class ViewHolder {
        TextView txtId;
        TextView txtFname;
        TextView txtLname;
        TextView txtType;
        TextView txtUser_id;
        RoundedImageView txtProfile;

    }
    public class accept_request extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String AcceptRequest = "";
            try {
                ArrayList<NameValuePair> accept = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = mContext.getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                accept.add(new BasicNameValuePair("user_id",userId));
                accept.add(new BasicNameValuePair("request_id",userid));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/accept_request",
                                accept);
                AcceptRequest = response.toString();
            } catch (Exception io) {

            }
            return AcceptRequest;
        }
    }

    public class ignore_request extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String IgnoreRequest = "";
            try {
                ArrayList<NameValuePair> ignore = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = mContext.getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                ignore.add(new BasicNameValuePair("user_id",userId));
                ignore.add(new BasicNameValuePair("request_id",userid));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/ignore_request",
                                ignore);
                IgnoreRequest = response.toString();
            } catch (Exception io) {

            }
            return IgnoreRequest;
        }
    }
}
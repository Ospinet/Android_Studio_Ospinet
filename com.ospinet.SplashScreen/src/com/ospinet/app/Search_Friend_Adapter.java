package com.ospinet.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class Search_Friend_Adapter extends BaseAdapter {
    private static ArrayList<Friend_search> friend_search;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
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

        String to_userid = friend_search.get(position).getid();
        if(friend_search.get(position).getsend_request() == null || friend_search.get(position).getsend_request().equals("null")){
            androidAQuery.id(holder.Send_request).image(R.drawable.add_user);
            holder.Send_request.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    //     new send_request().execute();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);

                    // set title
                    alertDialogBuilder.setTitle("Ospinet Confirmation!");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click Yes, if you are sure to send request.")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
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
            });


        }else{
            androidAQuery.id(holder.Send_request).image(R.drawable.user_ok);
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
    static class ViewHolder {
        TextView txtId;
        TextView txtFname;
        TextView txtLname;
        TextView txtType;
        TextView txtEmail;
        TextView txtNs;
        TextView txtLogin_status;
        TextView txtUid;
        ImageButton Send_request;
        String to_userid;
        RoundedImageView txtProfile;
    }
    public class send_request extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String SearchString = "";
            try {
                ArrayList<NameValuePair> send = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Search_Friend_Adapter.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                send.add(new BasicNameValuePair("user_id",userId));
                send.add(new BasicNameValuePair("to_userid",to_userid));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/send_friend_request",
                                send);
                SearchString = response.toString();
            } catch (Exception io) {

            }
            return SearchString;
        }
    }

}
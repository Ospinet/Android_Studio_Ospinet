package com.ospinet.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;

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
        RoundedImageView txtProfile;

    }

}
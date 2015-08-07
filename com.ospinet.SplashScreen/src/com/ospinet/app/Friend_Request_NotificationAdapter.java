package com.ospinet.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Friend_Request_NotificationAdapter extends BaseAdapter {
    private static ArrayList<Friend_request_notification> req_notify;
    private LayoutInflater mInflater;
    private Context mContext;

    public Friend_Request_NotificationAdapter(Context context, ArrayList<Friend_request_notification> results) {
        req_notify = results;
        mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
            holder.txtProfile = (TextView) convertView.findViewById(R.id.imgProfile);

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
        holder.txtProfile.setText(req_notify.get(position).getprofile());
        return convertView;

    }
    static class ViewHolder {
        TextView txtId;
        TextView txtFname;
        TextView txtLname;
        TextView txtType;
        TextView txtUser_id;
        TextView txtProfile;

    }

}
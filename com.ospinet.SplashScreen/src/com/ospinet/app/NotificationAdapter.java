package com.ospinet.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {
    private static ArrayList<notification> arrnotify;
    private LayoutInflater mInflater;
    private Context mContext;

    public NotificationAdapter(Context context, ArrayList<notification> results) {
        arrnotify = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrnotify.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrnotify.get(position);
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
            convertView = mInflater.inflate(R.layout.notification_row, null);

            holder = new ViewHolder();


            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
            holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);
            holder.txtType_id = (TextView) convertView.findViewById(R.id.txtType_id);
            holder.txtFrom_user_id = (TextView) convertView.findViewById(R.id.txtFrom_user_id);
            holder.txtMember_record_id = (TextView) convertView.findViewById(R.id.txtMember_record_id);
            holder.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtId.setText(arrnotify.get(position).getid()+"");
        holder.txtFname.setText(arrnotify.get(position).getfname());
        holder.txtLname.setText(arrnotify.get(position).getlname());
        holder.txtType_id.setText(arrnotify.get(position).gettype_id());
        holder.txtFrom_user_id.setText(arrnotify.get(position).getfrom_user_id());
        holder.txtMember_record_id.setText(arrnotify.get(position).getmember_record_id());
        holder.txtEmail.setText(arrnotify.get(position).getemail());
        return convertView;

    }
    static class ViewHolder {
        TextView txtId;
        TextView txtFname;
        TextView txtLname;
        TextView txtType_id;
        TextView txtFrom_user_id;
        TextView txtMember_record_id;
        TextView txtEmail;

    }

}
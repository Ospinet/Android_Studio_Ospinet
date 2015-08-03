package com.ospinet.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    private static ArrayList<contact> contactArrayList;
    private LayoutInflater mInflater;
    private Context mContext;

    public ContactAdapter(Context context, ArrayList<contact> results) {
        contactArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contactArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return contactArrayList.get(position);
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
            convertView = mInflater.inflate(R.layout.contact_row, null);

            holder = new ViewHolder();

            holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
            holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);
            holder.txtFriendId = (TextView) convertView.findViewById(R.id.txtFriendId);
            holder.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFname.setText(contactArrayList.get(position).getFname());
        holder.txtLname.setText(contactArrayList.get(position).getLname());
        holder.txtFriendId.setText(contactArrayList.get(position).getfriend_id());
        holder.txtEmail.setText(contactArrayList.get(position).getemail());
        return convertView;

    }
    static class ViewHolder {
        TextView txtFname;
        TextView txtLname;
        TextView txtFriendId;
        TextView txtEmail;
    }

}
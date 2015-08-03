package com.ospinet.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FilesAdapter extends BaseAdapter {
    private static ArrayList<file> filesArrayList;
    private LayoutInflater mInflater;
    private Context mContext;

    public FilesAdapter(Context context, ArrayList<file> results) {
        filesArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return filesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return filesArrayList.get(position);
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
            convertView = mInflater.inflate(R.layout.file_row, null);

            holder = new ViewHolder();

            holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
            holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);
            holder.txtMember_id = (TextView) convertView.findViewById(R.id.txtMember_id);
            holder.txtFilename = (TextView) convertView.findViewById(R.id.txtFilename);
            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtTagId = (TextView) convertView.findViewById(R.id.txtTagId);
            holder.txtTagName = (TextView) convertView.findViewById(R.id.txtTagName);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtFile_id = (TextView) convertView.findViewById(R.id.txtFile_id);
            holder.txtFile_name = (TextView) convertView.findViewById(R.id.txtFile_name);
            holder.txtFile_member_id = (TextView) convertView.findViewById(R.id.txtFile_Member_id);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFname.setText(filesArrayList.get(position).getFname());
        holder.txtLname.setText(filesArrayList.get(position).getLname());
        holder.txtMember_id.setText(filesArrayList.get(position).getMember_id());
        holder.txtFilename.setText(filesArrayList.get(position).getFilename());
        holder.txtId.setText(filesArrayList.get(position).getId());
        holder.txtTitle.setText(filesArrayList.get(position).getTitle());
        holder.txtDescription.setText(filesArrayList.get(position).getDescription());
        holder.txtTagName.setText(filesArrayList.get(position).getTagname());
        holder.txtTagId.setText(filesArrayList.get(position).getTagid());
        holder.txtDate.setText(filesArrayList.get(position).getDate());
        holder.txtFile_id.setText(filesArrayList.get(position).getFile_id());
        holder.txtFile_name.setText(filesArrayList.get(position).getFile_name());
        holder.txtFile_member_id.setText(filesArrayList.get(position).getFile_member_id());
        return convertView;

    }
    static class ViewHolder {
        TextView txtFname;
        TextView txtLname;
        TextView txtFilename;
        TextView txtId;
        TextView txtMember_id;
        TextView txtTitle;
        TextView txtDescription;
        TextView txtDate;
        TextView txtTagId;
        TextView txtTagName;
        TextView txtFile_name;
        TextView txtFile_id;
        TextView txtFile_member_id;
        
    }

}
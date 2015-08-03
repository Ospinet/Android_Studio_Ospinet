package com.ospinet.app;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ospinet.app.R;

public class RecordAdapter extends BaseAdapter {
    private static ArrayList<record> recordArrayList;
    private LayoutInflater mInflater;
    private Context mContext;

    public RecordAdapter(Context context, ArrayList<record> results) {
        recordArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return recordArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return recordArrayList.get(position);
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
            convertView = mInflater.inflate(R.layout.record_row, null);

            holder = new ViewHolder();


            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtTag = (TextView) convertView.findViewById(R.id.txtTag);
            holder.txtAttachmentPath = (TextView) convertView.findViewById(R.id.txtAttachmentPath);
            holder.txtMemID = (TextView) convertView.findViewById(R.id.txtMemID);
            holder.txtFname = (TextView) convertView.findViewById(R.id.txtFname);
            holder.txtLname = (TextView) convertView.findViewById(R.id.txtLname);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtId.setText(recordArrayList.get(position).getid()+"");
        holder.txtDescription.setText(recordArrayList.get(position).getdescription());
        holder.txtDate.setText(recordArrayList.get(position).getrecord_date());
        holder.txtTitle.setText(recordArrayList.get(position).gettitle());
        holder.txtTag.setText(recordArrayList.get(position).gettag());
        holder.txtAttachmentPath.setText(recordArrayList.get(position).getattachment_path());
        holder.txtMemID.setText(recordArrayList.get(position).getmember_id());
        holder.txtFname.setText(recordArrayList.get(position).getFname());
        holder.txtLname.setText(recordArrayList.get(position).getLname());
        return convertView;

    }
    static class ViewHolder {
        TextView txtId;
        TextView txtDescription;
        TextView txtDate;
        TextView txtTitle;
        TextView txtTag;
        TextView txtAttachmentPath;
        TextView txtMemID;
        TextView txtFname;
        TextView txtLname;
    }

}
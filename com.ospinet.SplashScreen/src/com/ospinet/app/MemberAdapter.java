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

public class MemberAdapter extends BaseAdapter {
	private static ArrayList<member> searchArrayList;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public MemberAdapter(Context context, ArrayList<member> results) {
		  searchArrayList = results;
		  mInflater = LayoutInflater.from(context);
		 }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchArrayList.get(position);
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
				  convertView = mInflater.inflate(R.layout.member_row, null);
			
		   holder = new ViewHolder();
		   
		   
		  holder.lstmemberid = (TextView) convertView.findViewById(R.id.txtId);
		  holder.lstmembername = (TextView) convertView.findViewById(R.id.txtName);
		  holder.age = (TextView) convertView.findViewById(R.id.txtAge);
		  holder.email = (TextView) convertView.findViewById(R.id.txtEmail);
		  holder.gender = (TextView) convertView.findViewById(R.id.txtGender);
		  holder.birth_day = (TextView) convertView.findViewById(R.id.txtBirthDay);
		  holder.birth_info = (TextView) convertView.findViewById(R.id.txtBirthInfo);
		  holder.birth_month = (TextView) convertView.findViewById(R.id.txtBirthMonth);
		  holder.birth_year = (TextView) convertView.findViewById(R.id.txtBirthYear);
		  holder.profile_pic = (TextView) convertView.findViewById(R.id.txtProfilePic);
		   
		   
		   convertView.setTag(holder);
		  } else 
		  {
			  holder = (ViewHolder) convertView.getTag();
		  }
			
		  
		  holder.lstmemberid.setText(searchArrayList.get(position).getMemberId()+"");
		  holder.lstmembername.setText(searchArrayList.get(position).getMemberName());
		  holder.age.setText(searchArrayList.get(position).getAge());
		  holder.email.setText(searchArrayList.get(position).getEmail());
		  holder.gender.setText(searchArrayList.get(position).getGender());
		  holder.birth_day.setText(searchArrayList.get(position).getBirth_day());
		  holder.birth_info.setText(searchArrayList.get(position).getBirth_Info());
		  holder.birth_month.setText(searchArrayList.get(position).getBirth_Month());
		  holder.birth_year.setText(searchArrayList.get(position).getBirth_Year());
		  holder.profile_pic.setText(searchArrayList.get(position).getProfile_Pic());
		  
		  return convertView;

	}
	static class ViewHolder {
	TextView lstmemberid;
	TextView lstmembername;
	TextView age;
	TextView gender;
	TextView email;
	TextView birth_info;
	TextView profile_pic;
	TextView birth_day;
	TextView birth_month;
	TextView birth_year;
	}

}

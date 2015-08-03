package com.ospinet.app;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlphabetListAdapter extends BaseAdapter {
	int rowPos;
    public static abstract class Row {}

    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }

    public static final class Item extends Row {
        public final String text;
        public final String id;

        public Item(String text,String id) {
            this.text = text;
            this.id=id;
        }
    }

    private List<Row> rows;

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Row getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        
        if (getItemViewType(position) == 0) { // Item
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_item, parent, false);
            }
            if(position==0)
            {
            	rowPos=-2;
            }
            else if(position== 1)
            {
            	rowPos =-1;
            }
            rowPos = rowPos+1;
            Item item = (Item) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(item.text);
            TextView textView2 = (TextView) view.findViewById(R.id.textView2);
            textView2.setText(item.id);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            final int finalPos = rowPos;
            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Log.e("Pos",finalPos+"");
					if(isChecked)
					{
						
						ContactsMainActivity.checkedContacts[finalPos] =true;
					}
					else
					{
						ContactsMainActivity.checkedContacts[finalPos] =false;
						
					}
				}

              
            });

        } else { // Section
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_section, parent, false);
            }

            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setText(section.text);
        }
        return view;
    }

}

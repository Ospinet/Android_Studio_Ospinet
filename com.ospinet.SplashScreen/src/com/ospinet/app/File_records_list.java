package com.ospinet.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.devspark.sidenavigation.SideNavigationView;

import java.util.ArrayList;

public class File_records_list extends Activity {
	String fileId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname;
    ArrayList<record> arrfilerecords;
    String response = "";
    private SearchView search;
    private SideNavigationView sideNavigationView;
    ArrayList<record> arrrecords;
    public static RecordAdapter rad;
    ProgressDialog dialog;
    ListView recordList;
    TextView txtNoRec;
    Button btnNew;

    public static String strQuery;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.records);
        SharedPreferences myPrefs = File_records_list.this
                .getSharedPreferences("remember", Context.MODE_PRIVATE);
        String fileId = myPrefs.getString("file_id", null);
        String response = myPrefs.getString("file_response", null);

	}

    public void onBackPressed() {
        Intent i = new Intent(File_records_list.this, FilesFragment.class);
        this.startActivity(i);
        finish();
    }
}

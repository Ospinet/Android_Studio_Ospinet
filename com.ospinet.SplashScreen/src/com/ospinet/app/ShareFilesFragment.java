package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShareFilesFragment extends Fragment implements ISideNavigationCallback {
    private SearchView search;
    private SideNavigationView sideNavigationView;
    String memid;
    ArrayList<file> arrfiles;
    public static ShareFilesAdapter rad;
    ProgressDialog dialog;
    ListView filesList;
    TextView txtNoRec,edit;
    Button btnNew;
    public static boolean isVisible = false;
    View rootView;
    public static String file_id="";

    public static Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.files, container, false);
       
        sideNavigationView = (SideNavigationView) rootView.findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);
        dialog = new ProgressDialog(getActivity());
        //memid = getActivity().getIntent().getStringExtra("member_id");
        txtNoRec = (TextView) rootView.findViewById(R.id.txt_home_norec);
        btnNew= (Button) rootView.findViewById(R.id.btnNewAdd);
        txtNoRec.setVisibility(View.GONE);
        btnNew.setVisibility(View.GONE);
        filesList = (ListView)rootView.findViewById(R.id.filesList);
        arrfiles = new ArrayList<file>();
        new Loadfiles().execute();
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//      super.setUserVisibleHint(isVisibleToUser);
      if (isVisibleToUser) { 
    	  isVisible = isVisibleToUser;
    	  if(getActivity()!=null)
    	  showActionBar();
      }
      else { 
    	  if(sideNavigationView!=null)
    	  sideNavigationView.hideMenu();
      }
    }
    public class Loadfiles extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String retstring = "";
            try {
                ArrayList<NameValuePair> allfiles = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = getActivity()
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                allfiles.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_all_file_details",
                                allfiles);
                retstring = response.toString();
            } catch (Exception io) {

            }
            return retstring;

        }

        @Override
        protected void onPostExecute(String retstring) {
            if (dialog.isShowing())
                dialog.dismiss();
            JSONObject jsonResponse = null;
            try {
                String file_name="";
                String file_member_id="";
                String fname ="";
                String lname="";
                String record_id = "";
                String member_id = "";
                String title = "";
                String description = "";
                String date = "";
                String tagid = "";
                String tagname = "";
                String filename = "";

                jsonResponse = new JSONObject(retstring);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse
                        .optJSONArray("result");
                if(jsonMainNode!=null)
                {
                    for (int i = 0; i < jsonMainNode.length(); i++) {

                        JSONArray jsonChildArray = jsonMainNode.getJSONArray(i);
                        if(jsonChildArray != null)
                        {
                            for(int j =0;j<jsonChildArray.length();j++)
                            {
                                JSONObject jsonChildObject = jsonChildArray.getJSONObject(j);
                                if(jsonChildObject!=null)
                                {
                                    file_id = jsonChildObject.optString("id");
                                    file_name = jsonChildObject.optString("file_name");
                                    file_member_id = jsonChildObject.optString("member_id");
                                    fname = jsonChildObject.optString("fname");
                                    lname = jsonChildObject.optString("lname");

                                    file r = new file();
                                    r.setFname(fname);
                                    r.setLname(lname);
                                    r.setMember_id(member_id);
                                    r.setFile_name(file_name);
                                    r.setFile_id(file_id);
                                    r.setFile_member_id(file_member_id);
                                    arrfiles.add(r);
                                    flag=1;
                                }
                            }
                            SharedPreferences myPrefs = getActivity()
                                    .getSharedPreferences("remember", Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = myPrefs.edit();

                            prefsEditor.putString("file_response", retstring);
                            prefsEditor.commit();
/*                    			JSONArray jsonRecordArray = jsonChildObject
                                         .optJSONArray("record");
                    				if(jsonRecordArray!=null)
                    				{
                    					for(int k=0;k<jsonRecordArray.length();k++)
                    					{
                    						JSONArray jsonChildRecordArray = jsonRecordArray.getJSONArray(k);
                    						if(jsonChildRecordArray!=null)
                    						{
                    							for(int m =0;m<jsonChildRecordArray.length();m++)
                    							{
                    								JSONObject recordObj = jsonChildRecordArray.getJSONObject(m);
                                                    record_id = recordObj.optString("id");
                    								 member_id = recordObj.optString("member_id");
                    								 title = recordObj.optString("title");
                    								 description = recordObj.optString("description");
                    								 date = recordObj.optString("date");
                    								 tagid = recordObj.optString("tagid");
                    								 tagname = recordObj.optString("tagname");
                    								 filename = recordObj.optString("filename");
                    								 file r = new file();
                    			                        r.setId(record_id);
                    			                        r.setFname(fname);
                    			                        r.setLname(lname);
                    			                        r.setMember_id(member_id);
                    			                        r.setTitle(title);
                    			                        r.setDescription(description);
                    			                        r.setDate(date);
                    			                        r.setTagid(tagid);
                    			                        r.setTagname(tagname);
                    			                        r.setFilename(filename);
                                                        r.setFile_name(file_name);
                                                        r.setFile_id(file_id);
                                                        r.setFile_member_id(file_member_id);
                    			                        arrfiles.add(r);
                    			                        flag=1;

                    							}
                    								
                    						}
                    					}
                    				}*/


                        }
                    }

                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        btnNew.setVisibility(View.VISIBLE);
                        filesList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        btnNew.setVisibility(View.GONE);
                        filesList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    btnNew.setVisibility(View.VISIBLE);
                    filesList.setVisibility(View.GONE);
                }
                rad = new ShareFilesAdapter(getActivity(),
                        arrfiles);

                filesList.setAdapter(rad);
                filesList.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        TextView txtFile_id = (TextView) view.findViewById(R.id.txtFile_id);
                        SharedPreferences myPrefs = getActivity()
                                .getSharedPreferences("remember", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = myPrefs.edit();

                        prefsEditor.putString("file_id", txtFile_id.getText().toString());
                        prefsEditor.commit();

                        Intent i = new Intent(getActivity(), File_records_list.class);
                        i.putExtra("file_id", file_id);

                        getActivity().startActivity(i);
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu4, null);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        edit=(TextView) v.findViewById(R.id.txtEdit);
        edit.setVisibility(View.INVISIBLE);

        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
                RelativeLayout rel = (RelativeLayout) rootView.findViewById(R.id.rel);
                rel.bringChildToFront(sideNavigationView);
            }
        });

        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);
        txtLogoName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getActivity(),PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

    }

    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                getActivity().startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(getActivity(), Member_Home.class);
                getActivity().startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(getActivity(), help.class);
                getActivity().startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(getActivity(), PreMemberHome.class);
                getActivity().startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(getActivity(), ShareMainActivity.class);
                getActivity().startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(getActivity(), SearchMainActivity.class);
                getActivity().startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }

}
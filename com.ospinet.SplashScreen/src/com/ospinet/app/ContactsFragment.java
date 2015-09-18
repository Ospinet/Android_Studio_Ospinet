package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
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

public class ContactsFragment extends Fragment implements ISideNavigationCallback {
    private SearchView search;
    private SideNavigationView sideNavigationView;
    String memid;
    ArrayList<contact> arrcontacts;
    public static ContactAdapter rad;
    ProgressDialog dialog;
    ListView contactList;
    TextView txtNoRec;
    Button btnNew;

    public static String strQuery;
    public static View rootView;
    public static boolean isVisible = false;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.contacts, container, false);
       // showActionBar();
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
        contactList = (ListView)rootView.findViewById(R.id.contactList);
        arrcontacts = new ArrayList<contact>();
        new Loadcontacts().execute();
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
 

    public class Loadcontacts extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String retstring = "";
            try {
                ArrayList<NameValuePair> allcontacts = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = getActivity()
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                allcontacts.add(new BasicNameValuePair("user_id",userId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_contacts",
                                allcontacts);
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
                String Friend_id="";
                String First_Name = "";
                String Last_Name ="";
                String Email ="";
                jsonResponse = new JSONObject(retstring);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse
                        .optJSONArray("result");
                if(jsonMainNode!=null)
                {
                    for (int i = 0; i < jsonMainNode.length(); i++) {

                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Friend_id = jsonChildNode.optString("friend_id");
                        First_Name = jsonChildNode.optString("fname");
                        Last_Name = jsonChildNode.optString("lname");
                        Email =  jsonChildNode.optString("email");
                        contact r = new contact();
                        r.setfriend_id(Friend_id);
                        r.setFname(First_Name);
                        r.setLname(Last_Name);
                        r.setemail(Email);
                        arrcontacts.add(r);
                        flag=1;
                    }

                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        btnNew.setVisibility(View.VISIBLE);
                        contactList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        btnNew.setVisibility(View.GONE);
                        contactList.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    txtNoRec.setVisibility(View.VISIBLE);
                    btnNew.setVisibility(View.VISIBLE);
                    contactList.setVisibility(View.GONE);
                }
                rad = new ContactAdapter(getActivity(),
                        arrcontacts);

                contactList.setAdapter(rad);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu2, null);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        search=(SearchView) v.findViewById(R.id.search);
        search.setQueryHint("Search");

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                strQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                //	Toast.makeText(getBaseContext(), newText,
                //Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
        imgAdd.setVisibility(View.INVISIBLE);
        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        imgAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                intent.putExtra("member_id", memid);
                getActivity().startActivity(intent);

            }
        });
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
	                RelativeLayout rel = (RelativeLayout) rootView.findViewById(R.id.rel);
	                rel.bringChildToFront(sideNavigationView);

            }
        });
        ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

        imgLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getActivity(),PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
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
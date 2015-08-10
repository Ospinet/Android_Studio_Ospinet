package com.ospinet.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.ospinet.app.AlphabetListAdapter.Item;
import com.ospinet.app.AlphabetListAdapter.Row;
import com.ospinet.app.AlphabetListAdapter.Section;

import android.app.ListActivity;

import android.view.View.OnTouchListener;



public class ContactsMainActivity extends ListActivity {
    String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname,toemail,friends_ids;

    private AlphabetListAdapter adapter = new AlphabetListAdapter();
    private GestureDetector mGestureDetector;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private int indexListSize;
    ProgressDialog dialog;
    ArrayList<contact> arrcontacts;
    Button btnAdd;
    public static boolean[] checkedContacts;
    class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            if (sideIndexX >= 0 && sideIndexY >= 0) {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_alphabet);
        toemail = getIntent().getStringExtra("friends_email_ids");
        friends_ids = getIntent().getStringExtra("friends_ids");
        recId = getIntent().getStringExtra("record_id");
        memfname = getIntent().getStringExtra("member_fname");
        memlname = getIntent().getStringExtra("member_lname");
        memid = getIntent().getStringExtra("member_id");
        recDesc = getIntent().getStringExtra("record_desc");
        recDate = getIntent().getStringExtra("record_date");
        recTitle = getIntent().getStringExtra("record_title");
        recTag = getIntent().getStringExtra("record_tag");

        mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());
        dialog = new ProgressDialog(ContactsMainActivity.this);
        arrcontacts = new ArrayList<contact>();
        new Loadcontacts().execute();
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String selectedEmails="";
                String selectedIds="";
                for(int i=0;i<checkedContacts.length;i++)
                {
                    if(checkedContacts[i])
                    {
                        selectedEmails= selectedEmails + "[ " + arrcontacts.get(i).getemail()+" ], ";
                        selectedIds= selectedIds + arrcontacts.get(i).getfriend_id()+",";
                    }
                }
                Toast.makeText(ContactsMainActivity.this, selectedEmails, Toast.LENGTH_LONG).show();

                Intent i = new Intent(ContactsMainActivity.this, Share.class);
                i.putExtra("record_id", recId);
                i.putExtra("member_id", memid);
                i.putExtra("record_desc", recDesc);
                i.putExtra("record_date", recDate);
                i.putExtra("record_title", recTitle);
                i.putExtra("record_tag", recTag);
                i.putExtra("member_fname", memfname);
                i.putExtra("member_lname", memlname);
                i.putExtra("friends_email_ids", selectedEmails);
                i.putExtra("friends_ids", selectedIds);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                ContactsMainActivity.this.startActivity(i);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            return false;
        }
    }

    public void updateList() {
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndex.removeAllViews();
        indexListSize = alphabet.size();
        if (indexListSize < 1) {
            return;
        }

        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
        int tmpIndexListSize = indexListSize;
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / 2;
        }
        double delta;
        if (tmpIndexListSize > 0) {
            delta = indexListSize / tmpIndexListSize;
        } else {
            delta = 1;
        }

        TextView tmpTV;
        for (double i = 1; i <= indexListSize; i = i + delta) {
            Object[] tmpIndexItem = alphabet.get((int) i - 1);
            String tmpLetter = tmpIndexItem[0].toString();

            tmpTV = new TextView(this);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextSize(15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        sideIndexHeight = sideIndex.getHeight();

        sideIndex.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // now you know coordinates of touch
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                // and can display a proper item it country list
                displayListItem();

                return false;
            }
        });
    }

    public void displayListItem() {
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        if (itemPosition < alphabet.size()) {
            Object[] indexItem = alphabet.get(itemPosition);
            int subitemPosition = sections.get(indexItem[0]);

            //ListView listView = (ListView) findViewById(android.R.id.list);
            getListView().setSelection(subitemPosition);
        }
    }

    public class Loadcontacts extends AsyncTask<String, String, String> {

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
                ArrayList<NameValuePair> allcontacts = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = ContactsMainActivity.this
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
                }
                else
                {
                    Toast.makeText(ContactsMainActivity.this, "No Contact Found", Toast.LENGTH_LONG).show();
                }

                List<String> lstContactName = new ArrayList<String>();
                /*for (contact contact : arrcontacts) {
                    lstContactName.add(contact.getFname() +' '+ contact.getLname()+ '\n'  + contact.getemail()+ '\n'+'\n'  + contact.getfriend_id());
                }

                Collections.sort(lstContactName);
*/
                List<Row> rows = new ArrayList<Row>();
                int start = 0;
                int end = 0;
                String previousLetter = null;
                Object[] tmpIndexItem = null;
                Pattern numberPattern = Pattern.compile("[0-9]");

                for (contact contact : arrcontacts) {
                    String firstLetter = contact.getFname().substring(0, 1);

                    // Group numbers together in the scroller
                    if (numberPattern.matcher(firstLetter).matches()) {
                        firstLetter = "#";
                    }

                    // If we've changed to a new letter, add the previous letter to the alphabet scroller
                    if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                        end = rows.size() - 1;
                        tmpIndexItem = new Object[3];
                        tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
                        tmpIndexItem[1] = start;
                        tmpIndexItem[2] = end;
                        alphabet.add(tmpIndexItem);

                        start = end + 1;
                    }

                    // Check if we need to add a header row
                    if (!firstLetter.equals(previousLetter)) {
                        rows.add(new Section(firstLetter));
                        sections.put(firstLetter, start);
                    }

                    // Add the country to the list
                    rows.add(new Item(contact.getFname() + " " +contact.getLname()+ "\n"  + contact.getemail(),contact.getfriend_id()));
                    previousLetter = firstLetter;
                }

                if (previousLetter != null) {
                    // Save the last letter
                    tmpIndexItem = new Object[3];
                    tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
                    tmpIndexItem[1] = start;
                    tmpIndexItem[2] = rows.size() - 1;
                    alphabet.add(tmpIndexItem);
                }

                checkedContacts = new boolean[arrcontacts.size()];
                for(int i =0;i<arrcontacts.size();i++)
                {
                    checkedContacts[i] = false;
                }
                adapter.setRows(rows);
                setListAdapter(adapter);

                updateList();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(this, Share.class);
        i.putExtra("record_id", recId);
        i.putExtra("member_id", memid);
        i.putExtra("record_desc", recDesc);
        i.putExtra("record_date", recDate);
        i.putExtra("record_title", recTitle);
        i.putExtra("record_tag", recTag);
        i.putExtra("member_fname", memfname);
        i.putExtra("member_lname", memlname);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("EXIT", true);
        this.startActivity(i);
    }
}



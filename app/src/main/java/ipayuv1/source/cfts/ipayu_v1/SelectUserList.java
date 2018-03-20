package ipayuv1.source.cfts.ipayu_v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import ipayuv1.source.cfts.adapters.UserList_Adapter;
import ipayuv1.source.cfts.listItems.UserList_items;
import ipayuv1.source.cfts.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oquendo on 5/29/15.
 */
public class SelectUserList extends AppCompatActivity
{

    ListView selectlist;
    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;
    SharedPreferences pref;

    private UserList_Adapter userlistAdapter;
    private ArrayList<UserList_items> userList;
    ProgressDialog pDialog;

    String idB="";
    String idusers="";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectuser);

        ActionBar ab = getSupportActionBar();
        ab.setElevation(0);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));
        ab.setTitle("SELECT CONTACT");
        pref = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idusers = pref.getString("idusers", "");

        selectlist = (ListView)findViewById(R.id.getuserlist);

        userList = new ArrayList<UserList_items>();
        userlistAdapter = new UserList_Adapter(getApplicationContext(),userList);
        selectlist.setAdapter(userlistAdapter);

        new getAllusers().execute();

    }

    public class getAllusers extends AsyncTask<String, String, String> {

        String userid="",userfname="", image="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectUserList.this);
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {

                Log.e("getAllUsers", idusers);

                JSONObject myParams = new JSONObject();
                myParams.accumulate("id_user", idusers);
                json = jsonParser.makeHttpRequest(Global.getAllUser,myParams);

                Log.d("Get All Users", json.toString());
            } catch (Exception e) {
                Toast.makeText(SelectUserList.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try{
                pDialog.dismiss();
                try{
                    JSONArray jObject2 = null;

                    jObject = json.getJSONObject("GetAllUsers");
                    jObject2 = jObject.getJSONArray("Data");
                    Log.e("ErrorSa", jObject2.toString());
                    for(int i=0; i<jObject2.length(); i++){
                        JSONObject json_data = jObject2.getJSONObject(i);
                        Log.e("ErrorTID", userList.toString());
                        userid = json_data.getString("id_users");
                        userfname = json_data.getString("user_fname");
                        image = json_data.getString("image");
                        userList.add(new  UserList_items(userfname, image, userid));
                        userlistAdapter.notifyDataSetChanged();
                    }
                    selectlist.setVisibility(View.VISIBLE);
//                    selectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            idB = userList.get(position).getP2();
//
//                            new addThread().execute(idB);
//                        }
//                    });
                }catch(JSONException ex){
                    Toast.makeText(SelectUserList.this,ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }


    public class addThread extends AsyncTask<String, String, String> {

        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectUserList.this);
            pDialog.setMessage("Adding new conversation ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {


                JSONObject myParams = new JSONObject();
                myParams.accumulate("p_a_id", idusers);
                myParams.accumulate("p_b_id", args[0]);
                json = jsonParser.makeHttpRequest(Global.addThread,myParams);

                Log.d("Add Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(SelectUserList.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try{
                pDialog.dismiss();
                try
                {

                    JSONArray jObject2 = null;

                    jObject = json.getJSONObject("AddTherad");


                    for(int i=0; i<jObject.length(); i++){
                        JSONObject json_data = jObject;

                        result = json_data.getString("ErrorCode");

                    }

                    if(result.equals("0"))
                    {

                        SelectUserList.this.finish();

                    }
                    else
                    {
                        Toast.makeText(SelectUserList.this,"Cannot Add Thread",Toast.LENGTH_SHORT).show();
                    }



                }
                catch(JSONException ex)
                {
                    Toast.makeText(SelectUserList.this,ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                // app icon in action bar clicked; go home
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        this.finish();
    }
}

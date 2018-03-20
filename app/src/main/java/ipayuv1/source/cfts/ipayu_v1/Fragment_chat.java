package ipayuv1.source.cfts.ipayu_v1;

/**
 * Created by oquendo on 5/22/15.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ipayuv1.source.cfts.FloatingActionButton;
import ipayuv1.source.cfts.adapters.Thread_Adapter;
import ipayuv1.source.cfts.listItems.UserList_items;
import ipayuv1.source.cfts.utilities.ImageLoader;
import ipayuv1.source.cfts.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Fragment_chat extends Fragment {

    ListView list;
    FloatingActionButton add;
    //Button add;
    int count = 0;

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;

    ProgressDialog pDialog;
    ImageLoader imageLoader = new ImageLoader(getActivity());
    private Thread_Adapter threadAdapter;
    private ArrayList<UserList_items> userList;

    ArrayList<String> listID = new ArrayList<String>();
    ArrayList<String> listThreadID = new ArrayList<String>();

    Context context = getActivity();
    SharedPreferences pref;
    String idusers="";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_chat,container,false);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idusers = pref.getString("idusers", "");
//        v.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i(getTag(), "keyCode: " + keyCode);
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    Log.i(getTag(), "onKey Back listener is working!!!");
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                    Intent i = new Intent(getActivity(), Home.class);
//
//                    startActivity(i);
//
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        add = (FloatingActionButton)v.findViewById(R.id.attach);
        //add = (Button)v.findViewById(R.id.attach);
        list = (ListView)v.findViewById(R.id.userListInfo);
        list.setFooterDividersEnabled(false);

        userList = new ArrayList<UserList_items>();
        threadAdapter = new Thread_Adapter(getActivity().getApplicationContext(),userList);
        list.setAdapter(threadAdapter);

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SelectUserList.class);
                startActivity(i);
            }
        });

        try{
            new checkThread().execute();
        }catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    public class checkThread extends AsyncTask<String, String, String> {
        int p_a_id,p_b_id;
        int countuser=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("id_user",idusers);
                json = jsonParser.makeHttpRequest(Global.getThreadAPI,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
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

                    jObject = json.getJSONObject("GetThread");
                    jObject2 = jObject.getJSONArray("Data");
                    countuser = jObject2.length();
                    count = 0;
                    for(int i=0; i<jObject2.length(); i++){
                        JSONObject json_data = jObject2.getJSONObject(i);

                        listID.add(json_data.getString("id_personB"));
                        listThreadID.add(json_data.getString("id_thread"));

                    }
                    if(countuser > 0)
                    {
                            for(String id: listID) {
                                new getUserInfo().execute(id);
                            }
                    }

                }
                catch(JSONException ex)
                {
                    Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERRORSATHREAD", e.toString());
            }
        }
    }

    public class getUserInfo extends AsyncTask<String, String, String> {
        String p2="";
        int countuser=0;
        String user_name="", image="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Fetching data ...");
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {

                    JSONObject myParams = new JSONObject();
                    myParams.accumulate("id_user", args[0]);
                    json = jsonParser.makeHttpRequest(Global.getUserInfo, myParams);
                    Log.d("Get User", json.toString());

            } catch (Exception e) {
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
                try
                {

                    JSONArray jObject2 = null;


                    jObject = json.getJSONObject("GetUser");
                    jObject2 = jObject.getJSONArray("Data");


                    for(int i=0; i<jObject2.length(); i++){
                        Log.e("ErrorTID", userList.toString());
                        JSONObject json_data = jObject2.getJSONObject(i);
                        user_name = json_data.getString("user_fname");
                        p2 = json_data.getString("id_users");
                        image = json_data.getString("image");
                        userList.add(new UserList_items(user_name, image, p2, Integer.valueOf(listThreadID.get(count))));
                    }

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String p2_id = userList.get(position).getP2();
                            String threadID = String.valueOf(userList.get(position).getID());
                            String user_name = userList.get(position).getName();

                            Intent i = new Intent(getActivity(),Conversation.class);
                            i.putExtra("user_name",user_name);
                            i.putExtra("p2_id",p2_id);
                            i.putExtra("threadID",threadID);

                            startActivity(i);
                        }
                    });

            }catch(Exception e){
                Log.e("ERRORSAUSERINFO", e.toString());
            }
            finally
            {

                pDialog.dismiss();
            }
        }
    }

}

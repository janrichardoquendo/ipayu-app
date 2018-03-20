package ipayuv1.source.cfts.ipayu_v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ipayuv1.source.cfts.adapters.Card_adapter;
import ipayuv1.source.cfts.listItems.Card_items;
import ipayuv1.source.cfts.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ipayuv1.source.cfts.ipayu_v1.Global;


public class Reg_security extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String IdUser = "idusers";
    public static final String UserN = "username";


    SharedPreferences sharedpreferences;

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;

    String fname,mname,sname,bday,gender,email,phone,addr,uname,upass,urpass;


    EditText username,password,rpassword;
    Button finish;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_security);


        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);


        Intent getI = getIntent();

        fname = getI.getStringExtra("fname");
        mname = getI.getStringExtra("mname");
        sname = getI.getStringExtra("sname");
        bday = getI.getStringExtra("bday");
        gender = getI.getStringExtra("gender");
        email = getI.getStringExtra("email");
        phone = getI.getStringExtra("phone");
        addr = getI.getStringExtra("addr");

        ActionBar ab = getSupportActionBar();
        ab.setTitle("REGISTRATION");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));

        username = (EditText)findViewById(R.id.txtUsername);
        password = (EditText)findViewById(R.id.txtPassword);
        rpassword = (EditText)findViewById(R.id.txtRpassword);
        finish = (Button)findViewById(R.id.btnFinish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uname = username.getText().toString();
                upass = password.getText().toString();
                urpass=rpassword.getText().toString();

                if(!upass.equals(urpass))
                {
                    password.setText("");
                    rpassword.setText("");
                    Toast.makeText(getApplication(),"Password did not match.",Toast.LENGTH_LONG).show();
                }else
                {
                    new newMember().execute();
                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            case android.R.id.home:
                intent = new Intent(this, Reg_contact.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                this.finish();
                // app icon in action bar clicked; go home
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class newMember extends AsyncTask<String, String, String> {
        String transMsg;
        int id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reg_security.this);
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("fname",fname);
                myParams.accumulate("mname",mname);
                myParams.accumulate("lname",sname);
                myParams.accumulate("email",email);
                myParams.accumulate("gender",gender);
                myParams.accumulate("birthdate",bday);
                myParams.accumulate("contact",phone);
                myParams.accumulate("address",addr);
                myParams.accumulate("username",uname);
                myParams.accumulate("password",upass);

                json = jsonParser.makeHttpRequest(Global.newMemberAPI,myParams);

                Log.d("Add Member", json.toString());
            } catch (Exception e) {
                Toast.makeText(Reg_security.this,e.toString(),Toast.LENGTH_LONG).show();
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

                    jObject = json.getJSONObject("NewMember");

                    for(int i=0; i<jObject.length(); i++){
                        JSONObject json_data = jObject;
                        transMsg = json_data.getString("TransMsg");
                    }

                    new checkUser().execute();

                }
                catch(JSONException ex)
                {
                    Toast.makeText(Reg_security.this,ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }



    public class checkUser extends AsyncTask<String, String, String> {
        String idUser;
        int id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reg_security.this);
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("username",uname);
                myParams.accumulate("password",upass);

                json = jsonParser.makeHttpRequest(Global.checkUserAPI,myParams);

                Log.d("Add Member", json.toString());
            } catch (Exception e) {
                Toast.makeText(Reg_security.this,e.toString(),Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try{
                pDialog.dismiss();
                try
                {

                    JSONObject jObject2 = null;

                    jObject = json.getJSONObject("CheckUser");
                    jObject2 = jObject.getJSONObject("Data");

                    for(int i=0; i<jObject2.length(); i++){
                        JSONObject json_data = jObject2;
                        idUser = json_data.getString("id_users");

                    }


                    if(!idUser.equals(""))
                    {

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(IdUser, idUser);
                        editor.putString(UserN, uname);

                        editor.commit();

                        Intent i = new Intent(Reg_security.this,Home.class);
                        i.putExtra("iduser",idUser);
                        i.putExtra("username",uname);

                        startActivity(i);
                        finish();
                    }

                    Log.i("err", idUser);

                }
                catch(JSONException ex)
                {
                    Toast.makeText(Reg_security.this,ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }


}

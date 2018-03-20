package ipayuv1.source.cfts.ipayu_v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import ipayuv1.source.cfts.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity implements OnClickListener{

    Button btnLogin;
    EditText uname, pword;
    TextView signup;


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String IdUser = "idusers";
    public static final String UserN = "username";
    public static final String UserPhone = "userphone";
    public static final String UserFname = "userfname";


    SharedPreferences sharedpreferences;

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;

    ProgressDialog pDialog;
    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        btnLogin = (Button)findViewById(R.id.btnLogin);
        uname = (EditText)findViewById(R.id.txtUname);
        pword = (EditText)findViewById(R.id.txtPword);
        signup = (TextView)findViewById(R.id.txtSignUp);

//        TelephonyManager phoneManager = (TelephonyManager)
//
//                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        if(!phoneManager.getLine1Number().equals(""))
//        {
//            Global.myPhone = phoneManager.getLine1Number();
//
//        }
//        else
//        {
//            Toast.makeText(getApplication(),"no sim card",Toast.LENGTH_LONG).show();
//        }
//
        video = (VideoView) findViewById(R.id.videoView);
        Uri videoUrl = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.sample);

        video.setVideoURI(videoUrl);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        btnLogin.setOnClickListener((OnClickListener) this);
        signup.setOnClickListener((OnClickListener) this);

    }

    public void onClick(final View v){
        if(v.getId()==R.id.btnLogin){

            new checkUser().execute();
        }else if(v.getId()==R.id.txtSignUp){
            startActivity(new Intent(this, Reg_personal.class));
            this.finish();
            return;
        }
    }

    public class checkUser extends AsyncTask<String, String, String> {
        String idUser,userPhone,userFname;
        int id;
        int countuser=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("username",uname.getText().toString());
                myParams.accumulate("password",pword.getText().toString());

                json = jsonParser.makeHttpRequest(Global.checkUserAPI,myParams);

                Log.d("Add Member", json.toString());
            } catch (Exception e) {
                Toast.makeText(Login.this,e.toString(),Toast.LENGTH_LONG).show();
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
                        userPhone = json_data.getString("user_contact");
                        userFname = json_data.getString("user_fname");
                        countuser = Integer.parseInt(json_data.getString("count"));


                    }




                    if(countuser!=0)
                    {
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(IdUser, idUser);
                        editor.putString(UserPhone,userPhone);
                        editor.putString(UserFname,userFname);
                        editor.putString(UserN, uname.getText().toString());


                        editor.commit();

                        Intent i = new Intent(Login.this,Home.class);
                        startActivity(i);
                        finish();
                        return;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
                    }

                    Log.i("err", idUser);

                }
                catch(JSONException ex)
                {
                    Toast.makeText(Login.this,ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(IdUser)) {
            if (sharedpreferences.contains(UserN)) {

                Intent i = new Intent(this, Home.class);

                startActivity(i);
                finish();
            }
        }
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        this.finish();
        return;
    }
}

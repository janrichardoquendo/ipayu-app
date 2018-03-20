package ipayuv1.source.cfts.ipayu_v1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ipayuv1.source.cfts.adapters.ChatArrayAdapter;
import ipayuv1.source.cfts.adapters.ChatMessage;
import ipayuv1.source.cfts.utilities.FTDownload;
import ipayuv1.source.cfts.utilities.FTUpload;
import ipayuv1.source.cfts.utilities.JSONParser;

/**
 * Created by oquendo on 5/28/15.
 */


public class Conversation extends AppCompatActivity
{

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    private static final String TAG = "ChatActivity";
    private ChatArrayAdapter chatArrayAdapter;


    private static final String IMAGE_DIRECTORY_NAME = "Chatimages";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;

    private static final int PICK_FROM_FILE = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;

    static File mediaStorageDir;

    private AlertDialog dialog;

    private boolean side = false;
    ListView list;
    Button sendsample,attach;
    EditText msg;


    String p2_id , threadID,threadID2,p2_contact;
    String message="";
    String theImagePath="";

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;

    ProgressDialog pDialog;

    int counter = 0;

    String msg_type,lastMsgID;

    TimerTask task = null;

    FTUpload fileUpload=null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Intent data = getIntent();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(data.getStringExtra("user_name").toUpperCase());
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));

        ab.setDisplayHomeAsUpEnabled(true);

        captureImageInitialization();

        fileUpload = new FTUpload(Conversation.this);

        p2_id = data.getStringExtra("p2_id");

        threadID = data.getStringExtra("threadID");

        list = (ListView)findViewById(R.id.listView1);
        sendsample = (Button)findViewById(R.id.buttonSend);
        msg=(EditText)findViewById(R.id.chatText);
        attach=(Button)findViewById(R.id.attach);

        chatArrayAdapter = new ChatArrayAdapter(Conversation.this, R.layout.message);
        list.setAdapter(chatArrayAdapter);

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(chatArrayAdapter);

        Log.e("Fields",Global.userID+" "+p2_id+" "+threadID);

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        new checkThread().execute();

        sendsample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = msg.getText().toString();
                msg.setText("");
                msg_type = "1";

                side =true;

                chatArrayAdapter.add(new ChatMessage(side, message, 0, ""));

                new addMessage().execute();
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                msg_type = "2";

                dialog.show();
            }
        });

        task = new TimerTask() {

            @Override
            public void run() {
                new getConversation2().execute();
            }
        };
    }

    public class checkThread extends AsyncTask<String, String, String> {
        int p_a_id,p_b_id;
        int countuser=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Conversation.this);
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("id_user2",Global.userID);
                myParams.accumulate("id_user1",p2_id);
                json = jsonParser.makeHttpRequest(Global.getConvo2,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(Conversation.this,e.toString(),Toast.LENGTH_LONG).show();
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
                    for(int i=0; i<jObject2.length(); i++){
                        JSONObject json_data = jObject2.getJSONObject(i);

                        threadID2 = json_data.getString("id_thread");
                    }


                    new getConversation().execute();


                }
                catch(JSONException ex)
                {
                    Toast.makeText(Conversation.this,ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    public class getConversation extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Conversation.this);
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("id_sender1",Global.userID);
                myParams.accumulate("id_sender2",p2_id);
                myParams.accumulate("id_thread1",threadID);
                myParams.accumulate("id_thread2",threadID2);
                json = jsonParser.makeHttpRequest(Global.getConvo,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(Conversation.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try{
                pDialog.dismiss();
                try {

                    JSONArray jObject2 = null;

                    jObject = json.getJSONObject("GetConvo");
                    jObject2 = jObject.getJSONArray("Data");

                    String message, sender, msgType;
                    String imagemes = "";
                    counter = jObject2.length();
                    for (int i = 0; i < jObject2.length(); i++) {
                        JSONObject json_data = jObject2.getJSONObject(i);

                        message = json_data.getString("message");
                        sender = json_data.getString("id_sender");
                        msgType = json_data.getString("type_message_id");

                        if (sender.equals(Global.userID)) {
                            side = true;


                        } else {
                            side = false;

                        }
                        if (msgType.equals("1")) {
                            chatArrayAdapter.add(new ChatMessage(side, message, 0, ""));
                        } else
                        {
                            chatArrayAdapter.add(new ChatMessage(side, "", 1, message));
                        }
                    }
                    Timer timer = new Timer();
                    timer.schedule(task, new Date(), 3000);

                }
                catch(JSONException ex)
                {
                    Toast.makeText(Conversation.this,ex.toString(), Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    public class getConversation2 extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(Conversation.this);
//            pDialog.setMessage("Fetching data ...");
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("id_sender1",Global.userID);
                myParams.accumulate("id_sender2",p2_id);
                myParams.accumulate("id_thread1",threadID);
                myParams.accumulate("id_thread2",threadID2);
                json = jsonParser.makeHttpRequest(Global.getConvo,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(Conversation.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try{
                //pDialog.dismiss();
                try
                {

                    JSONArray jObject2 = null;

                    jObject = json.getJSONObject("GetConvo");
                    jObject2 = jObject.getJSONArray("Data");

                    String message,sender,msgType;
                    if(counter!=jObject2.length()) {
                        counter = jObject2.length();
                        list.setAdapter(null);
                        chatArrayAdapter.clearItem();
                        chatArrayAdapter.clear();
                        list.setAdapter(chatArrayAdapter);
                        for (int i = 0; i < jObject2.length(); i++) {
                            JSONObject json_data = jObject2.getJSONObject(i);

                            message = json_data.getString("message");
                            sender = json_data.getString("id_sender");
                            msgType = json_data.getString("type_message_id");

                            if (sender.equals(Global.userID)) {
                                side = true;


                            } else {
                                side = false;

                            }

                            if(msgType.equals("1")) {
                                chatArrayAdapter.add(new ChatMessage(side, message, 0, ""));
                            }
                            else
                            {
                                chatArrayAdapter.add(new ChatMessage(side, "", 1, message));
                                Log.e("ImageMessage",message);
                            }

                        }
                        list.setAdapter(chatArrayAdapter);
                    }


                }
                catch(JSONException ex)
                {
                    Toast.makeText(Conversation.this,ex.toString(), Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    public class addMessage extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(Conversation.this);
//            pDialog.setMessage("Fetching data ...");
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("msg_type",msg_type);
                myParams.accumulate("msg",message);
                json = jsonParser.makeHttpRequest(Global.addMsg,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(Conversation.this, e.toString(), Toast.LENGTH_LONG).show();
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

                    jObject = json.getJSONObject("AddMessage");
                    //jObject2 = jObject.getJSONArray("Data");
                    String message,sender,msgType;

                    for (int i = 0; i < jObject.length(); i++) {
                        JSONObject json_data = jObject;

                        lastMsgID = json_data.getString("Data");

                    }

                    if(!lastMsgID.equals(""))
                    {

                        new addChat().execute();

                    }

                }
                catch(JSONException ex)
                {
                    Toast.makeText(Conversation.this,ex.toString(), Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    public class addChat extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(Conversation.this);
//            pDialog.setMessage("Fetching data ...");
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("thread_id",threadID);
                myParams.accumulate("msg_id",lastMsgID);
                myParams.accumulate("sender_id",Global.userID);

                json = jsonParser.makeHttpRequest(Global.addChat,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(Conversation.this, e.toString(), Toast.LENGTH_LONG).show();
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

                    jObject = json.getJSONObject("AddChat");
//                    jObject2 = jObject.getJSONArray("Data");
                    String messageAddChat="",sender,msgType;

                    for (int i = 0; i < jObject.length(); i++) {
                        JSONObject json_data = jObject;

                        messageAddChat = json_data.getString("ErrorCode");

                    }

                    if(!messageAddChat.equals("0"))
                    {
                        Toast.makeText(getApplicationContext(),"Message not saved",Toast.LENGTH_LONG).show();
                    }
                    else

                    {
                        new getConversation2().execute();
                    }


                }
                catch(JSONException ex)
                {
                    Toast.makeText(Conversation.this,ex.toString(), Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    //for image
    private void captureImageInitialization() {

        final String[] items = new String[] { "Take from camera",
                "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    captureImage();

                } else {

                    SelectFile();
                }
            }
        });

        dialog = builder.create();
    }

    private void SelectFile() {
        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_FILE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
            {
                theImagePath = fileUri.toString();
                Global.IMAGE_PATH = theImagePath;

                fileUpload.new sendingImage().execute(threadID);

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {

                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        if (requestCode == PICK_FROM_FILE) {

            if (resultCode == RESULT_OK)
            {

                mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        IMAGE_DIRECTORY_NAME);

                mediaStorageDir.mkdirs();

                String s = getPath(data.getData());
                theImagePath = s;
                Global.IMAGE_PATH = theImagePath;


                fileUpload.new sendingImage().execute(threadID);


            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(),
                        "User cancelled image selection", Toast.LENGTH_SHORT)
                        .show();
            } else {

                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator);

        } else {
            return null;
        }

        return mediaFile;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            case android.R.id.home:
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                this.finish();
                // app icon in action bar clicked; go home
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        task.cancel();
        this.finish();
    }
}

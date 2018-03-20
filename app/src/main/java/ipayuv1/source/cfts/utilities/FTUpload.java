package ipayuv1.source.cfts.utilities;

/**
 * Created by oquendo on 5/27/15.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ipayuv1.source.cfts.ipayu_v1.Global;

public class FTUpload
{


    SharedPreferences sharedpreferences;

    static final String FTP_HOST = "ftp.centennialtech.biz";
    static final String FTP_USER = "ipayu";
    static final String FTP_PASS = "123456";
    String msg="";
    String threadId="";
    ProgressDialog pDialog;

    String username="";

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;

    Context c=null;

    private int serverResponseCode = 0;

    public FTUpload(Context context)
    {
        this.c = context;



    }




    public class sendingImage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(c);
            pDialog.setMessage("Sending attachment ...");
            pDialog.show();
            pDialog.setCanceledOnTouchOutside(false);
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag


//           Global.IMAGE_URI =  uploadFile();
            threadId = args[0];
            uploadFile2();

           return null;

        }

        @Override
        protected void onPostExecute(String file_url) {




            pDialog.dismiss();




        }


    }



//    public String uploadFile(){
//
//
//
//        FTPClient client = new FTPClient();
//
//        String directory="/Images/"+Global.userFname+"/"+Global.phoneNumber+"/";
//        String pathName="";
//
//        FileInputStream in=null;
//        long time= System.currentTimeMillis();
//
//        try {
//
//            client.connect(FTP_HOST);
//            if(client.login(FTP_USER, FTP_PASS))
//            {
//                client.enterLocalPassiveMode();
//                client.setFileType(FTPClient.BINARY_FILE_TYPE);
//
//                in = new FileInputStream(new File(Global.IMAGE_PATH));
//
//                client.makeDirectory(directory);
//                client.changeWorkingDirectory(directory);
//                String extension = Global.IMAGE_PATH.substring((Global.IMAGE_PATH.lastIndexOf(".") + 1),
//                                                                Global.IMAGE_PATH.length());
//
//                boolean result = client.storeFile(Global.userFname+"_"+Global.phoneNumber+time+"."+extension,in);
//
//                if(result)
//                {
//                    msg = "success";
//
//                    Global.IMAGE_URI = Global.userFname+"_"+Global.phoneNumber+time+"."+extension;
//
//                    new addMessage().execute(Global.IMAGE_URI);
//                }
//                else
//                {
//                    msg="failed";
//
//                }
//            }
//            else
//            {
//                msg="I cannot connect";
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("pucha",e.toString());
//
//        }
//        finally
//        {
//            if(msg.equals("success"))
//            {
//                Log.i("result", msg);
//
//
//            }
//
//        }
//
//
//        return pathName;
//
//    }
//
//
//
//    public boolean getImage()
//    {
//
//        FTPClient client = new FTPClient();
//
//        String directory="/Images/"+Global.userFname+"/"+Global.phoneNumber;
//
//
//
//
//
//        return false;
//    }
//
//
//





    public int uploadFile2() {

        String fileName = Global.IMAGE_PATH;

        String source = Global.imagelocpath;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);

        String text;
        BufferedReader reader = null;
        long time= System.currentTimeMillis();

        if (!sourceFile.isFile()) {

            Log.i("Err","Cant upload");

            return 0;

        } else {
            try {


                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(source);


                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);



                dos = new DataOutputStream(conn.getOutputStream());

                String extension = Global.IMAGE_PATH.substring((Global.IMAGE_PATH.lastIndexOf(".") + 1),
                        Global.IMAGE_PATH.length());

                fileName=Global.userFname+time+"_"+Global.phoneNumber+"."+extension;

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];


                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();


                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                text = sb.toString();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    Log.e("resp",text);
                    String[] parts = fileName.split("_");

                    new addMessage().execute(parts[0]+"."+extension);

                }


                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {


                ex.printStackTrace();


                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {


                e.printStackTrace();

                Log.i("Upload file to server" , e.getMessage());
            }

            return serverResponseCode;

        }
    }


        public class addMessage extends AsyncTask<String, String, String> {

        String lastMsgID="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(get);
//            pDialog.setMessage("Fetching data ...");
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                JSONObject myParams = new JSONObject();
                myParams.accumulate("msg_type","2");
                myParams.accumulate("msg",args[0]);
                json = jsonParser.makeHttpRequest(Global.addMsg,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
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

                        new addChat().execute(lastMsgID);

                    }

                }
                catch(JSONException ex)
                {
                    Toast.makeText(c,ex.toString(), Toast.LENGTH_LONG).show();
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
                myParams.accumulate("thread_id",threadId);
                myParams.accumulate("msg_id",args[0]);
                myParams.accumulate("sender_id",Global.userID);

                json = jsonParser.makeHttpRequest(Global.addChat,myParams);

                Log.d("Get Thread", json.toString());
            } catch (Exception e) {
                Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(c,"Message not saved",Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                    }

                }
                catch(JSONException ex)
                {
                    Toast.makeText(c,ex.toString(), Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }




}

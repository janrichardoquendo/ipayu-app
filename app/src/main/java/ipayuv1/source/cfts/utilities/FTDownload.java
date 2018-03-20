package ipayuv1.source.cfts.utilities;

/**
 * Created by oquendo on 5/27/15.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import ipayuv1.source.cfts.ipayu_v1.Global;
import ipayuv1.source.cfts.ipayu_v1.Home;

import org.apache.commons.net.ftp.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FTDownload {

    SharedPreferences sharedpreferences;

    private static final String IMAGE_DIRECTORY_NAME = "ChatImages";
    static final String FTP_HOST = "ftp.centennialtech.biz";
    static final String FTP_USER = "ipayu";
    static final String FTP_PASS = "123456";
    String msg="";
    String threadId="";
    ProgressDialog pDialog;

    String username="";
    String getImage="";

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONArray jArray = null;
    JSONObject jObject = null;

    Context c=null;


    public FTDownload(Context context) {
        this.c = context;
    }

    public class getImage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(c);
//            pDialog.setMessage("Sending attachment ...");
//            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            getImage = args[0];

            getImage();

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }

    public void getImage() {

        FTPClient client = new FTPClient();

        String directory="/Images/"+Global.userFname+"/"+Global.phoneNumber+"/";
        String pathName="";

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory().getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/Pictures/"
                + IMAGE_DIRECTORY_NAME + "/" + Global.userFname + "/" + Global.phoneNumber + "/";

        OutputStream outputStream = null;

        try {

            File path = new File(targetPath+"/"+Global.userID+".jpg");

            try
            {
                outputStream = new BufferedOutputStream(new FileOutputStream(path));
                client.retrieveFile(directory+getImage, outputStream);
            }
            catch(IOException e)
            {
                Log.i("error",e.toString());
            }

        } finally {
            if (outputStream != null)
            {
                try {
                    outputStream.close();
                }
                catch(Exception e)
                {

                }
            }
        }
    }
}

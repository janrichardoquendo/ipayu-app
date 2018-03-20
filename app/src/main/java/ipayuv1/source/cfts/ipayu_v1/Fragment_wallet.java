package ipayuv1.source.cfts.ipayu_v1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ipayuv1.source.cfts.FloatingActionButton;
import ipayuv1.source.cfts.adapters.Card_adapter;
import ipayuv1.source.cfts.listItems.Card_items;
import ipayuv1.source.cfts.utilities.ConnectionDetector;
import ipayuv1.source.cfts.utilities.ImageLoader;
import ipayuv1.source.cfts.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_wallet extends Fragment implements OnClickListener{

    private static final String GET_CARDS_URL = "getCardInfo";

    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONObject jObject = null;
    SharedPreferences pref;

    ImageLoader imageLoader = new ImageLoader(getActivity());

    private ArrayList<Card_items> cardListItem;
    private Card_adapter cardListAdapter;
    Dialog optDialog;
    ProgressDialog pDialog;

    String idusers="";
    ListView cardLV;
    DatePicker datePicker;
    Button btnHistory, btnAbout, btnDateFrm, btnDateTo;
    FloatingActionButton btnNewCard;
    EditText dateFrom, dateTo;
    StringBuilder datePicked;
    Calendar c;
    int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_wallet,container,false);

        pref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idusers = pref.getString("idusers", "");
        cardLV = (ListView) v.findViewById(R.id.cardLV);
        btnNewCard = (FloatingActionButton) v.findViewById(R.id.btnNewCard);

        cardListItem = new ArrayList<Card_items>();
        cardListAdapter = new Card_adapter(getActivity().getApplicationContext(),cardListItem);
        cardLV.setAdapter(cardListAdapter);
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Log.e("YEAR", Integer.toString(year));
        Log.e("MONTH", Integer.toString(month));
        Log.e("DAY", Integer.toString(day));

        btnNewCard.setOnClickListener((OnClickListener) this);
        if(new ConnectionDetector(getActivity().getApplicationContext()).hasConnection()){
            new getCards().execute();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
        }

        cardLV.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), "samsamskajdsfcydsbvyubyu", Toast.LENGTH_LONG).show();
                Global.cardID = cardListItem.get(position).getNumber();
                Global.cardName = cardListItem.get(position).getName();
                //Intent i = new Intent(getActivity(), History.class);
//                i.putExtra("cardName", cardListItem.get(position).getName());
                //startActivity(i);
                //getActivity().finish();
               viewDetails(cardListItem.get(position).getPoints(), cardListItem.get(position).getImageUrl());
            }
        });
        return v;
    }
    public void viewDetails(String points,String img){
        try{
            optDialog = new Dialog(getActivity());
            LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
            View v = li.inflate(R.layout.activity_view_card, null);
            dateFrom = (EditText) v.findViewById(R.id.txtDateFrom);
            dateTo = (EditText) v.findViewById(R.id.txtDateTo);
            datePicker = (DatePicker) v.findViewById(R.id.datePicker);
            //datePicker.init(year, month, day, null);
            datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
                    getDatePickerValue(arg1, arg2 + 1, arg3);
                }
            });
            optDialog.setContentView(v);
            optDialog.setTitle("DATE SELECTION");
            optDialog.setCancelable(true);
            WindowManager.LayoutParams lp = optDialog.getWindow().getAttributes();
            lp.dimAmount=0.85f;
            optDialog.getWindow().setAttributes(lp);
            btnHistory = (Button) v.findViewById(R.id.btnCancel);
            btnAbout = (Button) v.findViewById(R.id.btnOk);
            btnDateFrm = (Button) v.findViewById(R.id.btnDateFrm);
            btnDateTo = (Button) v.findViewById(R.id.btnDateTo);
            btnHistory.setOnClickListener((OnClickListener) this);
            btnAbout.setOnClickListener((OnClickListener) this);
            btnDateFrm.setOnClickListener((OnClickListener) this);
            btnDateTo.setOnClickListener((OnClickListener) this);
            optDialog.getWindow().setLayout(600, 950);
            //optDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            optDialog.show();

        } catch (Exception e) {
            Log.e("ErrorSaSelect", e.toString());
        }
    }

    public void onClick(final View v){
        if(v.getId()==R.id.btnCancel){
            optDialog.dismiss();
        }else if(v.getId()==R.id.btnOk){
            if(!dateFrom.getText().toString().equals("") && !dateTo.getText().toString().equals("")) {
                optDialog.dismiss();
                Intent i = new Intent(getActivity(), History.class);
                startActivity(i);
                getActivity().finish();
            }else{
                Toast.makeText(getActivity(), "Invalid date.", Toast.LENGTH_LONG).show();
            }
        }else if(v.getId()==R.id.btnDateFrm){
            Global.dateFrom = String.valueOf(datePicked);
            dateFrom.setText(datePicked);
        }else if(v.getId()==R.id.btnDateTo){
            Global.dateTo = String.valueOf(datePicked);
            dateTo.setText(datePicked);
        }else if(v.getId()==R.id.btnNewCard){
            startActivity(new Intent(getActivity(), New_card.class));
            getActivity().finish();
        }
    }

    public void getDatePickerValue(int year, int month, int day){
        // set current date into textview
        datePicked = new StringBuilder()
                // Month is 0 based, just add 1
                .append(month).append("-").append(day).append("-")
                .append(year).append(" ");
    }

    public class getCards extends AsyncTask<String, String, String> {
        String name, number, points, image;
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardListItem.clear();
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
                myParams.accumulate("id_user", idusers);
                Log.e("User Id", pref.getString("idusers", ""));
                json = jsonParser.makeHttpRequest(Global.getCardAPI, myParams);
                Log.d("Fragment_wallet Succ", json.toString());
            } catch (Exception e) {
                Log.d("Error", json.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try{
                pDialog.dismiss();
                try{
                    JSONArray jObject2 = null;

                    jObject = json.getJSONObject("GetCard");
                    jObject2 = jObject.getJSONArray("Data");

                    for(int i=0; i<jObject2.length(); i++){
                        JSONObject json_data = jObject2.getJSONObject(i);
                        name = json_data.getString("merchant_name")+" "+json_data.getString("card_type_name");
                        number = json_data.getString("id_cards");
                        points = String.valueOf(json_data.getInt("total_points"));
                        image = json_data.getString("card_img_url");
                        cardListItem.add(new Card_items(number, points, name, image));
                        cardListAdapter.notifyDataSetChanged();
                    }
                }
                catch(JSONException ex){
                    //Toast.makeText(getActivity().getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                    Log.i("err",ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

}

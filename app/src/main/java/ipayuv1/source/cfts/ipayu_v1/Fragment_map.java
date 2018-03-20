package ipayuv1.source.cfts.ipayu_v1;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import ipayuv1.source.cfts.utilities.JSONParser;

public class Fragment_map extends Fragment {
    private SupportMapFragment fragment;
    private GoogleMap map;
    JSONParser jsonParser = new JSONParser();
    JSONObject json = null;
    JSONObject jObject = null;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_fragment_map, container, false);
        new getReport().execute();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }

    public class getReport extends AsyncTask<String, String, String> {
        String date, number, points, address;
        int id;
        double longi, lat;
        Geocoder coder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        LatLng loc = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity().getApplicationContext(), Global.dateFrom+" "+Global.dateTo, Toast.LENGTH_LONG).show();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching data ...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            try {
                Log.e("card id", Global.cardID);
                JSONObject myParams = new JSONObject();
                myParams.accumulate("id_cards", Global.cardID);
                myParams.accumulate("dateFrom", Global.dateFrom);
                myParams.accumulate("dateTo", Global.dateTo);
                json = jsonParser.makeHttpRequest(Global.getTransactionsAPI, myParams);
                Log.e("Report Succ", json.toString());
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

                    jObject = json.getJSONObject("GetTrans");
                    jObject2 = jObject.getJSONArray("Data");

                    for(int i=0; i<jObject2.length(); i++){
                        JSONObject json_data = jObject2.getJSONObject(i);
                        date = json_data.getString("transaction_date");
                        number = json_data.getString("id_transactions");
                        points = String.valueOf(json_data.getInt("points"));
                        address = json_data.getString("address");

                        try {
                            List<Address> addressList = coder.getFromLocationName(address, 1);
                            if (addressList != null && addressList.size() > 0) {
                                Address address = addressList.get(0);
                                lat = address.getLatitude();
                                longi = address.getLongitude();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        drawMarker(new LatLng(lat, longi));
                    }
                }
                catch(JSONException ex){
                    Toast.makeText(getActivity().getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    Log.i("err", ex.toString());
                }
            }catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }

    public void drawMarker(LatLng point) {
        if (map == null) {
            MarkerOptions markerOptions = new MarkerOptions();

            // Setting latitude and longitude for the marker
            markerOptions.position(point).title(point.toString());
            map = fragment.getMap();
            // Adding marker on the Google Map
            map.addMarker(markerOptions);
//            Toast.makeText(getActivity().getApplicationContext(), point.toString(), Toast.LENGTH_LONG).show();
//            map.addMarker(new MarkerOptions().position(new LatLng(53.558, 9.927)).title("Sample"));
//            map.addMarker(new MarkerOptions().position(new LatLng(53.558, 9.927)).title("JM"));
// 
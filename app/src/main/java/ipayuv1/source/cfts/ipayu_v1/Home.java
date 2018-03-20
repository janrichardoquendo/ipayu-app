package ipayuv1.source.cfts.ipayu_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ipayuv1.source.cfts.adapters.HomeFragmentAdapter;

public class Home extends AppCompatActivity {

    private static long back_pressed;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    ViewPager pager;
    HomeFragmentAdapter fragmentAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wallet","Chat","eMarket", "eShop", "iTaxi"};
    //private int tabIcons[] = {R.drawable.ic_tab_one, R.drawable.ic_tab_two, R.drawable.ic_tab_three}
    int Numboftabs =5;

    String iduser="",username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar ab = getSupportActionBar();
        ab.setElevation(0);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));
        ab.setTitle("iPayU");

        Intent getVal = getIntent();
        iduser = getVal.getStringExtra("iduser");
        username = getVal.getStringExtra("username");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Global.userID = sharedpreferences.getString("idusers", "");
        Global.userName = sharedpreferences.getString("username", "");
        Global.userFname = sharedpreferences.getString("userfname", "");
        Global.phoneNumber = sharedpreferences.getString("userphone", "");

        fragmentAdapter =  new HomeFragmentAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(fragmentAdapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.abc_secondary_text_material_dark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public void onBackPressed() {

            endSession();

    }

    public void endSession() {
        AlertDialog dialog = new AlertDialog.Builder(Home.this).create();
        dialog.setTitle("Logout");
        dialog.setIcon(android.R.drawable.star_on);
        dialog.setMessage("You want to logout");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        sharedpreferences.edit().clear().commit();

                        dialog.dismiss();
                        Intent i = new Intent(Home.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Home.this.finish();
                        dialog.dismiss();
                        return;
                    }
                });

        dialog.show();
    }


}

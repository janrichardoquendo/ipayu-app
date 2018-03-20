package ipayuv1.source.cfts.ipayu_v1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ipayuv1.source.cfts.adapters.TabsFragmentPagerAdapter;


public class History extends AppCompatActivity{

    ViewPager viewPager;
    TabsFragmentPagerAdapter tabsAdapter;

    SlidingTabLayout tabs;
    CharSequence Titles[]={"MAP","REPORT","GRAPH"};
    int Numboftabs =3;

//    DateFormat dateFrom, dateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

//        dateFrom = new SimpleDateFormat("MMMM dd, YYYY");
//        dateTo = new SimpleDateFormat("MMMM dd, YYYY");

        ActionBar ab = getSupportActionBar();
        ab.setElevation(0);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#072183")));
        ab.setTitle(Global.cardName.toUpperCase());
        ab.setSubtitle(Global.dateFrom + " - " + Global.dateTo);

        tabsAdapter = new TabsFragmentPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(tabsAdapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.abc_secondary_text_material_dark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            case android.R.id.home:
                intent = new Intent(this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                this.finish();
                // app icon in action bar clicked; go home
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, Home.class));
        this.finish();
    }
}

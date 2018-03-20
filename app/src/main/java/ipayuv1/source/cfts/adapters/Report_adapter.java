package ipayuv1.source.cfts.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ipayuv1.source.cfts.ipayu_v1.R;
import ipayuv1.source.cfts.listItems.Report_items;

import java.util.ArrayList;

/**
 * Created by michael on 5/22/2015.
 */
public class Report_adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Report_items> ReportListItem;

    public Report_adapter(Context context, ArrayList<Report_items> ReportListItem){
        this.context = context;
        this.ReportListItem = ReportListItem;
    }

    @Override
    public int getCount() {
        return ReportListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return ReportListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.report_item, null);
        }
        TextView tNum = (TextView) convertView.findViewById(R.id.txtTransactNum);
        TextView pts = (TextView) convertView.findViewById(R.id.txtPoints);
        TextView date = (TextView) convertView.findViewById(R.id.txtDate);
        tNum.setText(ReportListItem.get(position).getNumber());
        pts.setText(ReportListItem.get(position).getPoints()+" points earned");
        date.setText(ReportListItem.get(position).getDate());
        return convertView;
    }
}

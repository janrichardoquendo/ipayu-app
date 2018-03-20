package ipayuv1.source.cfts.ipayu_v1;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class Fragment_graph extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_fragment_graph, container, false);

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        DataPoint[] points = new DataPoint[50];
        for (int i = 0; i < 50; i++) {
            //points[i] = new DataPoint(i,3);
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(points);
        graph.addSeries(series);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.onDataChanged(false, false);

        // enable scrolling
        graph.getViewport().setScrollable(true);


        return view;
    }
}

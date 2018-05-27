package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;

public class HRStateHistory extends AppCompatActivity{

    String userid, name, hrstateValueTimestamp;
    int hrstateValue;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    ArrayList<String> array7; //array untuk Timestamp hrstate
    ArrayList<Integer> array6; //array untuk hrstate
    PointsGraphSeries<DataPoint> series ;
    int x=0;
    int i = 1;
    String[] xLabels ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrsstate);
        Intent intent = getIntent();
        if(intent!=null)
        {
            userid = intent.getStringExtra("userid");
            name = intent.getStringExtra("name");
        }

        final GraphView graph = (GraphView)findViewById(R.id.graphHRState);

        series = new PointsGraphSeries<>();
        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.addSeries(series);
        Viewport vp = graph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(0); //yg ditunjukin max berapa
        vp.setYAxisBoundsManual(true);
        vp.setMinY(1);
        vp.setMaxY(140); //yg ditunjukin max berapa
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        //graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        //graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        graph.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        graph.setTitle(name+"'s Abnormal Heartrate History" );
        graph.setTitleTextSize(40);


        array7 = new ArrayList<>(); //array untuk Timestamp hrstate
        array6 = new ArrayList<>(); //array untuk hrstate value







        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        ref.child("hrstate").child("nilaihrstate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                hrstateValue = dataSnapshot.child("tmpHr").getValue(Integer.class);
                hrstateValueTimestamp = dataSnapshot.child("Timestamp").getValue(String.class);

                array7.add(hrstateValueTimestamp);
                array6.add(hrstateValue);

                DataPoint point = new DataPoint(x, hrstateValue);
                series.appendData(point, false, 1000);
                x = x + 1;

                xLabels = new String[array7.size()];
                array7.toArray(xLabels);


                graph.getGridLabelRenderer().setHumanRounding(true);
                graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
                {
                    @Override
                    public String formatLabel(double value, boolean isValueX)
                    {
                        if (isValueX)
                        {
                            return xLabels[(int) value];
                        }
                        return super.formatLabel(value,isValueX);
                    }
                });

                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(getApplicationContext(), "Abnormal Heartbeat: "+dataPoint.getY(), Toast.LENGTH_SHORT).show();
                    }
                });

            }




            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


}


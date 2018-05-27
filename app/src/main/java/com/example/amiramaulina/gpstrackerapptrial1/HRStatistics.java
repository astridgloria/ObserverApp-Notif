package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HRStatistics extends AppCompatActivity {

    String name, userid, hrDate, hrTime, hrTimestamp;
    int hrValue;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    ArrayList<Integer> array8; //array untuk hrValue
    ArrayList<String> array9; //array untuk hrTimestamp
    LineGraphSeries<DataPoint> series ;
    int x=-1;
    int i = 1;
    String[] xLabels ;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String TanggalHariIni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_statistics);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        final GraphView graph = (GraphView)findViewById(R.id.graphHRValue);
        series = new LineGraphSeries<DataPoint>();
        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.addSeries(series);
        Viewport vp = graph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(2); //yg ditunjukin max berapa
        vp.setYAxisBoundsManual(true);
        vp.setMinY(0);
        vp.setMaxY(200); //yg ditunjukin max berapa
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        //graph.getViewport().setScrollableY(true); // enables vertical scrolling
        //graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        array8 = new ArrayList<>(); //array untuk hrvalue
        array9 = new ArrayList<>(); //array untuk timestamp

        Intent intent = getIntent();
        if(intent!=null)
        {
            userid = intent.getStringExtra("userid");
            name = intent.getStringExtra("name");
        }

        graph.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        graph.setTitle(name+"'s Daily Heartrate Stats" );
        graph.setTitleTextSize(40);


        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        ref.child("hrvalue").child("nilaihr").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                hrValue = dataSnapshot.child("tmpHR").getValue(Integer.class);
                hrTimestamp = dataSnapshot.child("Timestamp").getValue(String.class);
                hrDate = dataSnapshot.child("Date").getValue(String.class);
                hrTime = dataSnapshot.child("Time").getValue(String.class);

                TanggalHariIni = simpleDateFormat.format(calendar.getTime());

                Log.i("date hrstate", "date hrvalue " + hrDate);
                Log.i("hrvalue", "hrvalue " + hrValue);
                Log.i("tanggalhariini value", "tanggalhariini value " + TanggalHariIni);

                if  (TanggalHariIni.equals(hrDate)) {
                    array8.add(hrValue);
                    array9.add(hrTime);

                    x = x + 1;
                    DataPoint point = new DataPoint(x, hrValue);
                    series.appendData(point, false, 1000);
                    xLabels = new String[array9.size()];
                    array9.toArray(xLabels);
                }



                graph.getGridLabelRenderer().setHumanRounding(true);
                    graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);

                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return xLabels[(int) value];
                            }
                            return super.formatLabel(value, isValueX);
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

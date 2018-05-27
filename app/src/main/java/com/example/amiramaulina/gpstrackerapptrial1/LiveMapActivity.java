package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    GoogleMap mMap;
    LatLng friendLatLng;
    String latitude,longitude,name,userid,prevdate,prevImage;
    Toolbar toolbar;
    Marker marker;
    DatabaseReference reference;

    String myName,myLat,myLng,myDate;
    ArrayList<String> mKeys;
    MarkerOptions myOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_map);
        toolbar = (Toolbar)findViewById(R.id.toolbar22);

        myOptions = new MarkerOptions();
        Intent intent = getIntent();
        mKeys = new ArrayList<>();

        Bundle extras = intent.getExtras();

        if(intent!=null)
        {
            latitude= extras.getString("latitude");
            longitude = extras.getString("longitude");
            name = extras.getString("name");
            userid = extras.getString("userid");
            prevdate = extras.getString("date");
        }
        toolbar.setTitle(name + "'s Location");

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              //  Toast.makeText(getApplicationContext(),"onAdded",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //   CreateUser user = dataSnapshot.getValue(CreateUser.class);

              //  Toast.makeText(getApplicationContext(),dataSnapshot.getKey(),Toast.LENGTH_LONG).show();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                             myName = dataSnapshot.child("name").getValue(String.class);
                             myLat = dataSnapshot.child("lat").getValue(String.class);
                             myLng = dataSnapshot.child("lng").getValue(String.class);
                             myDate = dataSnapshot.child("date").getValue(String.class);



                        friendLatLng = new LatLng(Double.parseDouble(myLat),Double.parseDouble(myLng));

                        myOptions.position(friendLatLng);
                        myOptions.snippet("Last seen: "+myDate);
                        myOptions.title(myName);

                        if(marker == null)
                        {
                            marker = mMap.addMarker(myOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));
                        }
                        else
                        {
                            marker.setPosition(friendLatLng);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View row = getLayoutInflater().inflate(R.layout.custom_snippet,null);
                TextView nameTxt = row.findViewById(R.id.snippetName);
                TextView dateTxt = row.findViewById(R.id.snippetDate);
                CircleImageView imageTxt = row.findViewById(R.id.snippetImage);
                if(myName == null && myDate == null)
                {
                    nameTxt.setText(name);
                    dateTxt.setText(dateTxt.getText().toString() + prevdate);
                    Picasso.with(getApplicationContext()).load(prevImage).placeholder(R.drawable.defaultprofile).into(imageTxt);
                }
                else
                {
                    nameTxt.setText(myName);
                    dateTxt.setText(dateTxt.getText().toString() + myDate);
                }


                return row;
            }
        });

            friendLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
            MarkerOptions optionsnew = new MarkerOptions();

            optionsnew.position(friendLatLng);
            optionsnew.title(name);
        optionsnew.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
          //  optionsnew.snippet("Last seen:"+prevdate);

            if(marker == null)
            {
                marker = mMap.addMarker(optionsnew);
            }
            else
            {
                marker.setPosition(friendLatLng);
            }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

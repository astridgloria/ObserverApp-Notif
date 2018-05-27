package com.example.amiramaulina.gpstrackerapptrial1;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyNavigationTutorial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    FirebaseAuth auth;
    FirebaseUser user;
    LatLng latLngCurrent;
    DatabaseReference reference;
    TextView textName, textEmail;
    Marker marker;
    CircleImageView circleImageView;
    String myName, myEmail, myDate, mySharing,myProfileImage;
    InterstitialAd interstitialAd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_navigation_tutorial);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Family GPS Tracker");

        auth = FirebaseAuth.getInstance();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("");
        interstitialAd.loadAd(new AdRequest.Builder().build());


        if(interstitialAd.isLoaded())
        {
            interstitialAd.show();
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }



        user = auth.getCurrentUser();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        textName = (TextView) header.findViewById(R.id.nameTxt);
        textEmail = (TextView) header.findViewById(R.id.emailTxt);
        circleImageView = (CircleImageView)header.findViewById(R.id.imageView2);


        //   aSwitch.setOnCheckedChangeListener(getApplicationContext());





        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    myDate = dataSnapshot.child(user.getUid()).child("date").getValue().toString();
                    mySharing = dataSnapshot.child(user.getUid()).child("isSharing").getValue().toString();
                    myEmail = dataSnapshot.child(user.getUid()).child("email").getValue().toString();
                    myName = dataSnapshot.child(user.getUid()).child("name").getValue().toString();
                    myProfileImage = dataSnapshot.child(user.getUid()).child("profile_image").getValue().toString();
                    textName.setText(myName);
                    textEmail.setText(myEmail);
                    Picasso.with(MyNavigationTutorial.this).load(myProfileImage).placeholder(R.drawable.defaultprofile).into(circleImageView);
                }catch(NullPointerException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"Could not connect to the network. Please try again later",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();

        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.signout) {
            if (user != null) {
                auth.signOut();
                finish();


                Intent i = new Intent(MyNavigationTutorial.this, MainActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.joinCircle)
        {

            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent myIntent = new Intent(MyNavigationTutorial.this, JoinCircleActivity.class);
                        startActivity(myIntent);

                    }
                });
            }
            else
            {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent myIntent = new Intent(MyNavigationTutorial.this, JoinCircleActivity.class);
                startActivity(myIntent);

            }



        } else if (id == R.id.myCircle)
        {
            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent intent = new Intent(MyNavigationTutorial.this, MyCircleActivity.class);
                        startActivity(intent);

                    }
                });

            }
            else
            {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(MyNavigationTutorial.this, MyCircleActivity.class);
                startActivity(intent);

            }

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }








    }







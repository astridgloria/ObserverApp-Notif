package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MenuAwal extends AppCompatActivity{

    FirebaseAuth auth;
    FirebaseUser user;

    Button btnJoinCircle, btnMyCirlce, btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_awal);
        btnJoinCircle = (Button) findViewById(R.id.btnJoinCircle);
        btnMyCirlce = (Button) findViewById(R.id.btnMyCircle);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);



    }




    public void gotoJoinCircle(View v){
        Intent myIntent = new Intent(MenuAwal.this,JoinCircleActivity.class);
        startActivity(myIntent);
        //finish();
    }

    public void gotoMyCircle(View v){
        Intent myIntent = new Intent(MenuAwal.this,MyCircleActivity.class);
        startActivity(myIntent);
        //finish();
    }


    public void gotoSignOut(View v){
        auth.signOut();
        Intent myIntent = new Intent(MenuAwal.this,MainActivity.class);
        startActivity(myIntent);
        //finish();
    }





}

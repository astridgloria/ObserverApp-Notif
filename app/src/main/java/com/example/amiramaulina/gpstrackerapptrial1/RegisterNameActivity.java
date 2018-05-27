package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterNameActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText e1;
    Button b1;
    String email,password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Your Profile");
        e1 = (EditText) findViewById(R.id.editTextPass);
        b1 = (Button) findViewById(R.id.button);

        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");

        }
    }


    public void generateCode(View v) {

        if (e1.getText().toString().length() > 0) {
            Date curDate = new Date();


            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);


            final String code = String.valueOf(n);


            if(e1!=null)
            {
                Intent myIntent = new Intent(RegisterNameActivity.this, InviteCodeActivity.class);
                myIntent.putExtra("name", e1.getText().toString());
                myIntent.putExtra("email", email);
                myIntent.putExtra("password", password);
                myIntent.putExtra("date", "na");
                myIntent.putExtra("isSharing", "false");
                myIntent.putExtra("code", code);


                startActivity(myIntent);
                finish();


            }
            else
            {
               Toast.makeText(getApplicationContext(),"You must fill your email.",Toast.LENGTH_SHORT).show();
            }

        }
    }


 }


package com.example.tomatoeapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactDoctor extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNumber;
    ImageView ivWhatsApp,ivFacebook,ivInstagram,ivTwitter,imageCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_doctor);

        mEditTextNumber = findViewById(R.id.edit_text_number);
         imageCall = findViewById(R.id.image_call);
        //social media
        ivWhatsApp=findViewById(R.id.whatsApp);
        ivFacebook=findViewById(R.id.facebook);
        ivTwitter=findViewById(R.id.twitter);
        ivInstagram=findViewById(R.id.instagram);


        //listeners
        ivWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNumber="+256 703839899";
                //intitialize uri
                Uri uri=Uri.parse(String.format(
                        "https://api.whatsapp.com/send?phone=%s",sNumber
                ));
                //Initialize Intent
                Intent w=new Intent(Intent.ACTION_VIEW);
                w.setData(uri);
                //set flag
                w.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(w);
            }
        });

        //facebook
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize link
                String sAppLink="fb://page/237564710351658";
                String sPackage= "com.facebook.katana";
                String eWeblink= "https://www.facebook.com/12H1rDeveloper";

                //create a method
                 openLink(sAppLink,sPackage,eWeblink);


            }

        });

//instagram
        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize link
                String sApplink= "https://www.instagram.com/able_daniel_ofungi/";
                String sPackage= "com.instagram.android";
                openLink(sApplink,sPackage,sApplink);
            }
        });

        //twitter
        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize link
                String sApplink= "twitter://user?screen_name/bl312604830?t=e39c4dQJ-xdNRn5Zj2U7Nw&s=09";
                String sPackage= "com.twitter.android";
                String eWeblink= "https://twitter.com/AbleOfungi";
                //call method
                openLink(sApplink,sPackage,sApplink);
            }
        });






        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }


    private void makePhoneCall() {
        String number = mEditTextNumber.getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(ContactDoctor.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContactDoctor.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(ContactDoctor.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void openLink(String sAppLink, String sPackage, String eWeblink) {



        try{
            Uri uri=Uri.parse(sAppLink);
            Intent f=new Intent(Intent.ACTION_VIEW);
            f.setData(uri);
            f.setPackage(sPackage);
            //set flag
            f.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(f);


        }catch (ActivityNotFoundException activityNotFoundException){
            //open link in browser
            Uri uri=Uri.parse(eWeblink);
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);

            //set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }


    public void openMainActivity(View view) {
        Intent n =new Intent(ContactDoctor.this, MainActivity.class);
        startActivity(n);
    }
}



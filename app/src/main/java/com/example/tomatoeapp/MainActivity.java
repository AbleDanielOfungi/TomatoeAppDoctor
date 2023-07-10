package com.example.tomatoeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

package com.example.tumo20;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.example.tumo20.SessionManager.SessionManager;
//import com.example.tumo20.ml.Model;
//import com.example.tumo20.ml.Model2;
import com.example.tumo20.ml.Dan;
import com.example.tumo20.ml.Daniel;
import com.example.tumo20.ml.Final;
import com.example.tumo20.ml.Tommy;
//import com.example.tumo20.ml.Tommy;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button classfy, treatTomato;
    private TextView confidencs, classifiedas, lifeSpan1, prescription;
    private Bitmap bitmapimg;
    int imageSize = 224;
    Uri uri;
    private static final int CODE = 665;
    private static final int CAMERA_PERMISSION_CODE = 89;
    private static final int CAMERA_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        imageView.setOnClickListener(this);
        classfy.setOnClickListener(this);
        classifiedas.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

    }
    private void initViews() {
        imageView = findViewById(R.id.imageView);
        classfy = findViewById(R.id.buttonclassify);
        confidencs = findViewById(R.id.confidence);
        classifiedas = findViewById(R.id.classified);
        lifeSpan1 = findViewById(R.id.display);
        prescription = findViewById(R.id.drug);
        // treatTomato=findViewById(R.id.treat);
/*
        //initialize color drawable
        ColorDrawable leftBorder=new ColorDrawable(Color.RED);
        ColorDrawable topBorder=new ColorDrawable(Color.GREEN);
        ColorDrawable rightBorder=new ColorDrawable(Color.BLUE);
        ColorDrawable bottomBorder=new ColorDrawable(Color.YELLOW);
        ColorDrawable background=new ColorDrawable(Color.WHITE);

        //initialize drawable Array
        Drawable[] layers=new Drawable[]{
                leftBorder,//red
                topBorder,//green
                rightBorder,//blue
                bottomBorder, //yellow
                background  //white

        };
        //initialize layersDrawable

        LayerDrawable layerDrawable=new LayerDrawable(layers);
        //draw left border
        layerDrawable.setLayerInset(0,0,0,15,0);
        //draw top border
        layerDrawable.setLayerInset(1,15,0,0,15);
        //draw right border
        layerDrawable.setLayerInset(2,15,15,0,0);
        //draw bottom border
        layerDrawable.setLayerInset(3,15,15,15,0);
        //draw background
        layerDrawable.setLayerInset(4,15,15,15,15);
        //set background
        prescription.setBackground(layerDrawable);
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tumo_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clear:
                imageView.equals("");
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);

                break;
            case R.id.contact:

                Intent in = new Intent(MainActivity.this, TomatoeClinic.class);
                startActivity(in);
               /*
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.removeSession();
*/
                //  moveToLogin();
                break;
            case R.id.details:
                Intent a = new Intent(MainActivity.this, ContactDoctor.class);
                startActivity(a);
                break;
            case R.id.camera:
                Intent cam = new Intent(MainActivity.this, Camera.class);
                startActivity(cam);


            default:
                Toast.makeText(getApplicationContext(), "Not menu option", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /* private void moveToLogin() {
         Intent xerxes = new Intent(MainActivity.this, Login.class);
         xerxes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(xerxes);
     }
 */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                SelectPicOption();
                break;
            case R.id.buttonclassify:
                if (uri != null) {
                    bitmapimg = Bitmap.createScaledBitmap(bitmapimg, imageSize, imageSize, false);
                    classfyTumor(bitmapimg);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Select MRI image first",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.classified:
                String query = classifiedas.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.google.com/search?q=" + query));
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(),
                        "Not menu option",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void classfyTumor(Bitmap bitmapimg) {
        try {
            Final model = Final.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            bitmapimg.getPixels(intValues, 0, bitmapimg.getWidth(), 0, 0, bitmapimg.getWidth(), bitmapimg.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];//RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Final.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Tomato___Bacterial_spot",
                    "Tomato___Early_blight",
                    "Tomato___Late_blight",
                    "Tomato___Septoria_leaf_spot",
                    "Tomato___Spider_mites Two-spotted_spider_mite",
                    "Tomato___Target_Spot",
                    "Tomato___Tomato_Yellow_Leaf_Curl_Virus",
                    "Tomato___Tomato_mosaic_virus",
                    "Tomato___healthy",
                    "This is not a tomatoe leaf"
            };

            classifiedas.setText(classes[maxPos]);

            String able = "";
            for (int i = 0; i < classes.length; i++) {
                able += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);


            }

            confidencs.setText(able);

            //lifespan
            String[] lifespan = {"0 weeks: The plant can be considered dead",
                    "2 weeks: the tomato plant is able to withstand for about extra two weeks",
                    "0 weeks: The plant can be considered dead",
                    "3 weeks: The plant can't hold on past extra three weeks",
                    "1 week: beyond this the plant csn't withstand the population of eight legged mites",
                    "2 and half weeks:Beyond this the disease advances beyond the plant's ability to withstand",
                    "1 week: Beyond this the plant is going to die most likely",
                    "1 week: AFter that the plant eventualy dies",
                    "The lifespan is Usual and normal",
                    "No lifespan Predicted",


            };
            lifeSpan1.setText(lifespan[maxPos]);


            //Treatment
            String[] medication = {" Remove symptomatic plants from the field or greenhouse to prevent the spread of bacteria to healthy plants.\n",

                    "Do prunning \n" +
                            " Add Mulch to the soil \n" +
                            " use fungicide like Fungonil, Daconil to treat early blight",

                    "Pull out the infected plants.\n" +
                            "When late blight is detected in your region, consider a weekly prevetative spray using Actinovate",

                    "Prune leaves.\n" +
                            " Consider organic fungicide containing either copper or potassium bicarbonate.",

                    "Use overhead‑sprinkler irrigation  and use Miticides\n" +
                            "  Use bifenazate (Acramite): Group UN, derived from a soil bacterium",


                    "avoid over-fertilizing with nitrogen just incase it's being used\n" +
                            "Pruning suckers and older leaves \n" +
                            "Use chlorothalonil, mancozeb, and copper oxychloride\n",

                    "Remove affected plants or weeds.\n" +
                            "Use yellow sticky traps to monitor and control whiteflies\n" +
                            "Spray with suitable insecticides like Daconil",

                    "1. Control is mainly based on the use of virus-free seeds.\n" +
                            "2. It has no cure",

                    "its healthy, No treatment required",

                    "No Treatment available because it's not a tomato leaf",

            };
            prescription.setText(medication[maxPos]);
            //treatTomato.setText(medication[maxPos]);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SelectPicOption() {
        final String[] langs = {"Gallery", "Camera"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Select Option");
        mBuilder.setSingleChoiceItems(langs, -1, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int ofungi) {
                if (ofungi == 0) {
                    galleryPic();
                } else if (ofungi == 1) {
                    //cameraPic();
                    Intent in = new Intent(MainActivity.this, Camera.class);
                    startActivity(in);

                    //Intent cam = new Intent(MainActivity.this, Camera.class);
                    //startActivity(cam);


                }

                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mBuilder.show();
    }

    private void cameraPic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void galleryPic() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choose MRI Image"), CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            if (resultCode == RESULT_OK && data.getData() != null) {
                uri = data.getData();
                imageView.setImageURI(uri);

                try {
                    bitmapimg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data.getData() != null) {
                bitmapimg = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmapimg);

            }

        }
        //super.onActivityResult(requestCode, resultCode, data);
    }


    public void openMainActivity(View view) {
        Intent n = new Intent(MainActivity.this, TomatoeClinic.class);
    }


/*

    public boolean onOptionItemelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.clear) {
            return true;
        } else if (id == R.id.contact) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


}






package com.example.tomatoeapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.tumo20.ml.Final;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Camera extends AppCompatActivity {
    Button selectBtn,predictBtn,captureBtn,exitBtn;
    TextView resultTxt;
    ImageView imageView;
    Bitmap bitmap;
    int imageSize = 224;



    private Button classfy, treatTomato;
    private TextView confidencs, classifiedas, lifeSpan1, prescription;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        captureBtn=findViewById(R.id.capture);
        predictBtn=findViewById(R.id.predict);
        imageView=findViewById(R.id.img);
        exitBtn=findViewById(R.id.back);


        classfy = findViewById(R.id.buttonclassify);
        confidencs = findViewById(R.id.confidence);
        classifiedas = findViewById(R.id.classified);
        lifeSpan1 = findViewById(R.id.display);
        prescription = findViewById(R.id.drug);



        //permission
        getPermission();

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent revert=new Intent(Camera.this, MainActivity.class);
                startActivity(revert);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
            }
        });
        //predict button
        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Final model = Final.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    int[] intValues = new int[imageSize * imageSize];
                    bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
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
                    String[] medication = {" Remove symptomatic plants from the field or greenhouse to prevent the spread of bacteria to healthy plants.\n" +
                            "  Burn, bury or hot compost the affected plants and DO NOT eat symptomatic fruit.  Although bacterial spot pathogens are not human pathogens, the fruit blemishes that they cause can provide entry points for human pathogens that could cause illness.",

                            "PRUNNING: Quickly Remove the affected part or destroy any affected area  of the tomato plant\n" +
                                    " Add Mulch to the soil to prevent To prevent the spores spreading further through the air\n" +
                                    " FUNGICIDE: use  Fungonil, Daconil to treat early blight",

                            "Once a plant is infected, it must be destroyed.Pull up the plants and either seal them tightly in a trash bag, or secure them under black plastic, where the sun's heat can kill the spores.\n" +
                                    "When late blight is detected in your region, consider a weekly prevetative spray using Actinovate",

                            "Removing infected leaves Remove infected leaves immediately, and be sure to wash your hands and pruners thoroughly before working with uninfected plants.\n" +
                                   " Consider organic fungicide options. Fungicides containing either copper or potassium bicarbonate will help prevent the spreading of the disease. Begin spraying as soon as the first symptoms appear and follow the label directions for continued management.\n" +
                                    " Consider chemical fungicides. While chemical options are not ideal, they may be the only option for controlling advanced infections. One of the least toxic and most effective is chlorothalonil (sold under the names Fungonil and Daconil).",

                            "Use of overhead‑sprinkler irrigation may provide some short‑term relief of mite infestations and use Miticides\n" +
                                    "  Use bifenazate (Acramite): Group UN, a long residual nerve poison\n" +
                                    "use abamectin (Agri-Mek): Group 6, derived from a soil bacterium\n" +
                                    "use spirotetramat (Movento): Group 23, mainly affects immature stages\n" +
                                    "use spiromesifen (Oberon 2SC): Group 23, mainly affects immature stages\n",
                           " wider plant spacing and avoiding over-fertilizing with nitrogen just incase it's being used\n" +
                                    "Pruning suckers and older leaves in the lower canopy can also increase airflow and reduce leaf wetness\n" +
                                    "Destroy crop residues shortly after the final harvest\n" +
                                  "Products containing chlorothalonil, mancozeb, and copper oxychloride have been shown to provide good control of target spot\n",

                            "Remove affected plants\n" +
                                    "Keep the field free from weeds.\n" +
                                    "Use yellow sticky traps to monitor and control whiteflies\n" +
                                    "If the insect infestation is severe spray suitable insecticides. like Daconil",

                            "   1. Treating mosaic virus is difficult and there are no chemicl controls like there are for fungal diseases. Tomato mosaic virus has been found to survive for up to 50 years in desiccated plant detritus! So tomato mosaic virus control then leans less on eliminating the disease and more on reducing and eliminating the virus sources and insect infestations. Control is mainly based on the use of virus-free seeds.\n" +
                                    "   2. It has no cure",

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
////
//
//            }
              /*  try {
                    Model model = Model.newInstance(MainActivity.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                    //rescaling
                    bitmap=Bitmap.createScaledBitmap(bitmap, 224,224, true);

                    //error:java.lang.IllegalArgumentException: The size of byte buffer and the shape do not match.
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);

                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                   // resultTxt.setText(getMax(outputFeature0.getFloatArray())+"");

                    resultTxt.setText(labels[getMax(outputFeature0.getFloatArray())]+"");

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
                */


                //}
                //)


            }});
//    int getMax(float[] arr){
//        int max=0;
//        for(int i=0; i<arr.length; i++){
//            if(arr[1]>arr[max])
//                max=i;
//            }
//        return max;
//
//
    }
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Camera.this, new String[]{Manifest.permission.CAMERA}, 11);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 12) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}


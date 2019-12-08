package com.hybrid.dycesbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.hybrid.dycesbank.font.JosefinEdit;
import com.hybrid.meetobuy.font.JosefinSans;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PinActivity extends AppCompatActivity {

    JosefinEdit edpin;
    Button savepin;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        savepin = findViewById(R.id.savepin);
        edpin = findViewById(R.id.edpin);

        savepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pin = edpin.getText().toString();
                if(pin.isEmpty()) {
                    edpin.setError("Please enter pin ");
                    edpin.requestFocus();
                }else if(pin.length() <=3) {
                    edpin.setError("4 digit required");
                    edpin.requestFocus();
                }else if(pin.length() >4){
                    edpin.setError("4 digit required");
                    edpin.requestFocus();
                }else{

                             showWaitPop();
                    Map<String, Object> map = new HashMap<>();
                    map.put("Pin",pin);
                   firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(map, SetOptions.merge())
                  .addOnSuccessListener(new OnSuccessListener() {
                      @Override
                      public void onSuccess(Object o) {
                          Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                          showSuccessPop();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                           showFailurePop();
                       }
                   });
                }

            }
        });


    }

    private void showWaitPop() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.loadingpopup, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss();
                timer.cancel();
            }
        }, 1000);

    }

    private void showSuccessPop() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.success, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        dialogView.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToMainActivity();
            }
        });

        alertDialog.show();


    }


    private void showFailurePop() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.failure, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        dialogView.findViewById(R.id.buttoncanc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Animatoo.animateSlideLeft(this);
        finish();
    }


}

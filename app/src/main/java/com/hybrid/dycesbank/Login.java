package com.hybrid.dycesbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hybrid.dycesbank.font.JosefinEdit;
import com.hybrid.meetobuy.font.JosefinSans;

import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

    JosefinEdit lemail, lepass;
    JosefinSans forgot, loginext;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
   Button loginuser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
         lemail = findViewById(R.id.lemail);
         lepass = findViewById(R.id.lepass);
         loginext = findViewById(R.id.loginextpage);
         loginuser = findViewById(R.id.loginuser);

        loginext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(), SignUp.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                Animatoo.animateSlideLeft(getApplicationContext());

            }
        });

        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = lemail.getText().toString();
                final String password = lepass.getText().toString();

                if (email.isEmpty()){
                    lemail.setError("Please enter mail");
                    lemail.requestFocus();
                }else if(password.isEmpty()) {
                    lepass.setError("Please enter password");
                    lepass.requestFocus();
                }else{

                         showWaitPop();
                    firebaseAuth.signInWithEmailAndPassword(lemail.getText().toString(),lepass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                showSuccessPop();
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                            }else{
                                      showFailurePop();
                                String msg = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
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
        }, 2000);

    }

    private void showSuccessPop() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.success, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
      sendUserToMainActivity();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss();
                timer.cancel();
            }
        }, 1000);

    }


    private void showFailurePop() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.failure, viewGroup, false);
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

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Animatoo.animateSlideLeft(this);
        finish();
    }

}

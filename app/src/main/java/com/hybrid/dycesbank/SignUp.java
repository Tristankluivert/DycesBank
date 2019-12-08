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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hybrid.dycesbank.font.JosefinEdit;
import com.hybrid.meetobuy.font.JosefinSans;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SignUp extends AppCompatActivity {


    JosefinEdit edfirstname, edlastname, edmail, edpasword;
    Button reguser;
    FirebaseAuth firebaseAuth;
     FirebaseFirestore firestore;
     JosefinSans signext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        edfirstname = findViewById(R.id.edfirstname);
        edlastname = findViewById(R.id.edlastname);
        edmail = findViewById(R.id.edmail);
        edpasword = findViewById(R.id.edpassword);
        reguser = findViewById(R.id.reguser);
        signext  = findViewById(R.id.signextpage);

        signext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(), Login.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                Animatoo.animateSlideLeft(SignUp.this);

            }
        });

        reguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstname = edfirstname.getText().toString().trim();
                String lastname = edlastname.getText().toString().trim();
                String email = edmail.getText().toString();
                final String password = edpasword.getText().toString();

                if(firstname.isEmpty()){
                    edfirstname.setError("Please enter first name");
                    edfirstname.requestFocus();

                }else if(lastname.isEmpty()){
                    edlastname.setError("Please enter last name");
                    edlastname.requestFocus();
                }else if(email.isEmpty()){
                    edmail.setError("Please enter mail");
                    edmail.requestFocus();
                }else if(password.isEmpty()) {
                    edpasword.setError("Please enter password");
                    edpasword.requestFocus();

                }else if(password.length() <=5){
                    edpasword.setError("Minimum character is 6");
                    edpasword.requestFocus();
                }else{

                   showWaitPop();
                    firebaseAuth.createUserWithEmailAndPassword(edmail.getText().toString(), edpasword.getText().toString()).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        showSuccessPop();
                  //                      saveUser();
                                        Toast.makeText(getApplicationContext(),"Successfully registered",Toast.LENGTH_SHORT).show();
                                    }else {
                                        String msg = task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                    );

                }



            }
        });




    }

    public void saveUser(){


        String firstname = edfirstname.getText().toString().trim();
        String lastname = edlastname.getText().toString().trim();
        String email = edmail.getText().toString();

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("First name" , firstname);
        hashMap.put("Last name", lastname);
        hashMap.put("Email",email);

       firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
            .set(hashMap).addOnSuccessListener(new OnSuccessListener() {
           @Override
           public void onSuccess(Object o) {
               Toast.makeText(getApplicationContext(),"Data added",Toast.LENGTH_SHORT).show();
              sendUserToPinActivity();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
               showFailurePop();
           }
       });

    }


    private void sendUserToPinActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), PinActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Animatoo.animateSlideLeft(this);
        finish();
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Animatoo.animateSlideLeft(this);
        finish();
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
               saveUser();
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


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){
             sendUserToMainActivity();
        }
    }


}

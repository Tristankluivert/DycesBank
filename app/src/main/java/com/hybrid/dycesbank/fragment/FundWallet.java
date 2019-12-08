package com.hybrid.dycesbank.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.JetPlayer;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.hybrid.dycesbank.MainActivity;
import com.hybrid.dycesbank.R;
import com.hybrid.dycesbank.font.JosefinEdit;
import com.hybrid.meetobuy.font.JosefinSans;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FundWallet extends Fragment {


    public FundWallet() {
        // Required empty public constructor
    }

    JosefinEdit moneyfund, pinfund;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Button savefund;
    JosefinSans mono,username;
    DocumentReference documentReference1;
View viewGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fund_wallet, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
       viewGroup = view.findViewById(android.R.id.content);
              username = view.findViewById(R.id.username);
      DocumentReference documentReference = firestore.collection("Transactions").document(firebaseAuth.getCurrentUser().getUid());
       documentReference1 = firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        moneyfund = view.findViewById(R.id.moneyfund);
        pinfund = view.findViewById(R.id.pinfund);
        savefund = view.findViewById(R.id.savefund);
        mono = view.findViewById(R.id.mono);

        savefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveTransaction();
            }
        });

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    String money = documentSnapshot.getString("Money");

                     mono.setText("$ "+money);
                    //  Map<String, Object> map = documentSnapshot.getData();


                }else{
                    Toast.makeText(getContext(),"No deposits yet",Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });





        return view;
    }


    public void saveTransaction(){

        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    String pino = documentSnapshot.getString("Pin");
                    String name = documentSnapshot.getString("Last name");
                    String pin = pinfund.getText().toString();
                    username.setText("HI "+name);


                    if(!pin.equals(pino)){
                        Toast.makeText(getContext(),"Incorrect pin",Toast.LENGTH_SHORT).show();
                    }
               if (pin.isEmpty()){
                   Toast.makeText(getContext(),"Pin is empty",Toast.LENGTH_SHORT).show();
               }


                }else{


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        String money = moneyfund.getText().toString();
        String pin = pinfund.getText().toString();
       Integer doub = Integer.parseInt(moneyfund.getText().toString());

     //   double moni = Double.parseDouble(mono.getText().toString());

        if(money.isEmpty()){
            moneyfund.setError("Minimum is 100$");
            moneyfund.requestFocus();
        }else if(doub <= 99) {
            moneyfund.setError("Minimum is 100$");
            moneyfund.requestFocus();

        }else {

            showWaitPop();
            Map<String, Object> map = new HashMap<>();
            map.put("Money", money);
            firestore.collection("Transactions").document(firebaseAuth.getCurrentUser().getUid()).set(map, SetOptions.merge()).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showSuccess();
                            //sendUserToMainActivity();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showFail();
                }
            });

        }

    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Animatoo.animateSlideLeft(getActivity());

    }
    private void showWaitPop() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.loadingpopup, (ViewGroup) viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


    public void showFail(){
       View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.failure, (ViewGroup) viewGroup, false);
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setView(dialogView);
       final AlertDialog alertDialog = builder.create();
       dialogView.findViewById(R.id.buttoncanc).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertDialog.dismiss();
           }
       });
       alertDialog.show();
      //  sendUserToMainActivity();


   }

    public void showSuccess(){
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.success, (ViewGroup) viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);


        final AlertDialog alertDialog = builder.create();

        dialogView.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToMainActivity();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();



    }

}

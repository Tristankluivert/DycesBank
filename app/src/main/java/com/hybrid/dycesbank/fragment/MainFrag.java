package com.hybrid.dycesbank.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hybrid.dycesbank.R;
import com.hybrid.dycesbank.font.JosefinEdit;
import com.hybrid.meetobuy.font.JosefinSans;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFrag extends Fragment {


    public MainFrag() {
        // Required empty public constructor
    }

    FloatingActionButton button;
    JosefinSans edmoney;
    JosefinSans username;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    DocumentReference documentReference,documentReference1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_main, container, false);
          firebaseAuth = FirebaseAuth.getInstance();
          firestore = FirebaseFirestore.getInstance();
          edmoney = view.findViewById(R.id.edmoney);
          username = view.findViewById(R.id.username);

          documentReference = firestore.collection("Transactions").document(firebaseAuth.getCurrentUser().getUid());

        documentReference1 = firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
       button = view.findViewById(R.id.addstuffs);
        final NavController navController = NavHostFragment.findNavController(this);


       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                navController.navigate(R.id.addFunds);
           }
       });


   documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
       @Override
       public void onSuccess(DocumentSnapshot documentSnapshot) {
          if(documentSnapshot.exists()){
              String money = documentSnapshot.getString("Money");

              edmoney.setText("$ " + money);

          }else{
              Toast.makeText(getContext(),"No deposits yet",Toast.LENGTH_SHORT).show();
              edmoney.setText("$0.00 ");
          }
       }
   }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
           Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
       }
   });

        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    String name = documentSnapshot.getString("Last name");
                    //  Map<String, Object> map = documentSnapshot.getData();
                    username.setText("HI " + name);

                }else{
                    Toast.makeText(getContext(),"No name",Toast.LENGTH_SHORT).show();
                    edmoney.setText("dyces");
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

}

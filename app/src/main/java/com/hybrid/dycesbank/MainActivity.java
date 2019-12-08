package com.hybrid.dycesbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    NavController navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = Navigation.findNavController(this,R.id.reminfrag);
        NavigationUI.setupActionBarWithNavController(this,navigation);


    }

    @Override
    public boolean onSupportNavigateUp() {
        navigation.navigateUp();
        return super.onSupportNavigateUp();

    }


}

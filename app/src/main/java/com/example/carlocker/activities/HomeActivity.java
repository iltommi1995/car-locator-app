package com.example.carlocker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.carlocker.adapters.RecyclerViewAdapter;
import com.example.carlocker.database.AppDatabase;
import com.example.carlocker.models.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.carlocker.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

    private static View viewLayout;
    private static Context baseContext;

    private static AppDatabase db;
    private static List<Vehicle> vehicles;
    private static RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Gestisco lista veicoli
        viewLayout = findViewById(R.id.viewLayout2);
        baseContext = HomeActivity.this;


        // Creiamo il database
        if (savedInstanceState == null) {
            db = Room.databaseBuilder(this, AppDatabase.class, "vehicles_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            getValue();
        }

        // Riempio la lista
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(vehicles, HomeActivity.this, db, "tutti");
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


        // Gestisco onClick sul bottone
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateVehicleActivity.startCreateVehicleActivity(HomeActivity.this);
                startActivity(intent);
            }
        });
    }

    // Metodo per prendere i valori dentro al db
    private static void getValue() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        // Creiamo lista di Nota e prendiamo tutte le note da db
        vehicles = db.vehicleDao().getVehicleType(userEmail, "Moto");
        Log.d("IMPORTANTE GUARDA", vehicles.size()+"");
        // Aggiorno adapter
        recyclerView = viewLayout.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(vehicles, baseContext, db, "tutti");
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // Metodo per aggiornare la mainactivity dinamicamente, senza doverla chiudere
    public static void adapterNotifyAll() {
        // Pulisco la lista
        getValue();
    }

    public static void sendObject(Context context, int position)
    {
        context.startActivity(VehicleDetailActivity.getDetailsIntent(context, vehicles.get(position)));
    }

}
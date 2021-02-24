package com.example.carlocker.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.carlocker.activities.ui.allVehicles.AllVehiclesFragment;
import com.example.carlocker.database.AppDatabase;
import com.example.carlocker.models.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlocker.R;

public class VehicleDetailActivity extends AppCompatActivity {

    private Vehicle vehicle;
    private TextView name;
    private TextView type;
    private TextView coordinates;
    private ImageView img;
    private static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Room.databaseBuilder(this, AppDatabase.class, "vehicles_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        // Mostro il tasto indietro nella ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.nomeVeicolo);
        type = findViewById(R.id.type);
        coordinates = findViewById(R.id.coordinates);
        img = findViewById(R.id.imageView);
        vehicle = (Vehicle) getIntent().getSerializableExtra("vehicle");

        assert vehicle != null;
        name.setText(vehicle.getName());
        type.setText(vehicle.getType());
        coordinates.setText("Coordinate: " + vehicle.getLatitude() + " - " + vehicle.getLongitude());
        switch (vehicle.getType().toLowerCase())
        {
            case "automobile":
                img.setImageResource(R.drawable.ic_vehicle_car);
                break;
            case "bicicletta":
                img.setImageResource(R.drawable.ic_vehicle_bike);
                break;
            case "moto":
                img.setImageResource(R.drawable.ic_vehicle_motorbike);
                break;
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("Conferma la cancellazione", "Vuoi davvero cancellare la nota?", vehicle);
            }
        });
    }

    // Creiamo un metodo che restituisca un Intent per portare a questa Activity
    public static Intent getDetailsIntent(Context context, Vehicle vehicle)
    {
        Intent intent = new Intent(context, VehicleDetailActivity.class);
        // In questo intent non passiamo più delle variabili primitive e nemmeno oggetti di tipo
        // String. Passiamo una Nota.
        // Inviare oggetti in android è difficile, solitamente si fa con la classe Prsable, noi però
        // usiamo l'interfaccia seriezable, che ha però delle performance peggiori.
        // Quando si fanno app più complesse si usa Parsable
        intent.putExtra("vehicle", vehicle);
        return intent;
    }


    // Metodo per creare alert quando si clicca sul bottone cancella
    private void showAlert(String title, String message, Vehicle vehicle)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleDetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancello la nota
                        db.vehicleDao().delete(vehicle);
                        // Aggiorno la lista nel main
                        AllVehiclesFragment.adapterNotifyAll();
                        finish();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Chiudo l'alert
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
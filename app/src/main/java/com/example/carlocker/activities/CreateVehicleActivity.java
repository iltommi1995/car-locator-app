package com.example.carlocker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carlocker.R;
import com.example.carlocker.activities.ui.allVehicles.AllVehiclesFragment;
import com.example.carlocker.database.AppDatabase;
import com.example.carlocker.models.Vehicle;
import com.google.firebase.auth.FirebaseAuth;

public class CreateVehicleActivity extends AppCompatActivity {

    private static EditText name;
    private static Spinner typeSpinner;
    private static Button createBtn;
    private static FirebaseAuth mAuth;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);

        // Mostro il tasto indietro nella ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inizializzo mAuth
        mAuth = FirebaseAuth.getInstance();

        // Inizializzo i componenti
        name = findViewById(R.id.createName);
        typeSpinner = findViewById(R.id.type_spinner);
        createBtn = findViewById(R.id.createBtn);

        // Richiamo il database
        db = Room.databaseBuilder(this, AppDatabase.class, "vehicles_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        // Gestisco onClick sul bottone
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Devo controllare che il nome non sia vuoto.
                if (!name.getText().toString().isEmpty())
                {
                    // Ora devo creare il nuovo oggetto:
                    String latitude = "no";
                    String longitude = "no";
                    String userEmail = mAuth.getCurrentUser().getEmail();
                    String newName = name.getText().toString().trim();
                    String newType = String.valueOf(typeSpinner.getSelectedItem());

                    Vehicle vehicle = new Vehicle(newName, newType, longitude, latitude, userEmail);
                    db.vehicleDao().createVehicle(vehicle);

                    // Refresho la lista dei mezzi
                    AllVehiclesFragment.adapterNotifyAll();
                    // Chiudo l'activity in modo da tornare alla home
                    finish();
                }
                else
                {
                    Toast.makeText(CreateVehicleActivity.this, "Non puoi lasciare il nome vuoto!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Metodo per ritornare l'intent per creare questa activity
    public static Intent startCreateVehicleActivity(Context context) {
        Intent intent = new Intent(context, CreateVehicleActivity.class);
        return intent;
    }

    // Metodo per tornare indietro all'Activity precedente
    public boolean onOptionsItemSelected(){
        finish();
        return true;
    }
}
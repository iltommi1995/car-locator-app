package com.example.carlocker.activities.ui.allVehicles;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.carlocker.R;
import com.example.carlocker.activities.VehicleDetailActivity;
import com.example.carlocker.adapters.RecyclerViewAdapter;
import com.example.carlocker.database.AppDatabase;
import com.example.carlocker.models.Vehicle;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class AllVehiclesFragment extends Fragment {

    private static View viewLayout;
    private static Context baseContext;

    private static AppDatabase db;
    private static List<Vehicle> vehicles;
    private static RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Qui viene fatto l'inflate del layout che darò a questo fragment
        View root = inflater.inflate(R.layout.fragment_all_vehicles, container, false);

        // Gestisco lista veicoli
        viewLayout = root.findViewById(R.id.recyclerView);
        baseContext = getActivity();

        Log.d("QUESTO E' il CONTEXT:", viewLayout.toString());
        // Creiamo il database
        if (savedInstanceState == null) {
            db = Room.databaseBuilder(getActivity(), AppDatabase.class, "vehicles_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            getValue();
        }

        // Riempio la lista
        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(vehicles, getActivity(), db, "tutti");
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // Qui ritorno la view
        return root;
    }

    // Metodo per prendere i valori dentro al db
    private static void getValue() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();
        // Creiamo lista di Nota e prendiamo tutte le note da db
        vehicles = db.vehicleDao().getAllVehicles(userEmail);
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
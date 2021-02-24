package com.example.carlocker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carlocker.R;
import com.example.carlocker.activities.ui.allVehicles.AllVehiclesFragment;
import com.example.carlocker.activities.ui.bikes.BikesFragment;
import com.example.carlocker.activities.ui.cars.CarsFragment;
import com.example.carlocker.activities.ui.motorbikes.MotorbikesFragment;
import com.example.carlocker.database.AppDatabase;
import com.example.carlocker.models.Vehicle;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{

    List<Vehicle> vehicles;
    private String type;
    private Context context;
    private static AppDatabase db;

    public RecyclerViewAdapter(List<Vehicle> vehicles, Context context, AppDatabase db, String type) {
        this.vehicles = vehicles;
        this.context = context;
        this.db = db;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.name.setText(vehicle.getName());
        holder.type.setText(vehicle.getType());

        switch ( vehicle.getType().toLowerCase())
        {
            case "automobile":
                holder.img.setImageResource(R.drawable.ic_vehicle_car);
                break;
            case "bicicletta":
                holder.img.setImageResource(R.drawable.ic_vehicle_bike);
                break;
            case "moto":
                holder.img.setImageResource(R.drawable.ic_vehicle_motorbike);
                break;
        }

        // Onclick su pulsance cacella
        holder.deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert("Conferma la cancellazione", "Vuoi davvero cancellare la nota?", vehicle);
            }
        });

        // Onclick su elemento
        holder.touch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type.toLowerCase())
                {
                    case "tutti":
                        AllVehiclesFragment.sendObject(context, position);
                        notifyItemChanged(holder.getAdapterPosition());
                        break;
                    case "automobili":
                        CarsFragment.sendObject(context, position);
                        notifyItemChanged(holder.getAdapterPosition());
                        break;
                    case "moto":
                        MotorbikesFragment.sendObject(context, position);
                        notifyItemChanged(holder.getAdapterPosition());
                        break;
                    case "biciclette":
                        BikesFragment.sendObject(context, position);
                        notifyItemChanged(holder.getAdapterPosition());
                        break;
                }
                // DA QUI NASCE BUG DELLE COSE CHE SI MISCHIANO

            }
        });
    }

    // Metodo per creare alert quando si clicca sul bottone cancella
    private void showAlert(String title, String message, Vehicle vehicle)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
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

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView type;
        private ImageButton deleteFab;
        private ImageView img;
        private RelativeLayout touch_layout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            deleteFab = itemView.findViewById(R.id.deleteFab);
            img = itemView.findViewById(R.id.vehicleImage);


            touch_layout = itemView.findViewById(R.id.touch_layout);
        }
    }
}

package com.example.gestionmateriel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private Context context;
    private ArrayList<String> materialIds, materialName, materialDescription, serialNumber;
    Activity activity;

    public MaterialAdapter(Activity activity, Context context, ArrayList<String> materialIds, ArrayList<String> materialName,
                           ArrayList<String> materialDescription, ArrayList<String> serialNumber) {
        this.activity = activity;
        this.context = context;
        this.materialIds = materialIds;
        this.materialName = materialName;
        this.materialDescription = materialDescription;
        this.serialNumber = serialNumber;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.material_item, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        holder.materialName.setText(materialName.get(position));
        holder.materialDescription.setText(materialDescription.get(position));
        holder.serialNumber.setText(serialNumber.get(position));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", materialIds.get(position));
            intent.putExtra("name", materialName.get(position));
            intent.putExtra("description", materialDescription.get(position));
            intent.putExtra("serialNumber", serialNumber.get(position));
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return materialName.size();
    }

    public class MaterialViewHolder extends RecyclerView.ViewHolder {

        TextView materialName, materialDescription, serialNumber;

        public MaterialViewHolder(View itemView) {
            super(itemView);
            materialName = itemView.findViewById(R.id.material_name);
            materialDescription = itemView.findViewById(R.id.material_description);
            serialNumber = itemView.findViewById(R.id.serial_number);
        }
    }
}

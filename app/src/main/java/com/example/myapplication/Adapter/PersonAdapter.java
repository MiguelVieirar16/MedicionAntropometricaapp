package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Buscar;
import com.example.myapplication.InfoPersona;
import com.example.myapplication.R;
import com.example.myapplication.Registrar;
import com.example.myapplication.model.DatosUsuarios;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<DatosUsuarios> items;
    private static Context context;

    public PersonAdapter(List<DatosUsuarios> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, cedula, nacimiento, telefono;
        ImageView detele;
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textViewName);
            cedula = itemView.findViewById(R.id.textViewCedula);
            nacimiento = itemView.findViewById(R.id.textViewCumple);
            telefono = itemView.findViewById(R.id.textViewTelefono);
            detele = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   int position = getAdapterPosition();
                   Buscar.MostrarPersona(position, context);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return  items.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_person_single, viewGroup, false);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_cardsview);
        v.findViewById(R.id.CardPerson).startAnimation(animation);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.cedula.setText(items.get(i).getCedula());
        viewHolder.nacimiento.setText(items.get(i).getNacimiento());
        viewHolder.telefono.setText(items.get(i).getTelefono());

        viewHolder.detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar.Dialogo_Eliminar(items.get(i).getCedula() ,context, items.get(i).getUuid_hijos());
                //items.remove(i);
                //PersonAdapter.this.notifyItemRemoved(i);
            }
        });
    }
}

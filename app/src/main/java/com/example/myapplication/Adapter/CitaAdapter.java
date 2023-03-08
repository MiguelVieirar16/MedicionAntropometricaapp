package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Buscar;
import com.example.myapplication.InfoPersona;
import com.example.myapplication.NuevaCita;
import com.example.myapplication.R;
import com.example.myapplication.model.Citas;
import com.example.myapplication.model.DatosUsuarios;

import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.ViewHolder> {
    private List<Citas> items;
    private static Context context;

    public CitaAdapter(List<Citas> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fecha, peso, talla, edad;
        ImageView detele;
        public ViewHolder(View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.textViewFecha);
            peso = itemView.findViewById(R.id.peso_cita);
            talla = itemView.findViewById(R.id.talla_cita);
            edad = itemView.findViewById(R.id.edad_cita);
            detele = itemView.findViewById(R.id.delete_cita);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    InfoPersona.MostrarCitas(position, context);
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
                .inflate(R.layout.view_cita, viewGroup, false);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_cardsview);
        v.findViewById(R.id.cardCita).startAnimation(animation);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.fecha.setText(items.get(i).getFecha());
        viewHolder.peso.setText(items.get(i).getPeso());
        viewHolder.talla.setText(items.get(i).getTalla());
        viewHolder.edad.setText(items.get(i).getEdad());

        viewHolder.detele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoPersona.Dialogo_Eliminar(context, items.get(i).getUuid(), i);
                //items.remove(i);
                //CitaAdapter.this.notifyItemRemoved(i);
            }
        });

    }

}
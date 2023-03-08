package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;
import static com.google.firebase.firestore.Query.Direction.ASCENDING;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.Html;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.CitaAdapter;
import com.example.myapplication.Adapter.PersonAdapter;
import com.example.myapplication.model.Citas;
import com.example.myapplication.model.DatosUsuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InfoPersona extends AppCompatActivity {

    private static Context context;
    private static DatosUsuarios datosUsuarios;
    private Citas citas;
    private static boolean statecheck;
    private static FirebaseFirestore db;
    private RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ProgressBar progressBar;
    private TextView nombre;
    private TextView cedula;
    private TextView telefono;
    private TextView nacimiento;
    private TextView correo;
    private TextView direccion;
    private TextView sexo;
    private TextView colegio;
    private TextView nombre_papa;
    private TextView trabajo;
    private FloatingActionButton fab;
    private View view;
    static List<Citas> items = new ArrayList<>();

    public InfoPersona(Context context){
        this.context = context;
    }

    public InfoPersona(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_persona);

        lManager = new LinearLayoutManager(this);
        adapter = new CitaAdapter(items,this);

        // recycler.setItemAnimator(new DefaultItemAnimator());
        recycler = findViewById(R.id.recyclerView);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);

        nombre = findViewById(R.id.textViewName);
        cedula = findViewById(R.id.textViewCedula);
        telefono = findViewById(R.id.textViewTelefono);
        nacimiento = findViewById(R.id.textViewCumple);
        correo = findViewById(R.id.textViewCorreo);
        direccion = findViewById(R.id.textViewDirrecion);
        sexo = findViewById(R.id.textViewSexo);
        colegio = findViewById(R.id.textViewColegio);
        nombre_papa = findViewById(R.id.textViewNombre_papa);
        trabajo = findViewById(R.id.textViewPapa_trabajo);

        fab = findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_bar6);

        datosUsuarios = (DatosUsuarios) getIntent().getExtras().getSerializable("datos");
        statecheck = (boolean) getIntent().getExtras().getSerializable("statecheck");

        cedula.setText(datosUsuarios.getCedula());
        nombre.setText(datosUsuarios.getNombre());
        correo.setText(datosUsuarios.getCorreo());
        direccion.setText(datosUsuarios.getDireccion());
        colegio.setText(datosUsuarios.getColegio());
        telefono.setText(datosUsuarios.getTelefono());
        nacimiento.setText(datosUsuarios.getNacimiento());
        trabajo.setText(datosUsuarios.getTrabajo());
        nombre_papa.setText(datosUsuarios.getNombre_papa());
        sexo.setText(datosUsuarios.getSexo());

        //items.clear();
        try{
            if(!items.isEmpty()){
                items.clear();
                adapter.notifyDataSetChanged();
            }
            MostrarCitas("fecha", DESCENDING);
        }catch (Exception e){}

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MandarDatos();
                //items.clear();
            }
        });
    }

    private void MostrarCitas(String filtro, Query.Direction orden){
        progressBar.setVisibility(View.VISIBLE);
        if (statecheck) {
            db.collection("Usuarios").document(datosUsuarios.getCedula())
                    .collection("Citas")
                    .orderBy(filtro, orden)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Citas citas = document.toObject(Citas.class);
                                    items.add(new Citas(citas.getEdad(), citas.getPeso(), citas.getTalla(), citas.getCircunferencia_brazo(), citas.getImc(),
                                            citas.getEstatura(), citas.getGanancia_peso(), citas.getFecha(), document.getId(), citas.isEmbarazada(), citas.getSemanas()));
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                //swipeRefreshLayout.setRefreshing(false);
                                if (!items.isEmpty()) {
                                    //recycler.setItemAnimator(new DefaultItemAnimator());
                                    recycler.setHasFixedSize(true);
                                    // Usar un administrador para LinearLayout
                                    recycler.setLayoutManager(lManager);
                                    // Crear un nuevo adaptador
                                    recycler.setAdapter(adapter);
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                //snack.SnackMessage("Error obteniendo Ordenes de compra", R.color.error);
                            }
                        }
                    });
        }else{
            db.collection("UsuariosPadres").document(datosUsuarios.getCedula())
                    .collection("hijos")
                    .document(datosUsuarios.getUuid_hijos())
                    .collection("Citas")
                    //Para poder ordenar por fecha de mas actual a mas antigua
                    .orderBy(filtro, orden)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    citas = document.toObject(Citas.class);
                                    items.add(new Citas(citas.getEdad(), citas.getPeso(), citas.getTalla(), citas.getCircunferencia_brazo(), citas.getImc(),
                                            citas.getEstatura(), citas.getGanancia_peso(), citas.getFecha(), document.getId(), citas.isEmbarazada(), citas.getSemanas()));
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                //swipeRefreshLayout.setRefreshing(false);
                                if (!items.isEmpty()) {
                                    //recycler.setItemAnimator(new DefaultItemAnimator());
                                    recycler.setHasFixedSize(true);
                                    // Usar un administrador para LinearLayout
                                    recycler.setLayoutManager(lManager);
                                    // Crear un nuevo adaptador
                                    recycler.setAdapter(adapter);
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                //snack.SnackMessage("Error obteniendo Ordenes de compra", R.color.error);
                            }
                        }
                    });
        }
    }

    public void MandarDatos(){
        Intent intent = new Intent(getApplicationContext(), NuevaCita.class);
        intent.putExtra("datos", datosUsuarios);
        intent.putExtra("statecheck", statecheck);
        startActivity(intent);
    }

    public static void MostrarCitas(int position, Context context){
        String peso = items.get(position).getPeso();
        String edad = items.get(position).getEdad();
        String talla = items.get(position).getTalla();
        String circunferencia_brazo = items.get(position).getCircunferencia_brazo();
        String imc = items.get(position).getImc();
        String ganancia_peso = items.get(position).getGanancia_peso();
        String uuid = items.get(position).getUuid();
        Boolean embarazada = items.get(position).isEmbarazada();
        String fecha = items.get(position).getFecha();
        String estatura = items.get(position).getEstatura();
        String semanas = items.get(position).getSemanas();

        Citas citas = new Citas(edad, peso, talla, circunferencia_brazo, imc,
                estatura, ganancia_peso, fecha, uuid, embarazada, semanas);
        Intent intent = new Intent(context, ResumenCita.class);
        intent.putExtra("datos", datosUsuarios);
        intent.putExtra("citas", citas);
        intent.putExtra("statecheck", statecheck);
        context.startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(statecheck){
            Intent intent = new Intent(getApplicationContext(), Buscar.class);
            intent.putExtra("cedula", datosUsuarios.getCedula());
            intent.putExtra("stateCheck", statecheck);
            intent.putExtra("control",false);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), Buscar.class);
            intent.putExtra("cedula", datosUsuarios.getCedula());
            intent.putExtra("stateCheck", statecheck);
            intent.putExtra("control", true);
            startActivity(intent);
        }
    }

    public static void EliminarCita(Context context, String uuid){
        Activity activity = (Activity) context;
        if(statecheck){
            db.collection("Usuarios").document(datosUsuarios.getCedula())
                    .collection("Citas")
                    .document(uuid)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Dialogo(activity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Toast.makeText(activity, "Error al eliminar cita", LENGTH_SHORT).show();
                        }
                    });
        }else {
            db.collection("UsuariosPadres").document(datosUsuarios.getCedula())
                    .collection("hijos").document(datosUsuarios.getUuid_hijos())
                    .collection("Citas")
                    .document(uuid)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Dialogo(activity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Toast.makeText(activity, "Error al eliminar historia", LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static void Dialogo_Eliminar(Context context, String uuid_hijos, int i){
        Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(Html.fromHtml("<big>Está seguro que desea eliminar la historia?</big>"))
                .setPositiveButton(Html.fromHtml("<big>Sí</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EliminarCita(activity, uuid_hijos);
                    }
                }).setNegativeButton(Html.fromHtml("<big>No</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private static void Dialogo(Context context){
        Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(Html.fromHtml("<big>La historia fue eliminada correctamente!</big>"))
                .setPositiveButton(Html.fromHtml("<big>Aceptar</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //items.remove(i);
                        //adapter.notifyItemRemoved(i);
                        Intent intent = new Intent(context, InfoPersona.class);
                        intent.putExtra("datos", datosUsuarios);
                        intent.putExtra("statecheck", statecheck);
                        context.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}




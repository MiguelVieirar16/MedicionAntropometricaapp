package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.Adapter.PersonAdapter;
import com.example.myapplication.model.DatosUsuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Buscar extends AppCompatActivity {

    private static FirebaseFirestore db;
    private RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ProgressBar progressBar;
    private TextInputLayout il_buscarCedula;
    private Button bt_buscarCedula;
    private CheckBox checkboxBuscar;
    private static DatosUsuarios datosUsuarios;
    private static Context context;
    static List<DatosUsuarios> items = new ArrayList<>();
    private static boolean statecheck;
    private static String uuid_hijos = null;

    public Buscar(Context context){
        this.context = context;
    }

    public Buscar(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        lManager = new LinearLayoutManager(this);
        adapter = new PersonAdapter(items,this);

     // recycler.setItemAnimator(new DefaultItemAnimator());
        recycler = findViewById(R.id.recyclerView);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);

        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_bar6);

        il_buscarCedula = findViewById(R.id.il_buscarCedula);
        bt_buscarCedula = findViewById(R.id.bt_buscarCedula);
        checkboxBuscar = findViewById(R.id.checkboxBuscar);

        try{
            if(!items.isEmpty()){
            items.clear();
            adapter.notifyDataSetChanged();
            }
        }catch (Exception e){}

        bt_buscarCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarUsuario();
                items.clear();
            }
        });
    }

    public void BuscarUsuario(){
        progressBar.setVisibility(View.VISIBLE);
        String identificacion = il_buscarCedula.getEditText().getText().toString().trim();
        if(identificacion.isEmpty()){
            Toast.makeText(this,"Introduzca una cedula", LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            if(checkboxBuscar.isChecked()){
                db.collection("Usuarios").document(identificacion)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        datosUsuarios = document.toObject(DatosUsuarios.class);
                                        items.add(new DatosUsuarios(datosUsuarios.getCedula(), datosUsuarios.getNombre(),datosUsuarios.getColegio(),
                                                datosUsuarios.getCorreo(), datosUsuarios.getNombre_papa(), datosUsuarios.getDireccion(), datosUsuarios.getTelefono(),
                                                datosUsuarios.getNacimiento(), datosUsuarios.getTrabajo(), datosUsuarios.getSexo(), datosUsuarios.getUuid_hijos()));
                                        progressBar.setVisibility(View.INVISIBLE);
                                        statecheck = true;
                                    //swipeRefreshLayout.setRefreshing(false);
                                        if(!items.isEmpty()) {
                                            //recycler.setItemAnimator(new DefaultItemAnimator());
                                            recycler.setHasFixedSize(true);
                                            // Usar un administrador para LinearLayout
                                            recycler.setLayoutManager(lManager);
                                            // Crear un nuevo adaptador
                                            recycler.setAdapter(adapter);
                                        } else {
                                            Error();
                                        }
                                    }
                                    else {
                                        Error();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),"Error de conexion", LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                } else{
                    //CUANDO NO ESTA PRESIONADO
                db.collection("UsuariosPadres").document(identificacion)
                        .collection("hijos")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        datosUsuarios = document.toObject(DatosUsuarios.class);
                                        uuid_hijos= document.getId();
                                        items.add(new DatosUsuarios(datosUsuarios.getCedula(), datosUsuarios.getNombre(),datosUsuarios.getColegio(),
                                                datosUsuarios.getCorreo(), datosUsuarios.getNombre_papa(), datosUsuarios.getDireccion(), datosUsuarios.getTelefono(),
                                                datosUsuarios.getNacimiento(), datosUsuarios.getTrabajo(), datosUsuarios.getSexo(), datosUsuarios.getUuid_hijos()));
                                    }
                                    statecheck = false;
                                    progressBar.setVisibility(View.INVISIBLE);
                                    //swipeRefreshLayout.setRefreshing(false);
                                    if(!items.isEmpty()) {
                                        recycler.setItemAnimator(new DefaultItemAnimator());
                                        recycler.setHasFixedSize(true);
                                        // Usar un administrador para LinearLayout
                                        recycler.setLayoutManager(lManager);
                                        // Crear un nuevo adaptador
                                        recycler.setAdapter(adapter);
                                    }else{
                                        Error();
                                    }
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(),"Error de Obteniendo usuarios", LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void Error(){
        il_buscarCedula.setError(getString(R.string.di_invalido));
        progressBar.setVisibility(View.INVISIBLE);
    }

    public static void MostrarPersona(int position, Context context){
        String cedula = items.get(position).getCedula();
        String nombre = items.get(position).getNombre();
        String correo = items.get(position).getCorreo();
        String direccion = items.get(position).getDireccion();
        String colegio = items.get(position).getColegio();
        String telefono = items.get(position).getTelefono();
        String nacimiento = items.get(position).getNacimiento();
        String trabajo = items.get(position).getTrabajo();
        String nombre_papa = items.get(position).getNombre_papa();
        String sexo = items.get(position).getSexo();
        String uuid_hijos = items.get(position).getUuid_hijos();

        DatosUsuarios datosUsuarios = new DatosUsuarios(cedula, nombre, colegio,  correo, nombre_papa, direccion,
                telefono, nacimiento, trabajo, sexo,uuid_hijos);
        Intent intent = new Intent(context, InfoPersona.class);
        intent.putExtra("datos", datosUsuarios);
        intent.putExtra("statecheck", statecheck);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public static void EliminarPersona(String cedula,Context context, String uuid_hijos){
        Activity activity = (Activity) context;
        if(statecheck){
            db.collection("Usuarios").document(cedula)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Dialogo(activity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Error al eliminar usuario", LENGTH_SHORT).show();
                        }
                    });
        }else{
            db.collection("UsuariosPadres").document(cedula)
                    .collection("hijos").document(uuid_hijos)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Dialogo(activity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Error en eliminar usuario", LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public static void Dialogo_Eliminar(String cedula,Context context, String uuid_hijos){
        Activity activity = (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(Html.fromHtml("<big>¿Estás seguro que deseas eliminar el ususario?</big>"))
                .setMessage(Html.fromHtml("<big><br>Asegurese de haber eliminado todas las citas de este usuario previamente.</br></big>"))
                .setPositiveButton(Html.fromHtml("<big>Si</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EliminarPersona(cedula, activity, uuid_hijos);
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
        builder.setTitle(Html.fromHtml("<big>El usuario fue eliminado correctamente!</big>"))
                .setPositiveButton(Html.fromHtml("<big>Aceptar</big>")
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // items.remove(i);
                       // adapter.notifyItemRemoved(i);
                        Intent intent = new Intent(context, Buscar.class);
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
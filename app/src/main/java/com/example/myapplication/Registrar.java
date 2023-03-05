package com.example.myapplication;

import static android.widget.Toast.*;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Dialogs.Snack;
import com.example.myapplication.model.DatosUsuarios;
import com.example.myapplication.model.ValidarCampos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Registrar extends AppCompatActivity {
    private FirebaseFirestore db;
    private Spinner spinner_telf, spinner_mes, spinner_sexo;
    private TextInputLayout il_name, il_cedula, il_correor, il_telefonor, il_direccionr,
            il_colegio, il_dia,  il_año, il_papa, il_trabajo;
    private Button bt_enviar;
    private String nombre, cedula, correo, direccion, mes, sexo, telefono, nombre_papa, colegio, trabajo, codigo;
    private int dia, año;
    private Snack snack;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        spinner_mes = findViewById(R.id.spinner_mes);
        spinner_telf = findViewById(R.id.spinner_telf);
        spinner_sexo = findViewById(R.id.spinner_sexo);
        checkBox = findViewById(R.id.checkBox);
        snack = new Snack(this, view);

        il_cedula = findViewById(R.id.il_cedula);
        il_name = findViewById(R.id.il_name);
        il_dia = findViewById(R.id.il_dia);
        il_año = findViewById(R.id.il_año);
        il_correor = findViewById(R.id.il_correor);
        il_telefonor = findViewById(R.id.il_telefonor);
        il_direccionr = findViewById(R.id.il_direccionr);
        il_colegio = findViewById(R.id.il_colegio);
        il_papa = findViewById(R.id.il_papa);
        il_trabajo = findViewById(R.id.il_trabajo);

        bt_enviar = findViewById(R.id.bt_enviar);

        progressBar = findViewById(R.id.progress_bar);

        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sexo, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sexo.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.meses, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mes.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.numerotelef, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_telf.setAdapter(adapter3);


        bt_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarUsuario();
            }
        });
    }



    private void Dialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Registrar.this);
        builder.setTitle(Html.fromHtml("<big> El usuario fue creado, desea:  </big>"))
                .setPositiveButton(Html.fromHtml("<big> Ir al inicio  </big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                }).setNegativeButton(Html.fromHtml("<big> Ir a guardar una historia </big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(),Buscar.class);
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    private void validarSiTieneCedula(){

    }

    private void AgregarUsuario(){
        if(ValidarCamposInput()) {
            final String numerotelefonico, nacimiento;
            numerotelefonico = codigo.trim().concat(telefono);
            nacimiento = dia + "/" + mes + "/" + año;
            progressBar.setVisibility(View.VISIBLE);
            if(checkBox.isChecked()) {
                DocumentReference docRef = db.collection("Usuarios").document(cedula);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                il_cedula.setError(getString(R.string.usuario_registrado));
                                //snack.SnackMessage(getString(R.string.usuario_registrado), R.color.error);
                            } else {
                                il_cedula.setError(null);
                                DatosUsuarios datosUsuario = new DatosUsuarios(cedula, nombre, colegio, correo, nombre_papa, direccion,
                                        numerotelefonico, nacimiento, trabajo, sexo, null);
                                db.collection("Usuarios").document(cedula).set(datosUsuario)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //snack.SnackMessage("Usuario creado correctamente", R.color.good);
                                                // Dialogo.Dialogos(getActivity(),"Creación de usuario","¡Creación de usuario completada!\n" +
                                                //                 "\n"+
                                                //                 "Para iniciar sesión, utilice su documento de identidad \n" +cedula.trim().concat(cedula),
                                                //         "Aceptar",null, 2);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Dialogo();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //    snack.SnackMessage("No se pudo crear el usuario", R.color.error);
                                            }
                                        });
                            }
                        } else {
                           // snack.SnackMessage(getString(R.string.fallo_conexion), R.color.error);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                return;
            }else {
                final String uuid = UUID.randomUUID().toString().substring(0,UUID.randomUUID().toString().indexOf('-'));
                DocumentReference docRef = db.collection("UsuariosPadres").document(cedula);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                il_cedula.setError(getString(R.string.usuario_registrado));
                                //    snack.SnackMessage(getString(R.string.usuario_registrado), R.color.error);
                                il_cedula.setError(null);
                                Map<String, Object> user = new HashMap<>();
                                user.put("first", "Ada");
                                db.collection("UsuariosPadres").document(cedula)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                             // snack.SnackMessage("Usuario creado correctamente", R.color.good);
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //snack.SnackMessage("No se pudo crear el usuario", R.color.error);
                                        }
                                    });
                                DatosUsuarios datosUsuario = new DatosUsuarios(cedula, nombre, colegio, correo, nombre_papa, direccion,
                                    numerotelefonico, nacimiento, trabajo, sexo,uuid);
                                db.collection("UsuariosPadres").document(cedula)
                                        .collection("hijos")
                                        .document(uuid)
                                        .set(datosUsuario)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                               // snack.SnackMessage("Usuario creado correctamente", R.color.good);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Dialogo();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //    snack.SnacktMessage("No se pudo crear el usuario", R.color.error);
                                            }
                                        });

                        } else {
                            //  snack.SnackMessage(getString(R.string.fallo_conexion), R.color.error);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                return;
            }
        }
    }


    private void ObtenerDatos() {
        cedula = il_cedula.getEditText().getText().toString().trim();
        nombre = il_name.getEditText().getText().toString();
        correo = il_correor.getEditText().getText().toString().trim();
        direccion = il_direccionr.getEditText().getText().toString();
        colegio = il_colegio.getEditText().getText().toString();
        nombre_papa = il_papa.getEditText().getText().toString();
        trabajo = il_trabajo.getEditText().getText().toString();
        telefono = il_telefonor.getEditText().getText().toString();

        codigo = spinner_telf.getSelectedItem().toString();
        mes = spinner_mes.getSelectedItem().toString();
        sexo = spinner_sexo.getSelectedItem().toString();
    }

        private boolean ValidarCamposInput(){
            int var= 0;
            ObtenerDatos();
            ValidarCampos validar = new ValidarCampos(this);
            //----------------------------Validar Cedula o RIF------------------------------------------

                if (validar.ValidarID_CI(cedula).equals(getString(R.string.trues))) {
                    il_cedula.setError(null);
                    var++;
                }else if (validar.ValidarID_CI(cedula).equals(getString(R.string.empty))){
                    il_cedula.setError(getString(R.string.empty));
                }else if(validar.ValidarID_CI(cedula).equals(getString(R.string.ci_invalida))){
                    il_cedula.setError(getString(R.string.ci_invalida));
                }

            //----------------------------Validar nombre------------------------------------------

            if(validar.ValidarNombreApellido(nombre).equals(getString(R.string.trues))){
                il_name.setError(null);
                var++;
            }else if(validar.ValidarNombreApellido(nombre).equals(getString(R.string.nombre_valido))){
                il_name.setError(getString(R.string.nombre_valido));
            } else if(validar.ValidarNombreApellido(nombre).equals(getString(R.string.empty))){
                il_name.setError(getString(R.string.empty));
            }
            //----------------------------Validar correo------------------------------------------
            if(validar.ValidarEmail(correo).equals(getString(R.string.trues))){
                il_correor.setError(null);
                var++;
            }else if (validar.ValidarEmail(correo).equals(getString(R.string.empty))){
                il_correor.setError(getString(R.string.empty));
            }else if (validar.ValidarEmail(correo).equals(getString(R.string.correo_invalido))){
                il_correor.setError(getString(R.string.correo_invalido));
            }

            //----------------------------Validar direccion------------------------------------------
            if(validar.ValidarDireccion(direccion).equals(getString(R.string.trues))){
                il_direccionr.setError(null);
                var++;
            }else if (validar.ValidarDireccion(direccion).equals(getString(R.string.empty))){
                il_direccionr.setError(getString(R.string.empty));
            }

            //----------------------------Validar colegio------------------------------------------
            if(validar.ValidarColegio(colegio).equals(getString(R.string.trues))){
                il_colegio.setError(null);
                var++;
            }else if (validar.ValidarColegio(colegio).equals(getString(R.string.empty))){
                il_colegio.setError(getString(R.string.empty));
            }
            //----------------------------Validar fecha------------------------------------------
            if(il_dia.getEditText().getText().toString().isEmpty() || il_año.getEditText().getText().toString().isEmpty() ) {
                il_dia.setError(getString(R.string.empty));
                il_año.setError(getString(R.string.empty));
            }else{
                dia = Integer.parseInt(il_dia.getEditText().getText().toString());
                año = Integer.parseInt(il_año.getEditText().getText().toString());
                if (validar.ValidarFecha(dia, mes, año)) {
                    var++;
                    il_dia.setError(null);
                    il_año.setError(null);
                } else {
                    il_dia.setError(getString(R.string.fecha_invalida));
                    il_año.setError(getString(R.string.fecha_invalida));
                }
            }

            //----------------------------Validar nombre del papa------------------------------------------
            if(validar.ValidarNombre_papa(nombre_papa).equals(getString(R.string.trues))){
                il_papa.setError(null);
                var++;
            }else if(validar.ValidarNombre_papa(nombre_papa).equals(getString(R.string.nombre_valido))){
                il_papa.setError(getString(R.string.nombre_valido));
            } else if(validar.ValidarNombre_papa(nombre_papa).equals(getString(R.string.empty))){
                il_papa.setError(getString(R.string.empty));
            }

            //----------------------------Validar trabajo------------------------------------------
            if(validar.ValidarTrabajo(trabajo).equals(getString(R.string.trues))){
                il_trabajo.setError(null);
                var++;
            }else if (validar.ValidarTrabajo(trabajo).equals(getString(R.string.empty))){
                il_trabajo.setError(getString(R.string.empty));
            }

            //----------------------------Validar telefono------------------------------------------
            if(validar.ValidarTelefono(telefono).equals(getString(R.string.trues))){
                il_telefonor.setError(null);
                var++;
            }else if (validar.ValidarTelefono(telefono).equals(getString(R.string.empty))){
                il_telefonor.setError(getString(R.string.empty));
            }
            else if (validar.ValidarTelefono(telefono).equals(getString(R.string.telefono_invalido))) {
                il_telefonor.setError(getString(R.string.telefono_invalido));
            }
            if(var==9) {
                return true;
            }else{
                return false;
            }
        }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}


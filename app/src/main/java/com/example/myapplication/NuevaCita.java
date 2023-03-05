package com.example.myapplication;

import static android.app.ProgressDialog.show;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Fragments.RangoCincoEmbarazadasFragment;
import com.example.myapplication.Fragments.RangoCincoFragment;
import com.example.myapplication.Fragments.RangoCuatroEmbarazadasFragment;
import com.example.myapplication.Fragments.RangoCuatroFragment;
import com.example.myapplication.Fragments.RangoDosFragment;
import com.example.myapplication.Fragments.RangoTresFragment;
import com.example.myapplication.Fragments.RangoUnoFragment;
import com.example.myapplication.model.Citas;
import com.example.myapplication.model.DatosUsuarios;
import com.example.myapplication.model.ValidarCampos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class NuevaCita extends AppCompatActivity {

    private static DatosUsuarios datosUsuarios;
    private FirebaseFirestore db;
    private Citas citas;
    private static boolean statecheck;
    private TextInputEditText sexo;
    private TextInputEditText edad_et;
    private static TextInputLayout il_talla;
    private static TextInputLayout il_peso;
    private static String peso;
    private static String talla;
    private int sexoCond;
    private TextView nombre;
    private Button bt_mandar;
    private ProgressBar progressBar;
    private static Context context;
    static List<DatosUsuarios> items = new ArrayList<>();
    static List<Citas> objetos = new ArrayList<>();
    private String cadena;
    private Bundle bundle = new Bundle();
    private FragmentManager fragmentManager;
    private boolean is_year;
    private int edad_temp;
    private int number_fragment=0;
    private String ganancia_peso, circunferencia_brazo, edad;
    private String estatura;
    private String semanas;
    private boolean embarazada = false;
    private String uuid_hijos;
    private float imc = 0;

    public NuevaCita(Context context){
        this.context = context;
    }

    public NuevaCita(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);

        sexo = findViewById(R.id.et_sexo);
        sexo.setFocusable(false);
        edad_et = findViewById(R.id.et_edad);
        edad_et.setFocusable(false);
        nombre = findViewById(R.id.textView4);

        il_talla = findViewById(R.id.il_talla);
        il_peso = findViewById(R.id.il_peso);
        bt_mandar = findViewById(R.id.bt_mandar);
        progressBar = findViewById(R.id.progress_bar);
        db = FirebaseFirestore.getInstance();

        datosUsuarios = (DatosUsuarios) getIntent().getExtras().getSerializable("datos");
        statecheck = (boolean) getIntent().getExtras().getSerializable("statecheck");

        sexo.setText(datosUsuarios.getSexo());
        nombre.setText(datosUsuarios.getNombre());

        cadena = datosUsuarios.getNacimiento().substring(0,datosUsuarios.getNacimiento().indexOf('/'));
        String dia = cadena;
        cadena = datosUsuarios.getNacimiento().substring(datosUsuarios.getNacimiento().indexOf('/') + 1);
        String mes = cadena.substring(0,cadena.indexOf('/'));
        String mes_conv = convertirMes(mes);
        cadena = cadena.substring(cadena.indexOf('/')+1);
        String año = cadena;
        edad = calcularEdad(Integer.parseInt(año), Integer.parseInt(mes_conv),Integer.parseInt(dia));
        edad_et.setText(edad);

        bundle.putSerializable("datos", datosUsuarios);
        bundle.putSerializable("statecheck",statecheck);

        fragmentManager = getSupportFragmentManager();

        sexoCond = convertirSexo(datosUsuarios.getSexo());

        MostrarFragment();

        bt_mandar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CapturarDatosFragment();
            }
        });
    }

    private void CapturarDatosFragment(){
        switch (number_fragment){
            case    1:
            case    3:
                if(ValidarCamposInput()){
                    RegistrarBD();
                }
                break;
            case    2:
                circunferencia_brazo = RangoDosFragment.Circunferencia();
                if(ValidarCamposInput() && !circunferencia_brazo.equals("null")){
                    RegistrarBD();
                }
                break;
            case    4:
                estatura = RangoCuatroFragment.Estatura();
                if(ValidarCamposInput() && !estatura.equals("null")){
                    RegistrarBD();
                }
                break;
            case    6:
                estatura = RangoCuatroEmbarazadasFragment.Estatura();
                embarazada = RangoCuatroEmbarazadasFragment.Embarazada();
                circunferencia_brazo = RangoCuatroEmbarazadasFragment.Circunferencia();
                ganancia_peso = RangoCuatroEmbarazadasFragment.GananciaPeso();
                semanas = RangoCuatroEmbarazadasFragment.Semanas();
                if(ValidarCamposInput() && !estatura.equals("null") ){
                    if (embarazada) {
                        if(!ganancia_peso.equals("null") && !semanas.equals("null") && !circunferencia_brazo.equals("null")) {
                            RegistrarBD();
                        }
                    }else{
                        RegistrarBD();
                    }
                }
                break;
            case    5:
                estatura = RangoCincoFragment.Estatura();
                circunferencia_brazo = RangoCincoFragment.Circunferencia();
                if(ValidarCamposInput() && !estatura.equals("null") && !circunferencia_brazo.equals("null")){
                    RegistrarBD();
                }
                break;
            case    7:
                estatura = RangoCincoEmbarazadasFragment.Estatura();
                embarazada = RangoCincoEmbarazadasFragment.Embarazada();
                circunferencia_brazo = RangoCincoEmbarazadasFragment.Circunferencia();
                ganancia_peso = RangoCincoEmbarazadasFragment.GananciaPeso();
                semanas = RangoCincoEmbarazadasFragment.Semanas();
                if(ValidarCamposInput() && !estatura.equals("null")){
                    if (embarazada) {
                        if(!ganancia_peso.equals("null") && !semanas.equals("null") && !circunferencia_brazo.equals("null")) {
                            RegistrarBD();
                        }
                    }else{
                        RegistrarBD();
                    }
                }
                break;
        }
    }

    private void RegistrarBD(){
        progressBar.setVisibility(View.VISIBLE);
        final String uuid = UUID.randomUUID().toString().substring(0,UUID.randomUUID().toString().indexOf('-'));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = simpleDateFormat.format(new Date());

        if(is_year){
            if(edad_temp >= 10){
                float estatura_float = Float.parseFloat(estatura);
                float peso_float = Float.parseFloat(peso);
                imc = peso_float / (estatura_float * estatura_float);
            }
        }

        if(statecheck) {
            citas = new Citas(edad, peso, talla, circunferencia_brazo, String.format("%.2f", imc),
                    estatura, ganancia_peso, fecha, uuid, embarazada, semanas);
            db.collection("Usuarios").document(datosUsuarios.getCedula())
                            .collection("Citas").document(uuid)
                    .set(citas).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Dialogo();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }else {
            citas = new Citas(edad, peso, talla, circunferencia_brazo, String.format("%.2f", imc),
                    estatura, ganancia_peso, fecha, uuid, embarazada, semanas);
            db.collection("UsuariosPadres").document(datosUsuarios.getCedula())
                    .collection("hijos").document(datosUsuarios.getUuid_hijos())
                    .collection("Citas").document(uuid)
                    .set(citas).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Dialogo();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }
    private void MostrarFragment(){
        estatura = "null";
        peso = "null";
        talla = "null";
        circunferencia_brazo = "null";
        ganancia_peso = "null";
        semanas = "null";

        if(is_year){
            if(edad_temp < 5 && edad_temp >= 2){
                fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoDosFragment.class, bundle).commit();
                number_fragment = 2;
            }else if(edad_temp >= 5 && edad_temp < 10){
                fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoTresFragment.class, bundle).commit();
                number_fragment = 3;
            }else if(edad_temp >= 10 && edad_temp < 18){
                if(sexoCond == 1){
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCuatroFragment.class, bundle).commit();
                    number_fragment = 4;
                }else{
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCuatroEmbarazadasFragment.class, bundle).commit();
                    number_fragment = 6;
                }
            }else if(edad_temp >= 18){
                if(sexoCond == 1){
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCincoFragment.class, bundle).commit();
                    number_fragment = 5;
                }else{
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCincoEmbarazadasFragment.class, bundle).commit();
                    number_fragment = 7;
                }
            }
        }else{
            if(edad_temp < 3 && edad_temp >= 1){
                fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoUnoFragment.class, bundle).commit();
                number_fragment = 1;
            }else if(edad_temp >= 3 && edad_temp <= 24){
                fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoDosFragment.class, bundle).commit();
                number_fragment = 2;
            }
        }
    }

    public String calcularEdad(int ano,int mes,int dia){
        String edad = null;
        LocalDate today = LocalDate.now();
        LocalDate nacimiento = LocalDate.of(ano, mes,dia);
        Period p = Period.between(nacimiento, today);

        if(p.getYears()<1){
            if(p.getMonths()==1){
                edad = p.getMonths() + " mes";
            } else {
                edad = p.getMonths() + " meses";
            }
            is_year = false;
            edad_temp = p.getMonths();
        }else if(p.getYears()>=1 && p.getYears()<2){
            edad = p.getMonths() + 12 + " meses";
            is_year = false;
            edad_temp = p.getMonths() + 12;
        }else if(p.getYears() >= 2){
            edad = p.getYears() + " años";
            is_year=true;
            edad_temp = p.getYears();
        }return edad;
    }

    public int convertirSexo(String sexo_str){
        int sexoCond = 0;
        switch (sexo_str){
            case "Hombre":
                sexoCond = 1;
                break;
            case "Mujer":
                sexoCond = 2;
        }
        return sexoCond;
    }

    public String convertirMes(String mes_str){
        String mes = null;
        switch (mes_str){
            case "Enero":
                mes = "01";
                break;
            case "Febrero":
                mes = "02";
                break;
            case "Marzo":
                mes = "03";
                break;
            case "Abril":
                mes = "04";
                break;
            case "Mayo":
                mes = "05";
                break;
            case "Junio":
                mes = "06";
                break;
            case "Julio":
                mes = "07";
                break;
            case "Agosto":
                mes = "08";
                break;
            case "Septiembre":
                mes = "09";
                break;
            case "Octubre":
                mes = "10";
                break;
            case "Noviembre":
                mes = "11";
                break;
            case "Diciembre":
                mes = "12";
                break;
        }
        return mes;
    }

    private void ObtenerDatos() {
        peso = il_peso.getEditText().getText().toString().trim();
        talla = il_talla.getEditText().getText().toString().trim();
    }

    private boolean ValidarCamposInput() {
        int var = 0;
        ObtenerDatos();
        ValidarCampos validar = new ValidarCampos(this);
        //----------------------------Validar Peso------------------------------------------
        if(validar.Peso(peso).equals(getString(R.string.trues))){
            il_peso.setError(null);
            var++;
        }else if(validar.Peso(peso).equals(getString(R.string.nombre_valido))){
            il_peso.setError(getString(R.string.nombre_valido));
        } else if(validar.Peso(peso).equals(getString(R.string.empty))){
            il_peso.setError(getString(R.string.empty));
        }

        //----------------------------Validar Talla------------------------------------------
        if(validar.Talla(talla).equals(getString(R.string.trues))){
            il_talla.setError(null);
            var++;
        }else if(validar.Talla(talla).equals(getString(R.string.nombre_valido))){
            il_talla.setError(getString(R.string.nombre_valido));
        } else if(validar.Talla(talla).equals(getString(R.string.empty))){
            il_talla.setError(getString(R.string.empty));
        }
        if(var==2) {
            return true;
        }else{
            return false;
        }
    }

    private void Dialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaCita.this);
        builder.setTitle(Html.fromHtml("<big> La historia fue creada exitosamente! </big>"))
                .setPositiveButton(Html.fromHtml("<big>Aceptar</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(),InfoPersona.class);
                        i.putExtra("datos", datosUsuarios);
                        i.putExtra("statecheck", statecheck);
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, InfoPersona.class);
        intent.putExtra("datos", datosUsuarios);
        intent.putExtra("statecheck", statecheck);
        //intent.putExtra("citas", citas);
        context.startActivity(intent);
        //super.onBackPressed();
    }
     */
}
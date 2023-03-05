package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.FragmentsCitas.RangoCincoCitasFragment;
import com.example.myapplication.FragmentsCitas.RangoCincoEmbarazadasCitasFragment;
import com.example.myapplication.FragmentsCitas.RangoCuatroCitasFragment;
import com.example.myapplication.FragmentsCitas.RangoCuatroEmbarazadasCitasFragment;
import com.example.myapplication.FragmentsCitas.RangoDosCitasFragment;
import com.example.myapplication.FragmentsCitas.RangoTresCitasFragment;
import com.example.myapplication.FragmentsCitas.RangoUnoCitasFragment;
import com.example.myapplication.model.Citas;
import com.example.myapplication.model.DatosUsuarios;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResumenCita extends AppCompatActivity {

    private DatosUsuarios datosUsuarios;
    private boolean statecheck;
    private Citas citas;
    private FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private static Context context;
    private TextInputEditText fecha, edad, talla, peso, et_resumen, sexo, et_resumen_emb;
    private ProgressBar progressBar;
    private String cadena, numero, calendario;
    private TextInputLayout il_resumen, il_resumen_emb;
    private int numero_conv, semanas_conv;
    private float peso_conv, talla_conv, imc_conv, ganancia_conv;
    private Bundle bundle = new Bundle();
    private int sexoCond;
    private TextView resumen, resumen_emb, resumen_cbi;
    private float cbi_conv;

    public ResumenCita(Context context){
        this.context = context;
    }

    public ResumenCita(){
    }

    @Nullable
    @Override
    public ActionMode startSupportActionMode(@NonNull ActionMode.Callback callback) {
        return super.startSupportActionMode(callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_cita);

        datosUsuarios = (DatosUsuarios) getIntent().getExtras().getSerializable("datos");
        statecheck = (boolean) getIntent().getExtras().getSerializable("statecheck");
        citas = (Citas) getIntent().getExtras().getSerializable("citas");

        fecha = findViewById(R.id.marco_fecha);
        edad = findViewById(R.id.marco_edad);
        talla = findViewById(R.id.marco_talla);
        peso = findViewById(R.id.marco_peso);
        sexo = findViewById(R.id.marco_sexo);
        resumen_cbi = findViewById(R.id.textViewResumen_cbi);

        resumen = findViewById(R.id.textViewResumen);
        resumen_emb = findViewById(R.id.textViewResumen_emb);

        il_resumen = findViewById(R.id.cuadro_resumen);
        et_resumen = findViewById(R.id.marco_resumen);

        il_resumen_emb = findViewById(R.id.cuadro_resumen_embarazadas);
        et_resumen_emb =findViewById(R.id.marco_resumen_embarazadas);

        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_bar);


        fecha.setText(citas.getFecha());
        fecha.setFocusable(false);
        edad.setText(citas.getEdad());
        edad.setFocusable(false);
        talla.setText(citas.getTalla());
        talla.setFocusable(false);
        peso.setText(citas.getPeso());
        peso.setFocusable(false);
        sexo.setText(datosUsuarios.getSexo());
        sexo.setFocusable(false);

        et_resumen.setFocusable(false);
        et_resumen_emb.setFocusable(false);

        cadena = citas.getEdad().substring(0,citas.getEdad().indexOf(' '));
        numero = cadena;
        numero_conv = Integer.parseInt(numero);
        cadena = citas.getEdad().substring(citas.getEdad().indexOf(' ') + 1);
        cadena = cadena.substring(cadena.indexOf(' ')+1);
        calendario = cadena;

        bundle.putSerializable("datos", datosUsuarios);
        bundle.putSerializable("statecheck",statecheck);
        bundle.putSerializable("citas", citas);

        sexoCond = convertirSexo(datosUsuarios.getSexo());
        peso_conv = Float.parseFloat(citas.getPeso());
        talla_conv = Float.parseFloat(citas.getTalla());
        imc_conv = Float.parseFloat(citas.getImc());

        if(!citas.getSemanas().equals("null")) {
            semanas_conv = Integer.parseInt(citas.getSemanas());
        }else{
            semanas_conv = 0;
        }

        if(!citas.getGanancia_peso().equals("null")) {
            ganancia_conv = Float.parseFloat(citas.getGanancia_peso());
        }else{
            ganancia_conv = 0;
        }

        fragmentManager = getSupportFragmentManager();
        MostrarFragment();

        ValidacionCbi();

        TablasResumen();
        if(!citas.isEmbarazada()){
            il_resumen_emb.getEditText().setText("No aplica");
        }
        else{
            resumen_emb.setText("Extraido de la tabla ganancia de peso embarazadas.");
        }

        if(calendario.equals("años")){
            if(numero_conv >= 3 && numero_conv < 5) {
                il_resumen.getEditText().setText("No aplica");
            }
        }
    }

    private void ValidacionCbi(){
        if(calendario.equals("mes")){
            resumen_cbi.setText(" ");
        }
        else if(calendario.equals("meses")){
            if(numero_conv < 3){
                resumen_cbi.setText(" ");
            }
            if(numero_conv >= 3 && numero_conv <= 24){
                cbi_conv = Float.parseFloat(citas.getCircunferencia_brazo());
                if(cbi_conv <= 115){
                    resumen_cbi.setText("Déficit");
                }else{
                    resumen_cbi.setText("Normal");
                }
            }
        }else if(calendario.equals("años")){
            if(numero_conv >=2 && numero_conv < 5){
                cbi_conv = Float.parseFloat(citas.getCircunferencia_brazo());
                if(cbi_conv <= 115){
                    resumen_cbi.setText("Déficit");
                }else{
                    resumen_cbi.setText("Normal");
                }
            }else if (numero_conv >= 5 && numero_conv < 10){
                resumen_cbi.setText(" ");
            }else if(numero_conv >= 10 && numero_conv <18){
                if(sexoCond == 1){
                    resumen_cbi.setText(" ");
                }else{
                    if(citas.isEmbarazada()){
                        cbi_conv = Float.parseFloat(citas.getCircunferencia_brazo());
                        if(cbi_conv <= 221){
                            resumen_cbi.setText("Déficit");
                        }else{
                            resumen_cbi.setText("Normal");
                        }
                    }else{
                        resumen_cbi.setText(" ");
                    }
                }
            }
            else if(numero_conv >= 18){
                cbi_conv = Float.parseFloat(citas.getCircunferencia_brazo());
                if(cbi_conv <= 221){
                    resumen_cbi.setText("Déficit");
                }else{
                    resumen_cbi.setText("Normal");
                }
            }
        }
    }


    private void ValidacionPeso (double peso_uno, double peso_dos, double peso_tres, double peso_cuatro, double peso_cinco, double peso_seis,
                             double peso_siete, double peso_ocho){
        if (sexoCond == 1){
            if(peso_conv <= peso_uno){
                et_resumen.setText("Déficit");
            }else if(peso_conv >= peso_dos && peso_conv <= peso_tres){
                et_resumen.setText("Normal");
            }else if(peso_conv >= peso_cuatro){
                et_resumen.setText("Exceso");
            }
        }else{
            if(peso_conv <= peso_cinco){
                et_resumen.setText("Déficit");
            }else if(peso_conv >= peso_seis && peso_conv <= peso_siete){
                et_resumen.setText("Normal");
            }else if(peso_conv >= peso_ocho){
                et_resumen.setText("Exceso");
            }
        }
    }

    private void ValidacioTallaHombre(double peso_uno, double peso_dos, double peso_tres, double peso_cuatro){
            if(peso_conv <= peso_uno){
                et_resumen.setText("Déficit");
            }else if(peso_conv >= peso_dos && peso_conv <= peso_tres){
                et_resumen.setText("Normal");
            }else if(peso_conv >= peso_cuatro){
                et_resumen.setText("Exceso");
            }
    }

    private void ValidacioTallaMujer(double peso_uno, double peso_dos, double peso_tres, double peso_cuatro){
            if(peso_conv <= peso_uno){
                et_resumen.setText("Déficit");
            }else if(peso_conv >= peso_dos && peso_conv <= peso_tres){
                et_resumen.setText("Normal");
            }else if(peso_conv >= peso_cuatro){
                et_resumen.setText("Exceso");
            }
    }

    private void ValidacionImcHombre(double imc_uno, double imc_dos, double imc_tres, double imc_cuatro){
        if(imc_conv <= imc_uno){
            et_resumen.setText("Déficit");
        }else if(imc_conv >= imc_dos && imc_conv <= imc_tres){
            et_resumen.setText("Normal");
        }else if(imc_conv >= imc_cuatro){
            et_resumen.setText("Exceso");
        }
    }

    private void ValidacionImcMujer(double imc_uno, double imc_dos, double imc_tres, double imc_cuatro){
        if(imc_conv <= imc_uno){
            et_resumen.setText("Déficit");
        }else if(imc_conv >= imc_dos && imc_conv <= imc_tres){
            et_resumen.setText("Normal");
        }else if(imc_conv >= imc_cuatro){
            et_resumen.setText("Exceso");
        }
    }

   private void ValidacionGanancia(double ganancia_uno, double ganancia_dos, double ganancia_tres, double ganancia_cuatro){
        if(citas.isEmbarazada()){
            if(ganancia_conv <= ganancia_uno){
                et_resumen_emb.setText("Déficit");
            }else if(ganancia_conv >= ganancia_dos && ganancia_conv <= ganancia_tres){
                et_resumen_emb.setText("Normal");
            }else if(ganancia_conv >= ganancia_cuatro){
                et_resumen_emb.setText("Exceso");
            }
        }else{
            et_resumen_emb.setText("No aplica");
        }
    }

    private void TablasResumen () {
        if (calendario.equals("mes")) {
            resumen.setText("Extraido de la tabla Peso/edad (menores de 2 años).");
            ValidacionPeso(3.4, 3.5, 5.0, 5.1, 3.1, 3.2, 4.5, 4.6);
        } else if (calendario.equals("meses")) {
            resumen.setText("Extraido de la tabla Peso/edad (menores de 2 años).");
            switch (numero_conv) {
                case 0:
                    ValidacionPeso(2.7, 2.8, 4.2, 4.3, 2.5, 2.6, 3.6, 3.7);
                    break;
                case 2:
                    ValidacionPeso(4.1, 4.2, 6.2, 6.3, 3.9, 4.0, 5.5, 5.6);
                    break;
                case 3:
                    ValidacionPeso(4.9, 5.0, 7.1, 7.2, 4.4, 4.5, 5.5, 6.6);
                    break;
                case 4:
                    ValidacionPeso(5.4, 5.5, 8.0, 8.1, 5.0, 5.1, 7.1, 7.2);
                    break;
                case 5:
                    ValidacionPeso(5.9, 6.0, 8.5, 8.6, 5.5, 5.6, 7.9, 8.0);
                    break;
                case 6:
                    ValidacionPeso(6.4, 6.5, 9.0, 9.1, 6.0, 6.1, 8.4, 8.5);
                    break;
                case 7:
                    ValidacionPeso(7.0, 7.1, 9.5, 9.6, 6.3, 6.4, 9.0, 9.1);
                    break;
                case 8:
                    ValidacionPeso(7.5, 7.6, 10.0, 10.1, 6.9, 7.0, 9.4, 9.5);
                    break;
                case 9:
                    ValidacionPeso(7.9, 8.0, 10.4, 10.5, 7.3, 7.4, 9.8, 9.9);
                    break;
                case 10:
                    ValidacionPeso(8.2, 8.3, 10.7, 10.8, 7.5, 7.6, 10.2, 10.3);
                    break;
                case 11:
                    ValidacionPeso(8.4, 8.5, 11.2, 11.3, 7.9, 8.0, 10.5, 10.6);
                    break;
                case 12:
                    ValidacionPeso(8.8, 8.9, 11.5, 11.6, 8.1, 8.2, 10.9, 11.0);
                    break;
                case 13:
                    ValidacionPeso(9.0, 9.1, 11.7, 11.8, 8.3, 8.4, 11.2, 11.3);
                    break;
                case 14:
                    ValidacionPeso(9.3, 9.4, 12.0, 12.1, 8.5, 8.6, 11.5, 11.6);
                    break;
                case 15:
                    ValidacionPeso(9.4, 9.5, 12.4, 12.5, 8.7, 8.8, 11.7, 11.8);
                    break;
                case 16:
                    ValidacionPeso(9.7, 9.8, 12.6, 12.7, 8.9, 9.0, 12.0, 12.1);
                    break;
                case 17:
                    ValidacionPeso(9.8, 9.9, 12.9, 13.0, 9.0, 9.1, 12.1, 12.2);
                    break;
                case 18:
                    ValidacionPeso(9.9, 10.0, 13.2, 13.3, 9.2, 9.3, 12.4, 12.5);
                    break;
                case 19:
                    ValidacionPeso(10.2, 10.3, 13.4, 13.5, 9.4, 9.5, 12.6, 12.7);
                    break;
                case 20:
                    ValidacionPeso(10.3, 10.4, 13.6, 13.7, 9.5, 9.6, 12.9, 13.0);
                    break;
                case 21:
                    ValidacionPeso(10.4, 10.5, 13.8, 13.9, 9.6, 9.7, 13.1, 13.2);
                    break;
                case 22:
                    ValidacionPeso(10.6, 10.7, 14.0, 14.1, 9.9, 10.0, 13.3, 13.4);
                    break;
                case 23:
                    ValidacionPeso(10.7, 10.8, 14.3, 14.4, 10.0, 10.1, 13.5, 13.6);
                    break;
            }
        }else if (calendario.equals("años")) {
            if (numero_conv == 2) {
                resumen.setText("Extraido de la tabla Peso/edad (menores de 2 años).");
                ValidacionPeso(10.9, 11.0, 14.5, 14.6, 10.2, 10.3, 13.7, 13.8);
            }if(numero_conv == 5 || numero_conv == 6 || numero_conv == 7 || numero_conv == 8 || numero_conv == 9) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla Peso/Talla Masculino (menores de 10 años).");
                    if (talla_conv >= 55.0 && talla_conv <= 59.0) {
                        ValidacioTallaHombre(3.9, 4.0, 6.0, 6.1);
                    } else if (talla_conv >= 60.0 && talla_conv <= 63.0) {
                        ValidacioTallaHombre(4.9, 5.0, 7.0, 7.1);
                    } else if (talla_conv >= 64.0 && talla_conv <= 67.0) {
                        ValidacioTallaHombre(5.9, 6.0, 9.0, 9.1);
                    } else if (talla_conv >= 68.0 && talla_conv <= 71.0) {
                        ValidacioTallaHombre(6.9, 7.0, 10.0, 10.1);
                    } else if (talla_conv >= 72.0 && talla_conv <= 75.0) {
                        ValidacioTallaHombre(7.9, 8.0, 11.0, 11.1);
                    } else if (talla_conv >= 76.0 && talla_conv <= 80.0) {
                        ValidacioTallaHombre(8.9, 9.0, 12.0, 12.1);
                    } else if (talla_conv >= 81.0 && talla_conv <= 85.0) {
                        ValidacioTallaHombre(9.9, 10.0, 13.0, 13.1);
                    } else if (talla_conv >= 86.0 && talla_conv <= 90.0) {
                        ValidacioTallaHombre(10.9, 11.0, 14.0, 14.1);
                    } else if (talla_conv >= 91.0 && talla_conv <= 95.0) {
                        ValidacioTallaHombre(11.9, 12.0, 15.0, 15.1);
                    } else if (talla_conv >= 96.0 && talla_conv <= 99.0) {
                        ValidacioTallaHombre(12.9, 13.0, 16.0, 16.1);
                    } else if (talla_conv >= 100.0 && talla_conv <= 103.0) {
                        ValidacioTallaHombre(13.9, 14.0, 17.0, 17.1);
                    } else if (talla_conv >= 104.0 && talla_conv <= 107.0) {
                        ValidacioTallaHombre(14.9, 15.0, 19.0, 19.1);
                    } else if (talla_conv >= 108.0 && talla_conv <= 110.0) {
                        ValidacioTallaHombre(15.9, 16.0, 20.0, 20.1);
                    } else if (talla_conv >= 111.0 && talla_conv <= 114.0) {
                        ValidacioTallaHombre(16.9, 17.0, 21.0, 21.1);
                    } else if (talla_conv >= 115.0 && talla_conv <= 117.0) {
                        ValidacioTallaHombre(17.9, 18.0, 23.0, 23.1);
                    } else if (talla_conv >= 118.0 && talla_conv <= 120.0) {
                        ValidacioTallaHombre(18.9, 19.0, 24.0, 24.1);
                    } else if (talla_conv >= 121.0 && talla_conv <= 122.0) {
                        ValidacioTallaHombre(19.9, 20.0, 26.0, 26.1);
                    } else if (talla_conv >= 123.0 && talla_conv <= 125.0) {
                        ValidacioTallaHombre(20.9, 21.0, 27.0, 27.1);
                    } else if (talla_conv >= 126.0 && talla_conv <= 127.0) {
                        ValidacioTallaHombre(21.9, 22.0, 29.0, 29.1);
                    } else if (talla_conv >= 128.0 && talla_conv <= 129.0) {
                        ValidacioTallaHombre(22.9, 23.0, 30.0, 30.1);
                    } else if (talla_conv >= 130.0 && talla_conv <= 131.0) {
                        ValidacioTallaHombre(23.9, 24.0, 31.0, 31.1);
                    } else if (talla_conv >= 132.0 && talla_conv <= 134.0) {
                        ValidacioTallaHombre(24.9, 25.0, 33.0, 33.1);
                    } else if (talla_conv >= 135.0 && talla_conv <= 136.0) {
                        ValidacioTallaHombre(25.9, 26.0, 35.0, 35.1);
                    } else if (talla_conv >= 137.0 && talla_conv <= 138.0) {
                        ValidacioTallaHombre(26.9, 27.0, 37.0, 37.1);
                    } else if (talla_conv >= 139.0) {
                        ValidacioTallaHombre(27.9, 28.0, 39.0, 39.1);
                    } else if (talla_conv >= 140.0 && talla_conv <= 141.0) {
                        ValidacioTallaHombre(28.9, 29.0, 40.0, 40.1);
                    } else if (talla_conv >= 142.0 && talla_conv <= 143.0) {
                        ValidacioTallaHombre(29.9, 30.0, 41.0, 41.1);
                    } else if (talla_conv >= 144.0) {
                        ValidacioTallaHombre(30.9, 31.0, 43.0, 43.1);
                    } else if (talla_conv >= 145.0) {
                        ValidacioTallaHombre(31.9, 32.0, 44.0, 44.1);
                    }
                } else {
                    resumen.setText("Extraido de la tabla Peso/Talla Femenino (menores de 10 años).");
                    if (talla_conv >= 55.0 && talla_conv <= 61.0) {
                        ValidacioTallaMujer(3.9, 4.0, 6.0, 6.1);
                    } else if (talla_conv >= 62.0 && talla_conv <= 64.0) {
                        ValidacioTallaMujer(4.9, 5.0, 8.0, 8.1);
                    } else if (talla_conv >= 65.0 && talla_conv <= 68.0) {
                        ValidacioTallaMujer(5.9, 6.0, 9.0, 9.1);
                    } else if (talla_conv >= 69.0 && talla_conv <= 73.0) {
                        ValidacioTallaMujer(6.9, 7.0, 10.0, 10.1);
                    } else if (talla_conv >= 74.0 && talla_conv <= 77.0) {
                        ValidacioTallaMujer(7.9, 8.0, 11.0, 11.1);
                    } else if (talla_conv >= 78.0 && talla_conv <= 82.0) {
                        ValidacioTallaMujer(8.9, 9.0, 12.0, 12.1);
                    } else if (talla_conv >= 83.0 && talla_conv <= 88.0) {
                        ValidacioTallaMujer(9.9, 10.0, 13.0, 13.1);
                    } else if (talla_conv >= 89.0 && talla_conv <= 93.0) {
                        ValidacioTallaMujer(10.9, 11.0, 14.0, 14.1);
                    } else if (talla_conv >= 94.0 && talla_conv <= 97.0) {
                        ValidacioTallaMujer(11.9, 12.0, 15.0, 15.1);
                    } else if (talla_conv >= 98.0 && talla_conv <= 101.0) {
                        ValidacioTallaMujer(12.9, 13.0, 16.0, 16.1);
                    } else if (talla_conv >= 102.0 && talla_conv <= 105.0) {
                        ValidacioTallaMujer(13.9, 14.0, 17.0, 17.1);
                    } else if (talla_conv >= 106.0 && talla_conv <= 108.0) {
                        ValidacioTallaMujer(14.9, 15.0, 19.0, 19.1);
                    } else if (talla_conv >= 109.0 && talla_conv <= 111.0) {
                        ValidacioTallaMujer(15.9, 16.0, 20.0, 20.1);
                    } else if (talla_conv >= 112.0 && talla_conv <= 114.0) {
                        ValidacioTallaMujer(16.9, 17.0, 21.0, 21.1);
                    } else if (talla_conv >= 115.0 && talla_conv <= 118.0) {
                        ValidacioTallaMujer(17.9, 18.0, 23.0, 23.1);
                    } else if (talla_conv >= 119.0 && talla_conv <= 120.0) {
                        ValidacioTallaMujer(18.9, 19.0, 24.0, 24.1);
                    } else if (talla_conv >= 121.0 && talla_conv <= 123.0) {
                        ValidacioTallaMujer(19.9, 20.0, 26.0, 26.1);
                    } else if (talla_conv >= 124.0 && talla_conv <= 125.0) {
                        ValidacioTallaMujer(20.9, 21.0, 27.0, 27.1);
                    } else if (talla_conv >= 126.0 && talla_conv <= 127.0) {
                        ValidacioTallaMujer(21.9, 22.0, 29.0, 29.1);
                    } else if (talla_conv >= 128.0 && talla_conv <= 130.0) {
                        ValidacioTallaMujer(22.9, 23.0, 30.0, 30.1);
                    } else if (talla_conv >= 131.0 && talla_conv <= 132.0) {
                        ValidacioTallaMujer(23.9, 24.0, 31.0, 31.1);
                    } else if (talla_conv >= 133.0 && talla_conv <= 134.0) {
                        ValidacioTallaMujer(24.9, 25.0, 33.0, 33.1);
                    } else if (talla_conv >= 135.0 && talla_conv <= 136.0) {
                        ValidacioTallaMujer(25.9, 26.0, 35.0, 35.1);
                    } else if (talla_conv >= 137.0 && talla_conv <= 138.0) {
                        ValidacioTallaMujer(26.9, 27.0, 37.0, 37.1);
                    } else if (talla_conv >= 139.0) {
                        ValidacioTallaMujer(27.9, 28.0, 39.0, 39.1);
                    } else if (talla_conv >= 140.0 && talla_conv <= 141.0) {
                        ValidacioTallaMujer(28.9, 29.0, 40.0, 40.1);
                    } else if (talla_conv >= 142.0 && talla_conv <= 143.0) {
                        ValidacioTallaMujer(29.9, 30.0, 41.0, 41.1);
                    } else if (talla_conv >= 144.0) {
                        ValidacioTallaMujer(30.9, 31.0, 43.0, 43.1);
                    } else if (talla_conv >= 145.0) {
                        ValidacioTallaMujer(31.9, 32.0, 44.0, 44.1);
                    }
                }
            }else if (numero_conv == 10) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(14.5, 14.6, 19.9, 20.0);
                } else {
                    resumen.setText("Extraido de la tabla IMC Mujeres (mayores de 10 años).");
                    ValidacionImcMujer(14.5, 14.6, 20.4, 20.5);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 11) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(15.0, 15.1, 20.9, 21.0);
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(15.5, 15.1, 21.4, 21.5);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 12) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(15.5, 15.6, 21.4, 21.5);
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(15.5, 15.6, 22.4, 22.5);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 13) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(16.0, 16.1, 23.4, 24.5);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 14) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(16.5, 16.6, 23.4, 23.5);
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(16.5, 16.6, 24.4, 24.5);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 15) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(17.0, 17.1, 23.9, 24.0);
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(17.0, 17.1, 24.9, 25);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 16) {
                if (sexoCond == 1) {
                    ValidacionImcHombre(18.0, 18.1, 24.5, 24.6);
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(17.0, 17.1, 24.9, 25);
                    ValidacionSemanas();
                }
            }else if (numero_conv == 17) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(18.0, 18.1, 25.9, 26.0);
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(17.5, 17.6, 25.4, 25.5);
                    ValidacionSemanas();
                }
            }else if (numero_conv >= 18) {
                if (sexoCond == 1) {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(18.5, 18.6, 24.9, 25);
                } else {
                    resumen.setText("Extraido de la tabla IMC Hombres (mayores de 10 años).");
                    ValidacionImcHombre(18.5, 18.6, 24.9, 25);
                    if(numero_conv >= 18 && numero_conv <= 56){
                        ValidacionSemanas();
                    }
                }
            }
        }
    }

    private void ValidacionSemanas(){
        switch(semanas_conv){
            case 13:
                ValidacionGanancia(0.9, 1.0, 4.0, 4.1);
                break;
            case 14:
                ValidacionGanancia(1.1, 1.2, 4.5, 4.6);
                break;
            case 15:
                ValidacionGanancia(1.4, 1.5, 5.0, 4.6);
                break;
            case 16:
                ValidacionGanancia(1.8, 1.9, 5.5, 5.6);
                break;
            case 17:
                ValidacionGanancia(2.1, 2.2, 6.2, 6.3);
                break;
            case 18:
                ValidacionGanancia(2.5, 2.5, 6.9, 7.0);
                break;
            case 19:
                ValidacionGanancia(3.0, 3.1, 7.5, 7.6);
                break;
            case 20:
                ValidacionGanancia(3.4, 3.5, 8.1, 8.2);
                break;
            case 21:
                ValidacionGanancia(3.8, 3.9, 8.7, 8.8);
                break;
            case 22:
                ValidacionGanancia(4.1, 4.2, 9.2, 9.3);
                break;
            case 23:
                ValidacionGanancia(4.5, 4.6, 9.7, 9.8);
                break;
            case 24:
                ValidacionGanancia(5.0, 5.1, 10.2, 10.3);
                break;
            case 25:
                ValidacionGanancia(5.2, 5.3, 10.7, 10.8);
                break;
            case 26:
                ValidacionGanancia(5.5, 5.6, 11.2, 11.3);
                break;
            case 27:
                ValidacionGanancia(5.9, 6.0, 11.7, 11.8);
                break;
            case 28:
                ValidacionGanancia(6.1, 6.2, 12.2, 12.3);
                break;
            case 29:
                ValidacionGanancia(6.5, 6.6, 12.7, 12.8);
                break;
            case 30:
                ValidacionGanancia(6.7, 6.8, 13.2, 13.3);
                break;
            case 31:
                ValidacionGanancia(7.1, 7.2, 14.2, 14.3);
                break;
            case 32:
                ValidacionGanancia(7.2, 7.3, 14.2, 14.3);
                break;
            case 33:
                ValidacionGanancia(7.4, 7.5, 14.5, 14.6);
                break;
            case 34:
                ValidacionGanancia(7.5, 7.6, 14.9, 15.0);
                break;
            case 35:
                ValidacionGanancia(7.6, 7.7, 15.2, 15.3);
                break;
            case 36:
                ValidacionGanancia(7.7, 7.8, 15.4, 15.5);
                break;
            case 37:
                ValidacionGanancia(7.8, 7.9, 15.6, 15.7);
                break;
            case 38:
                ValidacionGanancia(7.9, 8.0, 15.7, 15.8);
                break;
            case 39:
                ValidacionGanancia(7.9, 8.0, 15.8, 15.9);
                break;
            case 40:
                ValidacionGanancia(7.9, 8.0, 16.0, 16.1);
                break;

        }
    }

    private void MostrarFragment(){
        if(calendario.equals("mes")){
            fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoUnoCitasFragment.class, bundle).commit();
        }
        else if(calendario.equals("meses")){
                if(numero_conv < 3){
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoUnoCitasFragment.class, bundle).commit();
                }else if(numero_conv >= 3 && numero_conv < 24){
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoDosCitasFragment.class, bundle).commit();
                }
        }
        else if(calendario.equals("años")){
                if(numero_conv >= 2 && numero_conv < 5){
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoDosCitasFragment.class, bundle).commit();
                }else if(numero_conv >=5 && numero_conv < 10){
                    fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoTresCitasFragment.class, bundle).commit();
                }else if(numero_conv >= 10 && numero_conv < 18){
                    if(sexoCond == 1){
                        fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCuatroCitasFragment.class, bundle).commit();
                    }else{
                        fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCuatroEmbarazadasCitasFragment.class, bundle).commit();
                    }
                }else if (numero_conv >= 18){
                    if(sexoCond == 1){
                        fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCincoCitasFragment.class, bundle).commit();
                    }else{
                        fragmentManager.beginTransaction().add(R.id.fragment_container_view, RangoCincoEmbarazadasCitasFragment.class, bundle).commit();
                    }
                }
        }
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
}
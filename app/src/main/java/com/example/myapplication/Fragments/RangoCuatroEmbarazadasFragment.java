package com.example.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.DatosUsuarios;
import com.example.myapplication.model.ValidarCampos;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RangoCuatroEmbarazadasFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private DatosUsuarios datosUsuarios;
    private boolean statecheck;
    private static CheckBox checkBox;
    private static TextInputLayout il_estatura;
    private static TextInputLayout il_ganancia_peso;
    private static TextInputLayout il_cbi, il_semanas;
    private static TextInputEditText et_ganancia_peso, et_semanas;
    private static String estatura = "null";
    private static String gananciaPeso = "null";
    private static String circunferencia_brazo = "null";
    private static String semanas = "null";
    private static Context context;

    public RangoCuatroEmbarazadasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rango_cuatro_embarazadas, container, false);
        il_estatura = view.findViewById(R.id.il_estatura);
        il_ganancia_peso = view.findViewById(R.id.il_gananciaPeso);
        et_ganancia_peso = view.findViewById(R.id.et_gananciaPeso);
        et_semanas = view.findViewById(R.id.et_semanas);
        il_cbi = view.findViewById(R.id.il_cbi);
        checkBox = view.findViewById(R.id.checkBoxEmbarazada);
        il_semanas = view.findViewById(R.id.il_semanas);
        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.bundle = savedInstanceState;
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        datosUsuarios = (DatosUsuarios) bundle.getSerializable("datos");
        statecheck = (boolean) bundle.getSerializable("statecheck");

        et_ganancia_peso.setEnabled(false);
        et_semanas.setEnabled(false);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()) {
                    et_ganancia_peso.setEnabled(false);
                    et_semanas.setEnabled(false);
                } else {
                    et_ganancia_peso.setEnabled(true);
                    et_semanas.setEnabled(true);
                }
            }
        });
    }

    public static String Estatura() {
        estatura = il_estatura.getEditText().getText().toString().trim();
        if (!estatura.isEmpty()) {
            il_estatura.setError(null);
            return estatura;
        } else {
            il_estatura.setError("Campo Obligatorio");
            return "null";
        }
    }

    public static boolean Embarazada() {
        if (checkBox.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    public static String GananciaPeso() {
        gananciaPeso = il_ganancia_peso.getEditText().getText().toString().trim();
        if (checkBox.isChecked()) {
            if (!gananciaPeso.isEmpty()) {
                il_ganancia_peso.setError(null);
                return gananciaPeso;
            } else {
                il_ganancia_peso.setError("Campo Obligatorio");
                return "null";
            }
        }
        return "null";
    }

    public static String Semanas() {
        semanas = il_semanas.getEditText().getText().toString().trim();
        if (checkBox.isChecked()) {
            if (!semanas.isEmpty()) {
                il_semanas.setError(null);
                return semanas;
            } else {
                il_semanas.setError("Campo Obligatorio");
                return "null";
            }
        }
        return "null";
    }

    public static String Circunferencia() {
        circunferencia_brazo = il_cbi.getEditText().getText().toString().trim();
        if (checkBox.isChecked()) {
            if (!circunferencia_brazo.isEmpty()) {
                il_cbi.setError(null);
                return circunferencia_brazo;
            } else {
                il_cbi.setError("Campo Obligatorio");
                return "null";
            }
        }
        return "null";
    }
}
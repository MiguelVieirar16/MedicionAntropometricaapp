package com.example.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.ConditionVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.DatosUsuarios;
import com.example.myapplication.model.ValidarCampos;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RangoCincoFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private DatosUsuarios datosUsuarios;
    private boolean statecheck;
    private CheckBox checkBox;
    private static TextInputLayout il_estatura;
    private static TextInputLayout il_cbi;
    private static String estatura = "null";
    private static String circunferencia_brazo = "null";
    private static Context context;

    public RangoCincoFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rango_cinco, container, false);
        il_estatura = view.findViewById(R.id.il_estatura);
        il_cbi = view.findViewById(R.id.il_cbi);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.bundle = savedInstanceState;
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        datosUsuarios = (DatosUsuarios) bundle.getSerializable("datos");
        statecheck = (boolean) bundle.getSerializable("statecheck");
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

    public static String Circunferencia() {
        circunferencia_brazo = il_cbi.getEditText().getText().toString().trim();
        if (!circunferencia_brazo.isEmpty()) {
            il_cbi.setError(null);
            return circunferencia_brazo;
        } else {
            il_cbi.setError("Campo Obligatorio");
            return "null";
        }
    }

}

package com.example.myapplication.FragmentsCitas;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Citas;
import com.example.myapplication.model.DatosUsuarios;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RangoCincoEmbarazadasCitasFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private DatosUsuarios datosUsuarios;
    private Citas citas;
    private boolean statecheck;
    private Button boton;
    private static CheckBox checkBox;
    private static TextInputLayout il_estatura;
    private static TextInputLayout il_ganancia_peso;
    private static TextInputLayout il_cbi, il_imc, il_semanas;
    private static TextInputEditText et_estatura, et_ganancia_peso, et_cbi, et_imc,  et_semanas;

    public RangoCincoEmbarazadasCitasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rango_cinco_embarazadas_citas, container, false);
        il_cbi = view.findViewById(R.id.mostrar_cbi_4);
        il_estatura = view.findViewById(R.id.mostrar_estatura_4);
        il_ganancia_peso = view.findViewById(R.id.mostrar_ganancia_peso_2);
        il_imc = view.findViewById(R.id.mostrar_imc_4);
        il_semanas = view.findViewById(R.id.mostrar_semanas_2);

        et_semanas = view.findViewById(R.id.rectangulo_semanas_2);
        et_imc = view.findViewById(R.id.rectangulo_imc_4);
        et_cbi = view.findViewById(R.id.rectangulo_cbi_4);
        et_estatura = view.findViewById(R.id.rectangulo_estatura_4);
        et_ganancia_peso = view.findViewById(R.id.rectangulo_ganancia_peso_2);

        checkBox = view.findViewById(R.id.checkBoxEmbarazada);

        et_cbi.setFocusable(false);
        et_estatura.setFocusable(false);
        et_ganancia_peso.setFocusable(false);
        et_imc.setFocusable(false);
        et_semanas.setFocusable(false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.bundle = savedInstanceState;
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();
        datosUsuarios = (DatosUsuarios) bundle.getSerializable("datos");
        statecheck = (boolean) bundle.getSerializable("statecheck");
        citas = (Citas) bundle.getSerializable("citas");

        il_estatura.getEditText().setText(citas.getEstatura());
        il_cbi.getEditText().setText(citas.getCircunferencia_brazo());
        il_ganancia_peso.getEditText().setText(citas.getGanancia_peso());
        il_imc.getEditText().setText(citas.getImc());
        il_semanas.getEditText().setText(citas.getSemanas());
        checkBox.setChecked(citas.isEmbarazada());
        checkBox.setClickable(false);
        if(!citas.isEmbarazada()){
            il_ganancia_peso.getEditText().setText("No aplica");
            il_semanas.getEditText().setText("No aplica");
        }
    }
}
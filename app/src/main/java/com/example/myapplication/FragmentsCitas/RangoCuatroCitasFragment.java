package com.example.myapplication.FragmentsCitas;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Citas;
import com.example.myapplication.model.DatosUsuarios;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.nullness.qual.NonNull;


public class RangoCuatroCitasFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private DatosUsuarios datosUsuarios;
    private Citas citas;
    private boolean statecheck;
    private static TextInputLayout il_estatura, il_imc;
    private static TextInputEditText et_estatura, et_imc;

    public RangoCuatroCitasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rango_cuatro_citas, container, false);
        il_estatura = view.findViewById(R.id.mostrar_estatura);
        il_imc = view.findViewById(R.id.mostrar_imc);

        et_imc = view.findViewById(R.id.rectangulo_imc);
        et_estatura = view.findViewById(R.id.rectangulo_estatura);

        et_estatura.setFocusable(false);
        et_imc.setFocusable(false);
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
        il_imc.getEditText().setText(citas.getImc());
    }

}
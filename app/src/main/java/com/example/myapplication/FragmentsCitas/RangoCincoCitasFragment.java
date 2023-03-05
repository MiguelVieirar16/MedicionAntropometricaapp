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

public class RangoCincoCitasFragment extends Fragment {

    private View view;
    private Bundle bundle;
    private DatosUsuarios datosUsuarios;
    private Citas citas;
    private boolean statecheck;
    private Button boton;
    private static TextInputLayout il_estatura;
    private static TextInputLayout il_cbi, il_imc;
    private static TextInputEditText et_estatura, et_cbi, et_imc;
    private CheckBox checkBox;

    public RangoCincoCitasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rango_cinco_citas, container, false);
        il_cbi = view.findViewById(R.id.mostrar_cbi_3);
        il_estatura = view.findViewById(R.id.mostrar_estatura_3);
        il_imc = view.findViewById(R.id.mostrar_imc_3);

        et_imc = view.findViewById(R.id.rectangulo_imc_3);
        et_cbi = view.findViewById(R.id.rectangulo_cbi_3);
        et_estatura = view.findViewById(R.id.rectangulo_estatura_3);

        et_cbi.setFocusable(false);
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
        il_cbi.getEditText().setText(citas.getCircunferencia_brazo());
        il_imc.getEditText().setText(citas.getImc());

    }

}
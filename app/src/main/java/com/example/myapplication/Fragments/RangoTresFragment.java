package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.DatosUsuarios;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RangoTresFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private DatosUsuarios datosUsuarios;
    private boolean statecheck;


    public RangoTresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rango_tres, container, false);
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
    }

}
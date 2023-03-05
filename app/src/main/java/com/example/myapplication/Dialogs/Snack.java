package com.example.myapplication.Dialogs;

import android.content.Context;
import android.view.View;

import com.example.myapplication.Registrar;
import com.google.android.material.snackbar.Snackbar;

public class Snack {
    Context context;
    View view;

    public Snack(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void SnackMessage(String mensaje, int color){
        Snackbar snackbar = Snackbar.make(view, mensaje, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(context.getResources().getColor(color, context.getTheme()));
        sbView.setElevation(50);
        snackbar.show();
    }

}

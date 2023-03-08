package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Button button1, button2, button3;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        button1 = findViewById(R.id.bt_registrarpersona);
        button2 = findViewById(R.id.bt_buscar);
        button3 = findViewById(R.id.bt_registrarcita);

        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),Registrar.class);
                startActivity(i);
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),Buscar.class);
                i.putExtra("control",false);
                startActivity(i);
            }
        });

        button3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),Buscar.class);
                i.putExtra("control",false);
                startActivity(i);
            }
        });
    }
    private void Dialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(Html.fromHtml("<big>¿Desea salir de la aplicacion?</big>"))
                .setPositiveButton(Html.fromHtml("<big>Sí</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                }).setNegativeButton(Html.fromHtml("<big>No</big>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Dialogo();
    }


}
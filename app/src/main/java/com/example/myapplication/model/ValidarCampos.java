package com.example.myapplication.model;

import android.content.Context;
import android.util.Patterns;

import com.example.myapplication.R;

import java.security.AccessControlContext;
import java.util.regex.Pattern;

//Se validan todos los campos que el usuario puede ingresar
public class ValidarCampos {


    private final Context mContext;

    private int dia, año,mes_int;
    private String mes;
    static int[] diasMes= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static final Pattern CI_PATTERN =
            Pattern.compile("^" +
                    "(?=\\S+$)"+            //sin espacios en blanco
                    ".{7,8}"+                //7-8 caracteres
                    "$");
    private static final Pattern TELF_PATTERN =
            Pattern.compile("^" +
                    "(?=\\S+$)"+            //sin espacios en blanco
                    ".{7}"+                //7 caracteres
                    "$");
    private static final Pattern NOMBRE_APELLIDO_PATTERN =
            Pattern.compile("^" + //sin espacios en blanco
                    ".{3,20}"+                //3-20 caracteres
                    "$");

    private static final Pattern PESOS_PATTERN =
           Pattern.compile("^" + //sin espacios en blanco
                    ".{1,5}"+                //1-5 caracteres
                    "$");

    public ValidarCampos(Context mContext) {
        this.mContext = mContext;
    }


    //convierte en entero la seleccion del mes del usuario
    private  void BuscarMes(String mes){
        if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[0])){
            mes_int = 1;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[1])){
            mes_int = 2;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[2])){
            mes_int = 3;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[3])){
            mes_int = 4;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[4])){
            mes_int = 5;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[5])){
            mes_int = 6;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[6])){
            mes_int = 7;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[7])){
            mes_int = 8;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[8])){
            mes_int = 9;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[9])){
            mes_int = 10;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[10])){
            mes_int = 11;
        }else if(mes.equals(mContext.getResources().getStringArray(R.array.meses)[11])){
            mes_int = 12;
        }
    }

    //valida que la fecha introducida sea valida (dia, mes y año)
    public boolean ValidarFecha(int dia, String mes, int año ){
        this.dia = dia;
        this.mes = mes;
        this.año = año;

        BuscarMes(mes);

        if ( año < 1900 || año > 2200 ) {
            return false;
        }
        if ( mes_int < 1 || mes_int > 12 )
            return false;
        // Para febrero y bisiesto el limite es 29
        if ( mes_int==2 && año%4==0 )
            return dia>=1 && dia<=29;
        return dia>=0 && dia<=diasMes[mes_int-1];
    }

    //Valida la direccion de correo contenga @ y .com
    public String ValidarEmail(String correo){
        if(correo.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            return mContext.getResources().getString(R.string.correo_invalido);
        }else{
            return  mContext.getResources().getString(R.string.trues);
        }
    }

    //Valida que tenga la longitud de un numero telefonico
    public String ValidarTelefono(String telefono){
        if(telefono.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!TELF_PATTERN.matcher(telefono).matches()){
            return mContext.getResources().getString(R.string.telefono_invalido);
        }else{

            return  mContext.getResources().getString(R.string.trues);
        }
    }


    //Valida que la Cedula tenga la longitud adecuada
    public String ValidarID_CI(String cedula){
        if(cedula.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!CI_PATTERN.matcher(cedula).matches()) {
            return mContext.getResources().getString(R.string.ci_invalida);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    //Valida que la Direccion tenga la longitud adecuada
    public String ValidarDireccion(String direccion){
        if(direccion.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else{
            return  mContext.getResources().getString(R.string.trues);}
    }
    //Valida que el colegio tenga la longitud adecuada
    public String ValidarColegio(String colegio){
        if(colegio.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    //Valida que el trabajo tenga la longitud adecuada
    public String ValidarTrabajo(String trabajo){
        if(trabajo.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    //Valida que el Nombre o apellido tenga la longitud adecuada
    public String ValidarNombreApellido(String nombre){
        if(nombre.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!NOMBRE_APELLIDO_PATTERN.matcher(nombre).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    //Valida que el Nombre o apellido del papa tenga la longitud adecuada
    public String ValidarNombre_papa(String nombre_papa){
        if(nombre_papa.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!NOMBRE_APELLIDO_PATTERN.matcher(nombre_papa).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    public String Estatura(String estatura){
        if(estatura.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!PESOS_PATTERN.matcher(estatura).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    public String Talla(String talla){
        if(talla.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!PESOS_PATTERN.matcher(talla).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    public String Peso(String peso){
        if(peso.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!PESOS_PATTERN.matcher(peso).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    public String CircunfereciaBarazo(String circunferencia){
        if(circunferencia.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!PESOS_PATTERN.matcher(circunferencia).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

    public String GananciaPeso(String ganancia_peso){
        if(ganancia_peso.isEmpty()){
            return mContext.getResources().getString(R.string.empty);
        }else if(!PESOS_PATTERN.matcher(ganancia_peso).matches()){
            return mContext.getResources().getString(R.string.nombre_valido);
        }
        else{
            return  mContext.getResources().getString(R.string.trues);}
    }

}
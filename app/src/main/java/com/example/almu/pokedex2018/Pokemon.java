package com.example.almu.pokedex2018;

import com.google.gson.annotations.SerializedName;

class Tipo {
    @SerializedName("type")
    private Contenido tipo;

    public Tipo(Contenido tipo) {
        this.tipo = tipo;
    }

    public Contenido getTipo() {
        return tipo;
    }

    public void setTipo(Contenido tipo) {
        this.tipo = tipo;
    }
}

class Habilidad{
    @SerializedName("ability")
    private Contenido habilidad;

    public Habilidad(Contenido habilidad) {
        this.habilidad = habilidad;
    }

    public Contenido getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(Contenido habilidad) {
        this.habilidad = habilidad;
    }
}

class Contenido{
    @SerializedName("name")
    private String nombre;

    public Contenido(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

public class Pokemon {

    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String nombre;
    @SerializedName("weight")
    double peso;
    @SerializedName("height")
    double altura;
    @SerializedName("types")
    Tipo[] tipos;
    @SerializedName("abilities")
    Habilidad[] habilidades;
    int oculto;
    double latitud;
    double longitud;

    public Pokemon(Integer id, String nombre, double altura, double peso,
                   Tipo[] tipos, Habilidad[] habilidades, int oculto, double latitud, double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.peso = peso;
        this.altura = altura;
        this.tipos = tipos;
        this.habilidades = habilidades;
        this.oculto = oculto;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public Tipo[] getTipos() {
        return tipos;
    }

    public void setTipos(Tipo[] tipos) {
        this.tipos = tipos;
    }

    public Habilidad[] getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(Habilidad[] habilidades) {
        this.habilidades = habilidades;
    }

    public int getOculto() {
        return oculto;
    }

    public void setOculto(int oculto) {
        this.oculto = oculto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}

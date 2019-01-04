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
    Integer peso;
    @SerializedName("height")
    Integer altura;
    @SerializedName("types")
    Tipo[] tipos;
    @SerializedName("abilities")
    private Habilidad[] habilidades;

    //List<String> evoluciones;

    public Pokemon(Integer id, String nombre, Integer peso, Integer altura,
                   Tipo[] tipos, Habilidad[] habilidades) {
        this.id = id;
        this.nombre = nombre;
        this.peso = peso;
        this.altura = altura;
        this.tipos = tipos;
        this.habilidades = habilidades;
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

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
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
}

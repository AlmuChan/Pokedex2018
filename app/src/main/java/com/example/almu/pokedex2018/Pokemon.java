package com.example.almu.pokedex2018;

import java.util.List;

public class Pokemon {
    int id;
    String nombre;
    String tipo;
    List<String> habilidades;
    List<String> evoluciones;

    public Pokemon(int id, String nombre, String tipo, List<String> habilidades,
                   List<String> evoluciones) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.habilidades = habilidades;
        this.evoluciones = evoluciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<String> habilidades) {
        this.habilidades = habilidades;
    }

    public List<String> getEvoluciones() {
        return evoluciones;
    }

    public void setEvoluciones(List<String> evoluciones) {
        this.evoluciones = evoluciones;
    }
}

package com.example.almu.pokedex2018;

import java.util.List;

public class Pokemon {
    int id;
    String nombre;
    String tipo;
    List<String> habilidades;
    List<String> evoluciones;

    public Pokemon(int id, String nombre, String tipo,List<String> habilidades, List<String> evoluciones) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.habilidades = habilidades;
        this.evoluciones = evoluciones;
    }
}

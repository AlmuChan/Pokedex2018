package com.example.almu.pokedex2018;

import java.util.List;

public class Pokemon {
    String nombre;
    List<String> habilidades;
    List<String> evoluciones;
    int fotoId;

    public Pokemon(String nombre, List<String> habilidades, List<String> evoluciones, int fotoId) {
        this.nombre = nombre;
        this.habilidades = habilidades;
        this.evoluciones = evoluciones;
        this.fotoId = fotoId;
    }
}

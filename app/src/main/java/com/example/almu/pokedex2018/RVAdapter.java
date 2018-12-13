package com.example.almu.pokedex2018;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PokemonViewHolder> {
    List<Pokemon> pokemonList;

    public RVAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main,
                viewGroup, false);
        PokemonViewHolder pvh = new PokemonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder pokemonViewHolder, int i) {
        pokemonViewHolder.pokemonNombre.setText(pokemonList.get(i).nombre);
        pokemonViewHolder.pokemonMovimientos.setText(pokemonList.get(i).habilidades.get(i));
        pokemonViewHolder.pokemonFoto.setImageResource(pokemonList.get(i).fotoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView pokemonNombre;
        TextView pokemonMovimientos;
        TextView pokemonEvoluciones;
        ImageView pokemonFoto;

        PokemonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardPrueba);
            pokemonNombre = (TextView)itemView.findViewById(R.id.person_name);
            pokemonMovimientos = (TextView)itemView.findViewById(R.id.person_age);
            pokemonFoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }
}

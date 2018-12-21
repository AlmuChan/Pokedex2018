package com.example.almu.pokedex2018;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PokemonViewHolder>{
    private List<Pokemon> items;
    public RecyclerView recycler;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recycler = recyclerView;
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {
        // Campos de la tarjeta para el item
        public ImageView imagen;
        public TextView nombre;
        public Button tipo;
        public View view;

        public PokemonViewHolder(View v) {
            super(v);
            view = v;
            imagen = v.findViewById(R.id.imagen);
            nombre = v.findViewById(R.id.nombre);
            tipo = v.findViewById(R.id.tipo);

            //Cuando se hace click en una tarjeta
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int pos = recycler.getChildLayoutPosition(v);
                    Toast.makeText(v.getContext(),"Pokemon clickado: "
                            + items.get(pos).id, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public RVAdapter(List<Pokemon> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        return new PokemonViewHolder(v);
    }

    // Colocamos todos los datos de una tarjeta
    @Override
    public void onBindViewHolder(PokemonViewHolder viewHolder, int i) {
        String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
        GlideApp.with(viewHolder.imagen.getContext())
                .load(url + items.get(i).id + ".png")
                .placeholder(R.mipmap.desconocido)
                .into(viewHolder.imagen);

        viewHolder.nombre.setText(items.get(i).id + ". " + items.get(i).nombre);
        viewHolder.tipo.setBackgroundColor(ContextCompat.getColor(viewHolder.view.getContext(),
                getColorType(items.get(i).tipo)));
        viewHolder.tipo.setText(items.get(i).tipo);
    }

    // Switch de colores para los tipos
    public int getColorType(String tipo) {
        switch (tipo) {
            case "Agua":
                return R.color.tipoAgua;
            case "Bicho":
                return R.color.tipoBicho;
            case "Dragón":
                return R.color.tipoDragon;
            case "Eléctrico":
                return R.color.tipoElectrico;
            case "Fantasma":
                return R.color.tipoFantasma;
            case "Fuego":
                return R.color.tipoFuego;
            case "Hielo":
                return R.color.tipoHielo;
            case "Lucha":
                return R.color.tipoLucha;
            case "Normal":
                return R.color.tipoNormal;
            case "Planta":
                return R.color.tipoPlanta;
            case "Psíquico":
                return R.color.tipoPsiquico;
            case "Roca":
                return R.color.tipoRoca;
            case "Tierra":
                return R.color.tipoTierra;
            case "Veneno":
                return R.color.tipoVeneno;
            case "Volador":
                return R.color.tipoVolador;
        }

        return R.color.colorAccent;
    }
}

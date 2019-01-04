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
        public Button tipo2;
        public View view;

        public PokemonViewHolder(View v) {
            super(v);
            view = v;
            imagen = v.findViewById(R.id.imagen);
            nombre = v.findViewById(R.id.nombre);
            tipo = v.findViewById(R.id.tipo);
            tipo2 = v.findViewById(R.id.tipo2);

            //Cuando se hace click en una tarjeta
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int pos = recycler.getChildLayoutPosition(v);
                    String text = "";

                    for(Tipo t : items.get(pos).getTipos())
                        text += t.getTipo().getNombre();

                    Toast.makeText(v.getContext(),"Pokemon clickado: "
                            + items.get(pos).id + text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public RVAdapter(List<Pokemon> items) {
        this.items = items;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
                getColorType(items.get(i).getTipos()[0].getTipo().getNombre())));
        viewHolder.tipo.setText(items.get(i).getTipos()[0].getTipo().getNombre());

        if(items.get(i).getTipos().length == 2) {
            viewHolder.tipo2.setVisibility(View.VISIBLE);
            viewHolder.tipo2.setBackgroundColor(ContextCompat.getColor(viewHolder.view.getContext(),
                    getColorType(items.get(i).getTipos()[1].getTipo().getNombre())));
            viewHolder.tipo2.setText(items.get(i).getTipos()[1].getTipo().getNombre());
        }
    }

    // Switch de colores para los tipos
    public int getColorType(String tipo) {
        switch (tipo) {
            case "water":
                return R.color.tipoAgua;
            case "bug":
                return R.color.tipoBicho;
            case "dragon":
                return R.color.tipoDragon;
            case "electric":
                return R.color.tipoElectrico;
            case "ghost":
                return R.color.tipoFantasma;
            case "fire":
                return R.color.tipoFuego;
            case "ice":
                return R.color.tipoHielo;
            case "fighting":
                return R.color.tipoLucha;
            case "normal":
                return R.color.tipoNormal;
            case "grass":
                return R.color.tipoPlanta;
            case "psychic":
                return R.color.tipoPsiquico;
            case "rock":
                return R.color.tipoRoca;
            case "ground":
                return R.color.tipoTierra;
            case "poison":
                return R.color.tipoVeneno;
            case "flying":
                return R.color.tipoVolador;
        }

        return R.color.colorAccent;
    }
}

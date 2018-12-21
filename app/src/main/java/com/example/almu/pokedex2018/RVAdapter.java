package com.example.almu.pokedex2018;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
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
        public TextView tipo;
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
        Glide.with(viewHolder.imagen.getContext())
                .load(url + items.get(i).id + ".png")
                //.placeholder(R.mipmap.ic_launcher_round)
                .into(viewHolder.imagen);

        viewHolder.nombre.setText(items.get(i).id + ". " + items.get(i).nombre);
        viewHolder.tipo.setText(items.get(i).tipo);
    }
}

package com.example.almu.pokedex2018;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PokemonViewHolder> {
    List<Pokemon> pokemonList;
    RecyclerView recycler;

    public RVAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    //para que queremos saber el tamaño de la lista?
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main,
                viewGroup, false);
        return new PokemonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder pokemonViewHolder, int i) {
        Glide.with(pokemonViewHolder.pokemonFoto.getContext())
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemonList.get(i).id+".png")
                //.placeholder(R.mipmap.ic_launcher_round)
                .into(pokemonViewHolder.pokemonFoto);

        pokemonViewHolder.pokemonNombre.setText(pokemonList.get(i).id+".-"+pokemonList.get(i).nombre);
        pokemonViewHolder.pokemonTipo.setText("Tipo: "+pokemonList.get(i).tipo);
        //entonces no cambiamos el color?
        //pokemonViewHolder.color = getCardColor(pokemonList.get(i).tipo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recycler = recyclerView;
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {

        TextView pokemonNombre;
        TextView pokemonTipo;
        ImageView pokemonFoto;
        public View view;

        PokemonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            pokemonNombre = (TextView)itemView.findViewById(R.id.nombre);
            pokemonTipo = (TextView)itemView.findViewById(R.id.tipo);
            pokemonFoto = (ImageView)itemView.findViewById(R.id.imagen);

            //Cuando se hace click en una tarjeta
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int pos = recycler.getChildLayoutPosition(v);
                    Toast.makeText(v.getContext(),"Pokemon clickado: "+ pokemonList.get(pos).id,Toast.LENGTH_LONG).show();
                }
            });
            //cv.setCardBackgroundColor(color);
        }
    }
}

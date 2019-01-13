package com.example.almu.pokedex2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PokemonDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PokemonDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokemonDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView sprite;
    TextView tvNombre, tvTipo, tvHabilidades, tvPeso;
    Button localizacion;

    private OnFragmentInteractionListener mListener;

    public PokemonDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PokemonDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PokemonDetailFragment newInstance() {
        return new PokemonDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Se modifica el view para los detalles de cada pokémon en específico
    // y se devuelve para que se muestre.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_detail, container, false);

        sprite = view.findViewById(R.id.ivSprite);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvTipo = view.findViewById(R.id.tvTipo);
        tvHabilidades = view.findViewById(R.id.tvHabilidades);
        tvPeso = view.findViewById(R.id.tvPeso);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String id = bundle.getString("id");
            String nombre;
            String urlImagen = "http://www.pokestadium.com/sprites/xy/";
            BDPokemon pokemons = new BDPokemon(getContext(), "BDPokemon", null, 1);

            Pokemon poke = pokemons.getPokemon(id);
            nombre = poke.getNombre().substring(0, 1).toLowerCase()
                    + poke.getNombre().substring(1);
            urlImagen += nombre + ".gif";
            if(sprite != null) {
                GlideApp.with(this)
                        .load(urlImagen)
                        .into(sprite);
            }

            // Asignación de datos al fragment.
            tvNombre.setText(Html.fromHtml("<b>#" + String.format("%03d", poke.getId())
                    + ". </b>" + poke.getNombre()));
            tvTipo.setText(Html.fromHtml("<b>Tipos: </b>"
                    + poke.getTipos()[0].getTipo().getNombre()));
            tvHabilidades.setText(Html.fromHtml("<b>Habilidades: </b>"
                    + poke.getHabilidades()[0].getHabilidad().getNombre()));
            tvPeso.setText(Html.fromHtml("<b>Peso: </b>" + poke.getPeso() + " kg "
                    + "  " + "<b>Altura: </b>" + poke.getAltura() + " m"));

            localizacion = view.findViewById(R.id.btn_localizacion);
            localizacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MapScreen.class);

                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitud", poke.getLatitud());
                    bundle.putDouble("longitud", poke.getLongitud());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            // Sonido de pokémon al clicar en imagen.
            sprite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SharedPreferences pref =
                            PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    if(pref.getBoolean("checkSonido", true))
                        PokeUtils.playSound(Integer.parseInt(id));
                }
            });

        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

package com.example.almu.pokedex2018;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface GetDataService {
    @GET("{filename}")
    @Streaming
    Call<Pokemon> getAllPokemon(@Path("filename") String filename);
}

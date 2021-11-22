package com.devgram.pokewiki.services

import com.devgram.pokewiki.model.Move
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.model.PokemonResponse
import com.devgram.pokewiki.model.TypeInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    fun  getPokemonList(@Query("limit") limit: Int?, @Query("offset") offset : Int?): Call<PokemonResponse>

    @GET("pokemon/{name}")
    fun getPokemon(@Path("name") name : String) : Call<Pokemon>

    @GET("type/{type}")
    fun getTypeDamage(@Path("type") name : String) : Call<TypeInfo>

    @GET("move/{move}")
    fun getMoveInfo(@Path("move") move : String) : Call<Move>
}
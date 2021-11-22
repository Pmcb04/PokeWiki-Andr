package com.devgram.pokewiki.util

import com.devgram.pokewiki.listeners.OnPokemonLoadedListener
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.services.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class PokemonNetworkLoaderRunnable(private val mOnPokemonLoadedListener: OnPokemonLoadedListener, private val limit: Int, private val offset: Int) : Runnable {

    override fun run() {
        // Create a very simple REST adapter wich points to the API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: PokemonService = retrofit.create(PokemonService::class.java)

        val pokemonList : MutableList<Pokemon> = mutableListOf()

        try {
            println("PETICION LISTA POKEMON")
            val pokemonResponse = service.getPokemonList(limit, offset).execute().body()
            println("IMPRIMIENDO LISTA POKEMON")
            if (pokemonResponse != null) {
                for (pok in pokemonResponse.results){
                    println(pok.name)
                    pokemonList.add(service.getPokemon(pok.name!!).execute().body()!!)
                }
            }
            AppExecutors.instance!!.mainThread().execute {
                mOnPokemonLoadedListener.onPokemonLoaded(pokemonList)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}

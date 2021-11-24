package com.devgram.pokewiki.util

import com.devgram.pokewiki.listeners.OnPokemonLoadedListener
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.services.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class PokemonSearchNetworkLoaderRunnable(private val mOnPokemonLoadedListener: OnPokemonLoadedListener, private val pokemon : String) : Runnable {

    override fun run() {
        // Create a very simple REST adapter wich points to the API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: PokemonService = retrofit.create(PokemonService::class.java)

        try {
            println("PETICION POKEMON")
            val pokemonResponse = service.getPokemon(pokemon).execute().body()
            println(pokemonResponse)
            AppExecutors.instance!!.mainThread().execute {
                mOnPokemonLoadedListener.onPokemonSearchLoaded(pokemonResponse)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}

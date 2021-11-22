package com.devgram.pokewiki.util

import com.devgram.pokewiki.listeners.OnPokemonLoadedListener
import com.devgram.pokewiki.listeners.OnTypeLoadedListener
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.services.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class TypeNetworkLoaderRunnable(private val mOnTypeLoadedListener: OnTypeLoadedListener, private val type: String) : Runnable {

    override fun run() {
        // Create a very simple REST adapter wich points to the API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: PokemonService = retrofit.create(PokemonService::class.java)

        try {
            println("PETICION TYPE INFO")
            val typeInfo = service.getTypeDamage(type).execute().body()
            AppExecutors.instance!!.mainThread().execute {
                mOnTypeLoadedListener.onTypeLoaded(typeInfo)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}

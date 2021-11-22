package com.devgram.pokewiki.util

import com.devgram.pokewiki.listeners.OnMoveLoadedListener
import com.devgram.pokewiki.listeners.OnPokemonLoadedListener
import com.devgram.pokewiki.listeners.OnTypeLoadedListener
import com.devgram.pokewiki.model.Info
import com.devgram.pokewiki.model.Move
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.services.PokemonService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MoveNetworkLoaderRunnable(private val mOnMoveLoadedListener: OnMoveLoadedListener, private val movements: List<Info>) : Runnable {

    override fun run() {
        // Create a very simple REST adapter wich points to the API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: PokemonService = retrofit.create(PokemonService::class.java)
        var movementsList : MutableList<Move> = mutableListOf()

        try {
            println("PETICION MOVE INFO")
            for (move in movements)
                movementsList.add(service.getMoveInfo(move.name!!).execute().body()!!)

            AppExecutors.instance!!.mainThread().execute {
                mOnMoveLoadedListener.onMoveLoaded(movementsList)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}

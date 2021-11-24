package com.devgram.pokewiki.listeners

import com.devgram.pokewiki.model.Pokemon

interface OnPokemonLoadedListener {
    fun onPokemonLoaded(pokemon: MutableList<Pokemon>)
    fun onPokemonSearchLoaded(pokemon : Pokemon?)
}
package com.devgram.pokewiki.listeners

import android.os.Bundle
import com.devgram.pokewiki.model.PokemonTeam

interface OnPokemonDetailListener {
    fun onPokemonDetailClick(team : Bundle, pokemonTeam : PokemonTeam?)
}
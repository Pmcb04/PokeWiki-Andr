package com.devgram.pokewiki.listeners

import android.os.Bundle
import android.view.View
import com.devgram.pokewiki.model.PokemonTeam

interface OnPokemonTeamListener {
    fun onClickExtendedFab()
    fun onClickArrowBack()
    fun onClickPokemon(team : Bundle, position: String, pokemonTeam: PokemonTeam?)
    fun onCreatePokemonTeam()
    fun onClickPokemonTeam(pokemonTeam : PokemonTeam)
}
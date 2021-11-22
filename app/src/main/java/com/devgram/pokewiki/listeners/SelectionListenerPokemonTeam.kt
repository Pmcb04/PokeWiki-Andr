package com.devgram.pokewiki.listeners

import com.devgram.pokewiki.model.PokemonTeam

interface SelectionListenerPokemonTeam {
    fun onListItemSelected(item: PokemonTeam?)
}

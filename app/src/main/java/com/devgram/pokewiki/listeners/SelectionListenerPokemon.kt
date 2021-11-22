package com.devgram.pokewiki.listeners

import android.os.Bundle
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.model.PokemonTeam

interface SelectionListenerPokemon {
    fun onListItemSelected(
        item: Pokemon,
        addTeam: Boolean,
        team: Bundle?,
        positionPokemonTeam: String?,
        pokemonTeam : PokemonTeam?
    )
}

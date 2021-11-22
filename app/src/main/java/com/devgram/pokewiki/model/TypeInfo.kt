package com.devgram.pokewiki.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TypeInfo (
    @SerializedName("id") @Expose var id: Int? = null,
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("damage_relations") @Expose var damageRelations: DamageRelations? = null,
    @SerializedName("pokemon") @Expose var pokemon: List<PokemonResponse>? = null
)
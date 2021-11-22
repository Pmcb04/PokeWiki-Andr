package com.devgram.pokewiki.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PokemonResponse (
    @SerializedName("count") @Expose val count : Int,
    @SerializedName("next") @Expose val next : String,
    @SerializedName("previous") @Expose val previous : String,
    @SerializedName("results") @Expose val results : List<Info>
)
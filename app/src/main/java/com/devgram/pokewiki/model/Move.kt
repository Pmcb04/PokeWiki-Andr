package com.devgram.pokewiki.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Move(
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("pp") @Expose var pp: String? = null,
    @SerializedName("type") @Expose var type: Type? = null,
    @SerializedName("power") @Expose var power: Int? = null
)


package com.devgram.pokewiki.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sprites (
    @SerializedName("back_default") @Expose var backDefault: String? = null,
    @SerializedName("back_female") @Expose var backFemale: Any? = null,
    @SerializedName("back_shiny") @Expose var backShiny: String? = null,
    @SerializedName("back_shiny_female") @Expose var backShinyFemale: Any? = null,
    @SerializedName("front_default") @Expose var frontDefault: String? = null,
    @SerializedName("front_female") @Expose var frontFemale: Any? = null,
    @SerializedName("front_shiny") @Expose var frontShiny: String? = null,
    @SerializedName("front_shiny_female") @Expose var frontShinyFemale: Any? = null
)
package com.devgram.pokewiki.model

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Stat (
    @SerializedName("base_stat") @Expose var baseStat: Int? = null,
    @SerializedName("effort") @Expose var effort: Int? = null
)
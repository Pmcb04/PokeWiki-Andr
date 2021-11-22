package com.devgram.pokewiki.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("name") @Expose var name: String? = null,
    @SerializedName("url") @Expose var url: String? = null
)
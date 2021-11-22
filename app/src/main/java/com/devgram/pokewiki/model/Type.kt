package com.devgram.pokewiki.model

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "types")
data class Type (
    @SerializedName("slot") @Expose var slot : Int? = null,
    @SerializedName("type") @Expose var type : Info? = null
)
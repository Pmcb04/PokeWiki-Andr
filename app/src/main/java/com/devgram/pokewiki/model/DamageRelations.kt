package com.devgram.pokewiki.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DamageRelations (

    @SerializedName("double_damage_from") @Expose val doubleDamageFrom : List<Info>,
    @SerializedName("double_damage_to") @Expose val doubleDamageTo : List<Info>,
    @SerializedName("half_damage_from") @Expose val halfDamageFrom : List<Info>,
    @SerializedName("half_damage_to") @Expose val halfDamageTo : List<Info>,
    @SerializedName("no_damage_from") @Expose val noDamageFrom : List<Info>,
    @SerializedName("no_damage_to") @Expose val noDamageTo : List<Info>

    )
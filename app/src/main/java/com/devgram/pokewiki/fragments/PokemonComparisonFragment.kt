package com.devgram.pokewiki.fragments

import android.graphics.Color.green
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import com.devgram.pokewiki.R
import com.devgram.pokewiki.model.Achievement
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [PokemonComparisonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonComparisonFragment(private val pok1 : Pokemon, private val pok2 : Pokemon) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_pokemon_comparison, container, false)

        //Sprite
        val v1 = v.findViewById<View>(R.id.pokemon_comp1)
        val v2 = v.findViewById<View>(R.id.pokemon_comp2)

        var subV1: View = v1.findViewById<View>(R.id.pokemon_sprit) as ImageView
        var subV2: View = v2.findViewById<View>(R.id.pokemon_sprit) as ImageView

        Picasso.get().load(pok1.sprites?.frontDefault).into(subV1 as ImageView)
        Picasso.get().load(pok2.sprites?.frontDefault).into(subV2 as ImageView)

        //Name
        subV1 = v1.findViewById<View>(R.id.pokemon_name) as TextView
        subV2 = v2.findViewById<View>(R.id.pokemon_name) as TextView

        subV1.text = pok1.name
        subV2.text = pok2.name

        //Ids
        subV1 = v1.findViewById<View>(R.id.pokemon_number) as TextView
        subV2 = v2.findViewById<View>(R.id.pokemon_number) as TextView

        subV1.text = pok1.id.toString()
        subV2.text = pok2.id.toString()

        //Types
        subV1 = v1.findViewById<View>(R.id.type1) as ImageView
        subV1.setImageResource(getDrawableIconType(pok1.types?.get(0)?.type?.name!!))
        subV1 = v1.findViewById<View>(R.id.type2) as ImageView
        if (pok1.types!!.size < 2) //si null is invisible = true
            subV1.isInvisible = true //TODO
        else
            subV1.setImageResource(getDrawableIconType(pok1.types?.get(1)?.type?.name!!))


        subV2 = v2.findViewById<View>(R.id.type1) as ImageView
        subV2.setImageResource(getDrawableIconType(pok2.types?.get(0)?.type?.name!!))
        subV2 = v2.findViewById<View>(R.id.type2) as ImageView
        if (pok2.types!!.size < 2) //si null is invisible = true
            subV2.isInvisible = true
        else
            subV2.setImageResource(getDrawableIconType(pok2.types?.get(1)?.type?.name!!))





        //Text Color
        var textCol1: TextView = v.findViewById<View>(R.id.tv_hp1) as TextView
        var textCol2: TextView = v.findViewById<View>(R.id.tv_hp2) as TextView
        textCol1.text = pok1.stats?.get(0)?.baseStat.toString()
        textCol2.text = pok2.stats?.get(0)?.baseStat.toString()
        setRowColor(textCol1, textCol2)

        textCol1 = v.findViewById<View>(R.id.tv_attack1)  as TextView
        textCol2 = v.findViewById<View>(R.id.tv_attack2) as TextView
        textCol1.text = pok1.stats?.get(1)?.baseStat.toString()
        textCol2.text = pok2.stats?.get(1)?.baseStat.toString()
        setRowColor(textCol1, textCol2)

        textCol1 = v.findViewById<View>(R.id.tv_defense1) as TextView
        textCol2 = v.findViewById<View>(R.id.tv_defense2) as TextView
        textCol1.text = pok1.stats?.get(2)?.baseStat.toString()
        textCol2.text = pok2.stats?.get(2)?.baseStat.toString()
        setRowColor(textCol1, textCol2)

        textCol1 = v.findViewById<View>(R.id.tv_speed1) as TextView
        textCol2 = v.findViewById<View>(R.id.tv_speed2) as TextView
        textCol1.text = pok1.stats?.get(3)?.baseStat.toString()
        textCol2.text = pok2.stats?.get(3)?.baseStat.toString()
        setRowColor(textCol1, textCol2)

        textCol1 = v.findViewById<View>(R.id.tv_spat1) as TextView
        textCol2 = v.findViewById<View>(R.id.tv_spat2) as TextView
        textCol1.text = pok1.stats?.get(4)?.baseStat.toString()
        textCol2.text = pok2.stats?.get(4)?.baseStat.toString()
        setRowColor(textCol1, textCol2)

        textCol1 = v.findViewById<View>(R.id.tv_spdf1) as TextView
        textCol2 = v.findViewById<View>(R.id.tv_spdf2) as TextView
        textCol1.text = pok1.stats?.get(5)?.baseStat.toString()
        textCol2.text = pok2.stats?.get(5)?.baseStat.toString()
        setRowColor(textCol1, textCol2)

        return v
    }

    private fun setRowColor(text1 : TextView, text2 : TextView){

        when{
            text1.text.toString().toInt() > text2.text.toString().toInt() -> text1.setTextColor(resources.getColor(R.color.green))
            text1.text.toString().toInt() < text2.text.toString().toInt() -> text2.setTextColor(resources.getColor(R.color.green))
            else -> { text1.setTextColor(resources.getColor(R.color.green)); text2.setTextColor(resources.getColor(R.color.green)) }
        }

    }

    private fun getDrawableIconType(type:String) : Int{
        return when(type){
            "bug" -> R.drawable.type_bug
            "dark" -> R.drawable.type_dark
            "dragon" -> R.drawable.type_dragon
            "electric" -> R.drawable.type_electric
            "fairy" -> R.drawable.type_fairy
            "fighting" -> R.drawable.type_fighting
            "fire" -> R.drawable.type_fire
            "flying" -> R.drawable.type_flying
            "ghost" -> R.drawable.type_ghost
            "grass" -> R.drawable.type_grass
            "ground" -> R.drawable.type_ground
            "ice" -> R.drawable.type_ice
            "normal" -> R.drawable.type_normal
            "poison" -> R.drawable.type_poison
            "psychic" -> R.drawable.type_psychic
            "rock" -> R.drawable.type_rock
            "steel" -> R.drawable.type_steel
            "water" -> R.drawable.type_water
            else -> R.drawable.type_normal
        }
    }

}


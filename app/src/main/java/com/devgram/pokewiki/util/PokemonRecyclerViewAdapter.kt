package com.devgram.pokewiki.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.devgram.pokewiki.R
import com.devgram.pokewiki.listeners.SelectionListenerPokemon
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.model.PokemonFavorite
import com.devgram.pokewiki.model.PokemonTeam
import com.devgram.pokewiki.roomdb.AppDatabase
import com.squareup.picasso.Picasso

class PokemonRecyclerViewAdapter(
    private val context: Context,
    private val addTeam: Boolean,
    private val team: Bundle?,
    private val positionPokemonTeam: String?,
    private val pokemonTeam: PokemonTeam?,
    callback: SelectionListenerPokemon,
    items: MutableList<Pokemon>) :

    RecyclerView.Adapter<PokemonRecyclerViewAdapter.PokemonViewHolder>() {

    private val mCallBack: SelectionListenerPokemon = callback
    private var pokemonList: MutableList<Pokemon> = items
    private lateinit var mOnClickListener: View.OnClickListener
    private val db = AppDatabase.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_content, parent, false)

        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {

        holder.name.text = pokemonList[position].name?.capitalize()
        holder.number.text = String.format("#%03d" , pokemonList[position].id)
        Picasso.get().load(pokemonList[position].sprites!!.frontDefault).into(holder.sprit)

        holder.type1.setImageResource(getDrawableIconType(pokemonList[position].types?.get(0)?.type?.name!!))

        if(pokemonList[position].types?.size == 2) // si tenemos dos slot, el pokemon tiene dos tipos
            holder.type2.setImageResource(getDrawableIconType(pokemonList[position].types?.get(1)?.type?.name!!))
        else
            holder.type2.isVisible = false

        mOnClickListener = View.OnClickListener { view ->
            val item: Pokemon = view.tag as Pokemon
            println(item)
            mCallBack.onListItemSelected(item, addTeam, team, positionPokemonTeam, pokemonTeam)
        }

        AppExecutors.instance?.diskIO()?.execute { // por cada pokemon de la lista comprueba si esta entre los favoritos del usuario
            val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val favorite = db.getFavoritePokemonDao().isFavorite(pokemonList[position].id!!, sp.getString("Actual_User", "")!!)

            if(favorite != 0) { // si devuelve 0 significa que no lo ha encontrado
                holder.favoriteButtom.setImageResource(R.drawable.ic_baseline_favorite_24)
            }else{
                holder.favoriteButtom.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

        }


        holder.itemView.setOnLongClickListener {
            AppExecutors.instance?.diskIO()?.execute { checkFavorite(position, holder) }
            true
        }

        holder.favoriteButtom.setOnClickListener { checkFavorite(position, holder) }

        holder.itemView.tag = pokemonList[position]
        holder.itemView.setOnClickListener(mOnClickListener)
    }

    private fun checkFavorite(position: Int, holder : PokemonViewHolder) {
        val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val favorite = db.getFavoritePokemonDao().isFavorite(pokemonList[position].id!!, sp.getString("Actual_User", "")!!)

        if(favorite > 0) {
            println("borrado")  // si devuelve > 0 significa que lo ha encontrado en la tabla de favoritos
            db.getFavoritePokemonDao().delete(favorite)
            holder.favoriteButtom.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }else{  // si devuelve 0 significa que no lo ha encontrado en la tabla de favoritos
            println("insercion")
            if(db.getPokemonDao().exist(pokemonList[position].id!!) != pokemonList[position].id!!)
                db.getPokemonDao().insert(pokemonList[position])
            val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            db.getFavoritePokemonDao().insert(PokemonFavorite( sp.getString("Actual_User", "")!!, pokemonList[position].id!!))
            holder.favoriteButtom.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var sprit : ImageView = view.findViewById(R.id.pokemon_sprit)
        var name : TextView = view.findViewById(R.id.pokemon_name)
        var number : TextView = view.findViewById(R.id.pokemon_number)
        var type1 : ImageView = view.findViewById(R.id.type1)
        var type2 : ImageView = view.findViewById(R.id.type2)
        var favoriteButtom : ImageButton = view.findViewById(R.id.favorite)
    }

    fun load(list: MutableList<Pokemon>) {
        pokemonList.addAll(list)
        notifyDataSetChanged()
    }

    fun loadClear(list: MutableList<Pokemon>) {
        pokemonList.clear()
        pokemonList.addAll(list)
        notifyDataSetChanged()
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

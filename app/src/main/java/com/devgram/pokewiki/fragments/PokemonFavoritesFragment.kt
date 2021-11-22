package com.devgram.pokewiki.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.view.isInvisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.devgram.pokewiki.R
import com.devgram.pokewiki.listeners.SelectionListenerPokemon
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.devgram.pokewiki.util.PokemonRecyclerViewAdapter
import java.lang.ClassCastException


/**
 * A simple [Fragment] subclass.
 * Use the [PokemonFavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonFavoritesFragment : Fragment() {

    private lateinit var mCallback: SelectionListenerPokemon
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PokemonRecyclerViewAdapter
    private lateinit var textEmpyList : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_pokemon_favorites, container, false)
        val include = v.findViewById(R.id.pokemon_favorites_list) as View
        mRecyclerView = include.findViewById(R.id.pokemon_list) as RecyclerView
        textEmpyList = v.findViewById(R.id.text_empty_favorite)

        val db = AppDatabase.getInstance(requireContext())

        val pokemonFavoritesList = mutableListOf<Pokemon>()

        AppExecutors.instance!!.diskIO().execute {
            val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val listFavorites = db.getFavoritePokemonDao().getAll(sp.getString("Actual_User","")!!)
            val listFavoritesPokemon = mutableListOf<Pokemon>()

            if(listFavorites.size > 0) // como la lista no esta vacia quitamos mensaje
                textEmpyList.isInvisible = true

            for (pok in listFavorites)
             listFavoritesPokemon.add(db.getPokemonDao().get(pok.id_pokemon))

            //insert into adapter list
            activity?.runOnUiThread {
                mAdapter.load(listFavoritesPokemon)
            }
        }

        mAdapter = PokemonRecyclerViewAdapter(requireContext(), false,null, null,  null, mCallback, pokemonFavoritesList)

        mRecyclerView.adapter = mAdapter

        return v
    }


    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        mCallback = try {
            context as SelectionListenerPokemon
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement SelectionListener"
            )
        }
    }
    
}
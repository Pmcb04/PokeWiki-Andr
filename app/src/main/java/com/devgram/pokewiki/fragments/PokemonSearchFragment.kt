package com.devgram.pokewiki.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.devgram.pokewiki.R
import com.devgram.pokewiki.util.PokemonRecyclerViewAdapter
import com.devgram.pokewiki.listeners.SelectionListenerPokemon
import com.devgram.pokewiki.listeners.OnPokemonLoadedListener
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.util.AppExecutors
import com.devgram.pokewiki.util.PokemonNetworkLoaderRunnable
import java.lang.ClassCastException
import android.widget.Toast
import com.devgram.pokewiki.model.PokemonTeam
import com.devgram.pokewiki.util.PokemonSearchNetworkLoaderRunnable
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "addTeam"
private const val ARG_PARAM2 = "team"
private const val ARG_PARAM3 = "position"
private const val ARG_PARAM4 = "pokemonTeam"

private const val MAX_POKEMON = 1118

/**
 * A simple [Fragment] subclass.
 * Use the [PokemonSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonSearchFragment : Fragment() {

    private var addTeam: Boolean? = null
    private var team: Bundle? = null
    private var position : String? = null
    private var pokemonTeam : PokemonTeam? = null

    private lateinit var search : EditText
    private lateinit var mCallback: SelectionListenerPokemon
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PokemonRecyclerViewAdapter
    private lateinit var lupa : ImageView

    private var offset : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addTeam = it.getBoolean(ARG_PARAM1)
            team = it.getBundle(ARG_PARAM2)
            position = it.getString(ARG_PARAM3)
            if(it.getSerializable(ARG_PARAM4) != null)
                pokemonTeam = it.getSerializable(ARG_PARAM4) as PokemonTeam
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_pokemon_search, container, false)

        search = v.findViewById(R.id.search)

        lupa = v.findViewById(R.id.lupa)
        lupa.setOnClickListener {
            if(search.text.toString().isNotEmpty()) filter(search.text.toString().lowercase())
        }

        mRecyclerView = v.findViewById(R.id.pokemon_list) as RecyclerView

        val pokemonSearchList = mutableListOf<Pokemon>()

        mAdapter = PokemonRecyclerViewAdapter(requireContext(), addTeam!!, team, position, pokemonTeam, mCallback,  pokemonSearchList)

        addPokemon()

        mRecyclerView.adapter = mAdapter

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) { // scroll hacia abajo
                    Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()

                    if(offset < MAX_POKEMON) {
                        offset += LIMIT
                        addPokemon()
                    }
                }

                if (!recyclerView.canScrollVertically(-1)) { // scroll hacia arriba
                    Toast.makeText(context, "Up", Toast.LENGTH_LONG).show()

                    if (offset > 0){
                        offset -= LIMIT
                        addPokemon()
                    }
                }
            }
        })

        return v

    }

    private fun filter(text: String) {

        AppExecutors.instance!!.networkIO().execute(
            PokemonSearchNetworkLoaderRunnable(
                object : OnPokemonLoadedListener {
                    override fun onPokemonLoaded(pokemon: MutableList<Pokemon>) {}
                    override fun onPokemonSearchLoaded(pokemon: Pokemon?) {
                        if(pokemon != null){
                            println("pokemon no nulo")
                            mAdapter.loadClear(mutableListOf(pokemon))
                        }
                    }
                }, text)
        )
    }

    private fun addPokemon(){
        println("addPokemon")
        AppExecutors.instance!!.networkIO().execute(
            PokemonNetworkLoaderRunnable(
                object : OnPokemonLoadedListener {
                    override fun onPokemonLoaded(pokemon: MutableList<Pokemon>) {
                        mAdapter.load(pokemon)
                    }
                    override fun onPokemonSearchLoaded(pokemon: Pokemon?) {}
                }, LIMIT, offset)
        )
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param addTeam Parameter indicate that is a new Pokemon team
         * @return A new instance of fragment PokemonTeamsDetailFragment.
         */
        @JvmStatic
        fun newInstance(addTeam: Boolean, team: Bundle?, position: String?, pokemonTeam : PokemonTeam?) =
            PokemonSearchFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, addTeam)
                    putBundle(ARG_PARAM2, team)
                    putString(ARG_PARAM3, position)
                    Log.i("PokemonSearchNewInst", pokemonTeam.toString())
                    putSerializable(ARG_PARAM4, pokemonTeam)
                }
            }

        private const val LIMIT : Int = 10
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = try {
            context as SelectionListenerPokemon
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement SelectionListenerPokemon"
            )
        }
    }

}
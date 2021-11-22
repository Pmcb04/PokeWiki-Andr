package com.devgram.pokewiki.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isInvisible
import androidx.preference.PreferenceManager

import androidx.recyclerview.widget.RecyclerView
import com.devgram.pokewiki.R
import com.devgram.pokewiki.listeners.OnPokemonTeamListener
import com.devgram.pokewiki.listeners.SelectionListenerPokemon
import com.devgram.pokewiki.listeners.SelectionListenerPokemonTeam
import com.devgram.pokewiki.model.*
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.squareup.picasso.Picasso
import java.lang.ClassCastException


/**
 * A simple [Fragment] subclass.
 * Use the [PokemonTeamsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonTeamsFragment : Fragment() {

    private lateinit var mCallback: OnPokemonTeamListener
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PokemonTeamsRecyclerViewAdapter
    private lateinit var mExtendedFab : ExtendedFloatingActionButton
    private lateinit var message : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_pokemon_teams, container, false)
        mRecyclerView = v.findViewById<View>(R.id.pokemonTeam_list) as RecyclerView
        mExtendedFab = v.findViewById(R.id.extended_fab)
        message = v.findViewById(R.id.message)

        mExtendedFab.setOnClickListener {
            mCallback.onClickExtendedFab() // cuando pulsemos el boton de crear, llamamos a el callback para que la MainActivity cambie de fragment
        }


        val db = AppDatabase.getInstance(requireContext())

        val pokemonTeamsList = mutableListOf<PokemonTeam>()


        AppExecutors.instance!!.diskIO().execute {
            val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val list = db.getPokemonTeamDao().getAll(sp.getString("Actual_User", "")!!)

            if(list.size > 0)
                message.isInvisible = true

            //insert into adapter list
            activity?.runOnUiThread { mAdapter.load(list) }
        }


        mAdapter = PokemonTeamsRecyclerViewAdapter( pokemonTeamsList, mCallback, requireContext())
        mRecyclerView.adapter = mAdapter
        return v
    }

    class PokemonTeamsRecyclerViewAdapter(items: MutableList<PokemonTeam>,
                                          private val mCallBack: OnPokemonTeamListener,
                                          private val context: Context
    ) :
        RecyclerView.Adapter<PokemonTeamsRecyclerViewAdapter.PokemonTeamsViewHolder>() {

        private var pokemonTeamsList: MutableList<PokemonTeam> = items
        private lateinit var mOnClickListener: View.OnClickListener

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTeamsViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.pokemon_team_list_content, parent, false)
            return PokemonTeamsViewHolder(view)
        }

        override fun onBindViewHolder(holder: PokemonTeamsViewHolder, position: Int) {

            Picasso.get().load(pokemonTeamsList[position].pokemon_team[0].sprites?.frontDefault).into(holder.pokemon1)
            Picasso.get().load(pokemonTeamsList[position].pokemon_team[1].sprites?.frontDefault).into(holder.pokemon2)
            Picasso.get().load(pokemonTeamsList[position].pokemon_team[2].sprites?.frontDefault).into(holder.pokemon3)
            Picasso.get().load(pokemonTeamsList[position].pokemon_team[3].sprites?.frontDefault).into(holder.pokemon4)
            Picasso.get().load(pokemonTeamsList[position].pokemon_team[4].sprites?.frontDefault).into(holder.pokemon5)
            Picasso.get().load(pokemonTeamsList[position].pokemon_team[5].sprites?.frontDefault).into(holder.pokemon6)

            holder.teamName.text = pokemonTeamsList[position].team_name

            holder.itemView.tag = pokemonTeamsList[position]

            mOnClickListener = View.OnClickListener { view ->
                val item: PokemonTeam = view.tag as PokemonTeam
                println(item)
                mCallBack.onClickPokemonTeam(item)
                println("aqui")
            }

            holder.itemView.setOnLongClickListener {

                val db = AppDatabase.getInstance(context)

                AppExecutors.instance!!.diskIO().execute {
                    db.getPokemonTeamDao().delete(pokemonTeamsList[position])
                    holder.itemView.isInvisible = true
                }
                true
            }

            holder.itemView.setOnClickListener(mOnClickListener)
        }

        override fun getItemCount(): Int {
            return pokemonTeamsList.size
        }

        inner class PokemonTeamsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var pokemon1: ImageView = view.findViewById<View>(R.id.pokemon_1) as ImageView
            var pokemon2: ImageView = view.findViewById<View>(R.id.pokemon_2) as ImageView
            var pokemon3: ImageView = view.findViewById<View>(R.id.pokemon_3) as ImageView
            var pokemon4: ImageView = view.findViewById<View>(R.id.pokemon_4) as ImageView
            var pokemon5: ImageView = view.findViewById<View>(R.id.pokemon_5) as ImageView
            var pokemon6: ImageView = view.findViewById<View>(R.id.pokemon_6) as ImageView
            var teamName : TextView = view.findViewById(R.id.team_name)

        }

        fun load(list: MutableList<PokemonTeam>) {
            pokemonTeamsList.clear()
            pokemonTeamsList = list
            notifyDataSetChanged()
        }
    }

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        mCallback = try {
            context as OnPokemonTeamListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement SelectionListener"
            )
        }
    }


}
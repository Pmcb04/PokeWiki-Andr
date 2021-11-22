package com.devgram.pokewiki.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.preference.PreferenceManager
import com.devgram.pokewiki.R
import com.devgram.pokewiki.listeners.OnPokemonTeamListener
import com.devgram.pokewiki.model.Achievement
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.model.PokemonTeam
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import java.lang.ClassCastException

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "newTeam"
private const val ARG_PARAM2 = "team"
private const val ARG_PARAM3 = "pokemonTeam"

/**
 * A simple [Fragment] subclass.
 * Use the [PokemonTeamsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonTeamsDetailFragment : Fragment() {
    private var newTeam: Boolean? = null
    private var pokemonTeam : PokemonTeam? = null
    private var mCallback : OnPokemonTeamListener? = null
    private lateinit var pokemon1 : View
    private lateinit var pokemon2 : View
    private lateinit var pokemon3 : View
    private lateinit var pokemon4 : View
    private lateinit var pokemon5 : View
    private lateinit var pokemon6 : View
    private lateinit var arrowBack : ImageView
    private lateinit var team : Bundle
    private lateinit var create_team : Button
    private lateinit var db : AppDatabase
    private lateinit var team_name : EditText

    private lateinit var pokemonDataList : MutableList<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newTeam = it.getBoolean(ARG_PARAM1)
            team = it.getBundle(ARG_PARAM2)!!
            if(it.getSerializable(ARG_PARAM3) != null)
                pokemonTeam = it.getSerializable(ARG_PARAM3) as PokemonTeam
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_pokemon_teams_detail, container, false)

        db = AppDatabase.getInstance(requireContext())

        pokemon1 = v.findViewById(R.id.pokemon_1)
        pokemon2 = v.findViewById(R.id.pokemon_2)
        pokemon3 = v.findViewById(R.id.pokemon_3)
        pokemon4 = v.findViewById(R.id.pokemon_4)
        pokemon5 = v.findViewById(R.id.pokemon_5)
        pokemon6 = v.findViewById(R.id.pokemon_6)

        pokemonDataList = mutableListOf()

        team_name = v.findViewById(R.id.team_name)

        create_team = v.findViewById(R.id.create_team)
        create_team.setOnClickListener {

            if(team_name.text.toString().isEmpty()){
                MaterialAlertDialogBuilder(requireContext(),
                    R.style.Theme_MaterialComponents_DayNight_Dialog_Alert)
                    .setTitle(R.string.title_team_name_not_found)
                    .setMessage(resources.getString(R.string.team_name_not_found))
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        // Respond to positive button press
                    }
                    .show()
            }else if(team.keySet().size < 6){
                println("SIZE ${team.keySet().size}")
                MaterialAlertDialogBuilder(requireContext(),
                    R.style.Theme_MaterialComponents_DayNight_Dialog_Alert)
                    .setTitle(R.string.title_cannot_create_team)
                    .setMessage(resources.getString(R.string.cannot_create_team))
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        // Respond to positive button press
                    }
                    .show()
            }else{

                AppExecutors.instance!!.diskIO().execute {
                    val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

                    if(!newTeam!!){
                        pokemonTeam?.team_name = team_name.text.toString()
                        db.getPokemonTeamDao().update(pokemonTeam!!)
                    }
                    else
                        db.getPokemonTeamDao().insert(PokemonTeam(pokemonDataList, sp.getString("Actual_User", "")!! , team_name.text.toString()))

                        val updateAchievement: Achievement = db.getAchievementDao().get(resources.getString((R.string.create_1_team_title)),sp.getString("Actual_User", "")!! )
                        updateAchievement.percentage = 100
                        db.getAchievementDao().update(updateAchievement)

                    mCallback?.onCreatePokemonTeam()

                }

            }
        }


        arrowBack = v.findViewById(R.id.arrow_back)
        arrowBack.setOnClickListener { mCallback?.onClickArrowBack() }

        if(newTeam!!){
            newTeamScreen()
        }

        pokemon1.setOnClickListener {  if(!team.keySet().contains("pokemon1")) mCallback?.onClickPokemon(team, "pokemon1", pokemonTeam) }
        pokemon2.setOnClickListener {  if(!team.keySet().contains("pokemon2")) mCallback?.onClickPokemon(team, "pokemon2", pokemonTeam) }
        pokemon3.setOnClickListener {  if(!team.keySet().contains("pokemon3")) mCallback?.onClickPokemon(team, "pokemon3", pokemonTeam) }
        pokemon4.setOnClickListener {  if(!team.keySet().contains("pokemon4")) mCallback?.onClickPokemon(team, "pokemon4", pokemonTeam) }
        pokemon5.setOnClickListener {  if(!team.keySet().contains("pokemon5")) mCallback?.onClickPokemon(team, "pokemon5", pokemonTeam) }
        pokemon6.setOnClickListener {  if(!team.keySet().contains("pokemon6")) mCallback?.onClickPokemon(team, "pokemon6", pokemonTeam) }

        pokemon1.setOnLongClickListener { team.remove("pokemon1") ; addIconView(pokemon1) ; true }
        pokemon2.setOnLongClickListener { team.remove("pokemon2") ; addIconView(pokemon2) ; true }
        pokemon3.setOnLongClickListener { team.remove("pokemon3") ; addIconView(pokemon3) ; true }
        pokemon4.setOnLongClickListener { team.remove("pokemon4") ; addIconView(pokemon4) ; true }
        pokemon5.setOnLongClickListener { team.remove("pokemon5") ; addIconView(pokemon5) ; true }
        pokemon6.setOnLongClickListener { team.remove("pokemon6") ; addIconView(pokemon6) ; true }



        if(!team.isEmpty){

            println("KEY TEAM")
            for (key in team.keySet()){
                println(key)
            }

            if(team.keySet().contains("team_name")){
                team_name.setText(team.getString("team_name"))
                create_team.text = resources.getString(R.string.edit)
            }

            if(team.keySet().contains("pokemon1")){
                val pokemon1Data : Pokemon = team.getSerializable("pokemon1") as Pokemon
                pokemonDataList.add(pokemon1Data)
                if(pokemonTeam != null) pokemonTeam!!.pokemon_team[0] = pokemon1Data
                println("pokemon1$pokemon1Data")
                setPokemon(pokemon1, pokemon1Data)
            }

            if(team.keySet().contains("pokemon2")){
                val pokemon2Data : Pokemon = team.getSerializable("pokemon2") as Pokemon
                pokemonDataList.add(pokemon2Data)
                if(pokemonTeam != null) pokemonTeam!!.pokemon_team[1] = pokemon2Data
                println("pokemon2$pokemon2Data")
                setPokemon(pokemon2, pokemon2Data)
            }

            if(team.keySet().contains("pokemon3")){
                val pokemon3Data : Pokemon = team.getSerializable("pokemon3") as Pokemon
                pokemonDataList.add(pokemon3Data)
                if(pokemonTeam != null) pokemonTeam!!.pokemon_team[2] = pokemon3Data
                println("pokemon3$pokemon3Data")
                setPokemon(pokemon3, pokemon3Data)
            }

            if(team.keySet().contains("pokemon4")){
                val pokemon4Data : Pokemon = team.getSerializable("pokemon4") as Pokemon
                pokemonDataList.add(pokemon4Data)
                if(pokemonTeam != null) pokemonTeam!!.pokemon_team[3] = pokemon4Data
                println("pokemon4$pokemon4Data")
                setPokemon(pokemon4, pokemon4Data)
            }

            if(team.keySet().contains("pokemon5")){
                val pokemon5Data : Pokemon = team.getSerializable("pokemon5") as Pokemon
                pokemonDataList.add(pokemon5Data)
                if(pokemonTeam != null) pokemonTeam!!.pokemon_team[4] = pokemon5Data
                println("pokemon5$pokemon5Data")
                setPokemon(pokemon5, pokemon5Data)
            }

            if(team.keySet().contains("pokemon6")){
                val pokemon6Data : Pokemon = team.getSerializable("pokemon6") as Pokemon
                pokemonDataList.add(pokemon6Data)
                if(pokemonTeam != null) pokemonTeam!!.pokemon_team[5] = pokemon6Data
                println("pokemon6$pokemon6Data")
                setPokemon(pokemon6, pokemon6Data)
            }

        }

        return v
    }

    private fun newTeamScreen() {
        addIconView(pokemon1)
        addIconView(pokemon2)
        addIconView(pokemon3)
        addIconView(pokemon4)
        addIconView(pokemon5)
        addIconView(pokemon6)
    }

    private fun setPokemon(view : View, pokemon : Pokemon){
        val name : TextView = view.findViewById(R.id.pokemon_name)
        val sprite : ImageView = view.findViewById(R.id.pokemon_sprit)
        val number : TextView = view.findViewById(R.id.pokemon_number)
        val type1 : ImageView = view.findViewById(R.id.type1)
        val type2 : ImageView = view.findViewById(R.id.type2)
        val favoriteButtom : ImageView = view.findViewById(R.id.favorite)


        name.text = pokemon.name?.capitalize()
        number.text = String.format("#%03d" , pokemon.id)
        Picasso.get().load(pokemon.sprites?.frontDefault).into(sprite)
        type1.setImageResource(getDrawableIconType(pokemon.types!![0].type?.name!!))
        if(pokemon.types?.size == 2) type2.setImageResource(getDrawableIconType(pokemon.types!![1].type?.name!!))
        else type2.isInvisible = true



        AppExecutors.instance?.diskIO()?.execute {

            val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val favorite = db.getFavoritePokemonDao().isFavorite(pokemon.id!!,sp.getString("Actual_User", "")!!)

            if(favorite != 0) { // si devuelve 0 significa que no lo ha encontrado
                favoriteButtom.setImageResource(R.drawable.ic_baseline_favorite_24)
            }else{
                favoriteButtom.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

            activity?.runOnUiThread { favoriteButtom.isInvisible = false  }

        }

    }

    private fun addIconView(view : View){
        val name : TextView = view.findViewById(R.id.pokemon_name)
        val sprite : ImageView = view.findViewById(R.id.pokemon_sprit)
        val number : TextView = view.findViewById(R.id.pokemon_number)
        val type1 : ImageView = view.findViewById(R.id.type1)
        val type2 : ImageView = view.findViewById(R.id.type2)
        val favoriteButtom : ImageView = view.findViewById(R.id.favorite)

        name.text = ""
        number.text = ""
        sprite.setImageResource(R.drawable.ic_baseline_add_24)
        type1.isInvisible = true
        type2.isInvisible = true
        favoriteButtom.isInvisible = true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param newTeam Parameter indicate that is a new Pokemon team
         * @return A new instance of fragment PokemonTeamsDetailFragment.
         */
        @JvmStatic
        fun newInstance(newTeam: Boolean, team: Bundle, pokemonTeam: PokemonTeam?) =
            PokemonTeamsDetailFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, newTeam)
                    putBundle(ARG_PARAM2, team)
                    Log.i("PokemonTeamsDetNewInst", pokemonTeam.toString())
                    putSerializable(ARG_PARAM3, pokemonTeam)
                }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallback = try {
            context as OnPokemonTeamListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement SelectionListenerPokemon"
            )
        }
    }
}
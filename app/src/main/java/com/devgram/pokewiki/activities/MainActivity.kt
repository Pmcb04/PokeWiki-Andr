package com.devgram.pokewiki.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.devgram.pokewiki.R
import com.devgram.pokewiki.fragments.*
import com.devgram.pokewiki.listeners.OnPokemonDetailListener
import com.devgram.pokewiki.listeners.OnPokemonTeamListener
import com.devgram.pokewiki.listeners.SelectionListenerPokemon
import com.devgram.pokewiki.listeners.SelectionListenerPokemonTeam
import com.devgram.pokewiki.model.Achievement
import com.devgram.pokewiki.model.Pokemon
import com.devgram.pokewiki.model.PokemonTeam
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), SelectionListenerPokemon, SelectionListenerPokemonTeam, OnPokemonTeamListener, OnPokemonDetailListener{

    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.menu.getItem(0).isCheckable = true
        replaceFragment(PokemonTeamsFragment())


        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.teams -> {
                    replaceFragment(PokemonTeamsFragment())
                    true
                }

                R.id.pokemon_favorites -> {
                    replaceFragment(PokemonFavoritesFragment())
                    true
                }

                R.id.pokemon_search -> {
                    replaceFragment(PokemonSearchFragment.newInstance(false, null, null, null))
                    true
                }

                R.id.achievement_list -> {
                    replaceFragment(AchievementFragment())
                    true
                }

                R.id.user -> {
                    replaceFragment(UserFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fr : Fragment){
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fragment,fr)
        frag.commit()
    }

    private fun addFragment(fr : Fragment){
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fragment,fr)
        frag.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        frag.addToBackStack(null)
        frag.commit()
    }

    override fun onListItemSelected(
        item: Pokemon,
        addTeam: Boolean,
        team: Bundle?,
        positionPokemonTeam: String?,
        pokemonTeam: PokemonTeam?
    ) {
       replaceFragment(PokemonDetailsFragment.newInstance(addTeam, item, team, positionPokemonTeam, pokemonTeam))
    }

    override fun onClickExtendedFab() {
        val team = Bundle()
        addFragment(PokemonTeamsDetailFragment.newInstance(true, team, null))
    }

    override fun onClickArrowBack() {
        when (supportFragmentManager.backStackEntryCount) {
            0 -> super.onBackPressed()
            1 -> moveTaskToBack(false)
            else -> supportFragmentManager.popBackStack()
        }
    }

    override fun onClickPokemon(team : Bundle, position : String, pokemonTeam : PokemonTeam?) {
        addFragment(PokemonSearchFragment.newInstance(true, team, position, pokemonTeam))
    }

    override fun onCreatePokemonTeam() {
        replaceFragment(PokemonTeamsFragment())
    }

    override fun onClickPokemonTeam(pokemonTeam : PokemonTeam) {
        println("onClickPokemonTeam")
        val team = Bundle()
        team.putSerializable("pokemon1", pokemonTeam.pokemon_team[0])
        team.putSerializable("pokemon2", pokemonTeam.pokemon_team[1])
        team.putSerializable("pokemon3", pokemonTeam.pokemon_team[2])
        team.putSerializable("pokemon4", pokemonTeam.pokemon_team[3])
        team.putSerializable("pokemon5", pokemonTeam.pokemon_team[4])
        team.putSerializable("pokemon6", pokemonTeam.pokemon_team[5])
        team.putString("team_name", pokemonTeam.team_name)
        replaceFragment(PokemonTeamsDetailFragment.newInstance(false, team, pokemonTeam))
    }

    override fun onPokemonDetailClick(team: Bundle, pokemonTeam: PokemonTeam?) {
        replaceFragment(PokemonTeamsDetailFragment.newInstance(pokemonTeam == null, team, pokemonTeam))
    }

    override fun onListItemSelected(item: PokemonTeam?) {
        println("pokemon team")
    }

} 

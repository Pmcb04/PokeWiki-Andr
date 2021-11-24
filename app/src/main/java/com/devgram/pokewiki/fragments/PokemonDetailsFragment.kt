package com.devgram.pokewiki.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.devgram.pokewiki.R
import com.devgram.pokewiki.model.Pokemon
import com.squareup.picasso.Picasso
import com.devgram.pokewiki.listeners.*
import com.devgram.pokewiki.model.Info
import com.devgram.pokewiki.model.TypeInfo
import com.devgram.pokewiki.util.AppExecutors
import com.devgram.pokewiki.util.RadarMarkerView
import com.devgram.pokewiki.util.TypeNetworkLoaderRunnable

import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import android.graphics.Typeface
import android.util.Log
import androidx.annotation.NonNull
import com.devgram.pokewiki.model.PokemonTeam
import java.lang.ClassCastException

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "newTeam"
private const val ARG_PARAM2 = "pokemon"
private const val ARG_PARAM3 = "team"
private const val ARG_PARAM4 = "position"
private const val ARG_PARAM5 = "pokemonTeam"


/**
 * A simple [Fragment] subclass.
 * Use the [PokemonDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonDetailsFragment : Fragment() {

    private var addTeam: Boolean? = null
    private lateinit var pokemon: Pokemon
    private var team : Bundle? = null
    private var position : String? = null
    private var pokemonTeam : PokemonTeam? = null

    private lateinit var imageViewType: ImageView
    private lateinit var linearLayout: LinearLayout
    private lateinit var button : Button
    private lateinit var chart : RadarChart
    private lateinit var tfLight: Typeface
    private lateinit var mCallback : OnPokemonDetailListener
    private var typesStrengths : MutableSet<String> = mutableSetOf()
    private var typesWeakness : MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addTeam = it.getBoolean(ARG_PARAM1)
            pokemon = it.getSerializable(ARG_PARAM2) as Pokemon
            team = it.getBundle(ARG_PARAM3)
            position = it.getString(ARG_PARAM4)
            if(it.getSerializable(ARG_PARAM5) != null)
                pokemonTeam = it.getSerializable(ARG_PARAM5) as PokemonTeam

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_pokemon_details, container, false)

        button = v.findViewById(R.id.bottom_action)

        if(addTeam == true) button.text = getString(R.string.add)
        else button.text = getString(R.string.compare)

        button.setOnClickListener {
            if(addTeam == true){
                team?.putSerializable(position, pokemon)
                println("obtenemos key")
                mCallback.onPokemonDetailClick(team!!, pokemonTeam)
            }else{
                // TODO comparar
            }
        }


        chart = v.findViewById(R.id.chart_radar)
        chart.setBackgroundColor(Color.rgb(60, 65, 82))
        tfLight = Typeface.createFromAsset(activity?.assets, "OpenSans-Light.ttf")

        chart.description.isEnabled = false

        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv: MarkerView = RadarMarkerView(requireContext(), R.layout.radar_makerview)
        mv.chartView = chart // For bounds control

        chart.marker = mv // Set the marker to the chart

        setData()

        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)

        val xAxis: XAxis = chart.xAxis
        xAxis.typeface = tfLight
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        /*xAxis.setValueFormatter(
            object : com.devgram.pokewiki.listeners.IAxisValueFormatter {
            private val mActivities = arrayOf("Burger", "Steak", "Salad", "Pasta", "Pizza")
            override fun getFormattedValue(value: Float, axis: AxisBase?): String? {
                return mActivities[value.toInt() % mActivities.size]
            }
        })*/
        xAxis.textColor = Color.WHITE

        val yAxis: YAxis = chart.yAxis
        yAxis.typeface = tfLight
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 165f
        yAxis.setDrawLabels(false)
/*
        val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.typeface = tfLight
        l.xEntrySpace = 7f
        l.yEntrySpace = 3f
        l.textColor = Color.WHITE
*/

        if(pokemon.types?.size == 2) {  // si tenemos dos slot, el pokemon tiene dos tipos
            AppExecutors.instance?.networkIO()?.execute (
                TypeNetworkLoaderRunnable(
                    object : OnTypeLoadedListener {
                        override fun onTypeLoaded(typeInfo: TypeInfo?) {
                            linearLayout = v.findViewById(R.id.types_strengths)
                            load(typeInfo!!.damageRelations!!.doubleDamageTo, typesStrengths)
                            //loadTypes(typesStrengths) // cargamos la lista de tipos a favor
                            linearLayout = v.findViewById(R.id.types_weakness)
                            load(typeInfo.damageRelations!!.doubleDamageFrom, typesWeakness)
                            //loadTypes(typesWeakness) // cargamos la lista de tipos en contra
                        }
                    }, pokemon.types?.get(1)?.type?.name!!) // obtenemos las debilidades y fortalezas del segundo tipo
            )

        }

        AppExecutors.instance?.networkIO()?.execute (

            TypeNetworkLoaderRunnable(
                object : OnTypeLoadedListener {
                    override fun onTypeLoaded(typeInfo: TypeInfo?) {
                        linearLayout = v.findViewById(R.id.types_strengths)
                        load(typeInfo!!.damageRelations!!.doubleDamageTo, typesStrengths)
                        loadTypes(typesStrengths) // cargamos la lista de tipos a favor
                        linearLayout = v.findViewById(R.id.types_weakness)
                        load(typeInfo.damageRelations!!.doubleDamageFrom, typesWeakness)
                        loadTypes(typesWeakness) // cargamos la lista de tipos en contra
                    }
                }, pokemon.types?.get(0)?.type?.name!!) // obtenemos las debilidades y fortalezas del primer tipo
        )


        // cargamos el sprite del pokemon
        val sprite :ImageView = v.findViewById(R.id.pokemon_sprit)
        Picasso.get().load(pokemon.sprites?.frontDefault).into(sprite)

        // cargamos el nombre del pokemon
        val name : TextView = v.findViewById(R.id.pokemon_name)
        name.text = pokemon.name?.capitalize()

        // cargamos el numero del pokemon
        val number : TextView = v.findViewById(R.id.pokemon_number)
        number.text = String.format("#%03d" , pokemon.id)

        return v
    }

    private fun setData() {
        val entries1: ArrayList<RadarEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (stat in pokemon.stats!!) {
            entries1.add(RadarEntry(stat.baseStat!!.toFloat()))
        }
        val set1 = RadarDataSet(entries1, null)
        set1.color = Color.rgb(103, 110, 129)
        set1.fillColor = Color.rgb(103, 110, 129)
        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 2f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)
        val sets: ArrayList<IRadarDataSet> = ArrayList()
        sets.add(set1)
        val data = RadarData(sets)
        data.setValueTypeface(tfLight)
        data.setValueTextSize(8f)
        data.setDrawValues(true)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        chart.invalidate()
    }


    private fun load(types : List<Info>, set : MutableSet<String>){
        for (type in types)
            if(type.name !in set)
                set.add(type.name!!)
    }

    private fun loadTypes(set : Set<String>){
        for (type in set){
                imageViewType = ImageView(context)
                imageViewType.setImageResource(getDrawableIconType(type))
                addImageView(linearLayout, imageViewType, 40, 40)
            }
        }


    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)
        mCallback = try {
            context as OnPokemonDetailListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$context must implement SelectionListener"
            )
        }
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
        fun newInstance(addTeam: Boolean, pokemon: Pokemon, team: Bundle?, position: String?, pokemonTeam : PokemonTeam?) =
            PokemonDetailsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, addTeam)
                    putSerializable(ARG_PARAM2, pokemon)
                    putBundle(ARG_PARAM3, team)
                    putString(ARG_PARAM4, position)
                    Log.i("PokemonDetailsNewInst", pokemonTeam.toString())
                    putSerializable(ARG_PARAM5, pokemonTeam)
                }
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

    private fun addImageView(layout: LinearLayout, imageView: ImageView, width: Int, height: Int) {
        val params = LinearLayout.LayoutParams(width, height)

        // setting the margin in linearlayout
        //params.setMargins(0, 10, 0, 10)
        imageView.layoutParams = params

        // adding the image in layout
        layout.addView(imageView)
    }



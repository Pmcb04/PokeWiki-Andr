package com.devgram.pokewiki.fragments

import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.devgram.pokewiki.R
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import java.util.*


class SettingsFragment : PreferenceFragmentCompat() {

    private var user_name : String = ""

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


        AppExecutors.instance?.diskIO()?.execute{
            user_name = sp.getString("Actual_User", "")!!
        }

        val box: SwitchPreferenceCompat? = findPreference("darkmode") as SwitchPreferenceCompat?
        val editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(activity)!!.edit()


        box!!.setOnPreferenceClickListener {
            editor.putBoolean("dark_mode_${user_name}", box.isChecked)
            editor.apply()
            changeTheme()
            true
        }

        val list : ListPreference = findPreference<ListPreference>("language_list")!!

        //List listener
        list.setOnPreferenceChangeListener { preference, newValue ->
            if (preference is ListPreference) {
                if((newValue == "English") or (newValue == "Inglés"))
                    editor.putString("language_${user_name}", "en")
                else
                    editor.putString("language_${user_name}", "es")
                editor.apply()
                changeLanguage()
            }

            true
        }

    }

    private fun changeTheme(){
        val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if(sp.getBoolean("dark_mode_${user_name}", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun changeLanguage(){
        val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val s : String? = sp.getString("language_${user_name}", "")
        if(s!! == "en") {
            updateResources("en")
        }else{
            updateResources("es")
        }
    }


    private fun updateResources(language: String) {
        val res : Resources = resources
        val dm : DisplayMetrics = res.displayMetrics
        val conf : Configuration = res.configuration
        conf.setLocale(Locale(language.lowercase()))
        Locale.setDefault(Locale(language));
        res.updateConfiguration(conf, dm)
        activity?.recreate()
    }



}
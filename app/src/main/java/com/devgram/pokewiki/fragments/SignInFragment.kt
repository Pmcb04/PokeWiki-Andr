package com.devgram.pokewiki.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.devgram.pokewiki.R
import com.devgram.pokewiki.activities.MainActivity
import com.devgram.pokewiki.model.User
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val db = AppDatabase.getInstance(requireContext())

        val inputName : EditText = v.findViewById(R.id.textSignInName)
        val inputPassword : EditText = v.findViewById(R.id.textSignInPassword)
        val btnLogin : Button = v.findViewById(R.id.buttonSignIn)


        btnLogin.setOnClickListener {

            AppExecutors.instance!!.diskIO().execute {

                println("Nombre: "+ inputName.text.toString())
                println("Password: "+ inputPassword.text.toString())

                if(db.getUserDao()?.getByName(inputName.text.toString())?.name == inputName.text.toString()) {
                    println("Es correcto el nombre")
                    if(db.getUserDao()?.getByName(inputName.text.toString())?.password == inputPassword.text.toString()){
                        println("Es correcta la contraseña")

                        val sesionUserE : SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
                            sesionUserE.putString("Actual_User",inputName.text.toString())
                            sesionUserE.apply()

                        AppExecutors.instance!!.mainThread().execute {

                            val sp : SharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
                            val editor: SharedPreferences.Editor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(activity)!!.edit()

                            val name : String? = sp.getString("Actual_User", "")
                            val dark_mode : Boolean = sp.getBoolean("dark_mode_${name}", false)
                            val language : String? = sp.getString("language_${name}", resources.getString(R.string.loc_eng))

                            editor.apply()

                            if(!isDarkTheme(requireActivity()) and dark_mode){
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            }

                            if(((Locale.getDefault().toString() != "en") and (language == "en")) or
                                ((Locale.getDefault().toString() != "es") and (language == "es"))){
                                updateResources(language!!)
                            }

                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }

                    }else{
                        activity?.runOnUiThread {
                            inputPassword.error = getString(R.string.SignInPasswordError)
                        }
                        println("No es correcta la contraseña")
                    }
                }else{
                    activity?.runOnUiThread {
                        inputName.error = getString(R.string.SignInNameError)
                    }
                    println("No es correcto el nombre")
                }

            }

        }

        return v
    }

    private fun isDarkTheme(activity: FragmentActivity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
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
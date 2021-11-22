package com.devgram.pokewiki.fragments

import android.app.Activity
import android.content.SharedPreferences
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.ColorFilter
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
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.FragmentActivity
import com.devgram.pokewiki.R
import com.devgram.pokewiki.activities.MainActivity
import com.devgram.pokewiki.model.Achievement
import com.devgram.pokewiki.model.User
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_sign_up, container, false)

        val db = AppDatabase.getInstance(requireContext())

        val inputName : EditText = v.findViewById(R.id.textSignUpName)
        val inputNameLayout : TextInputLayout = v.findViewById(R.id.SignUpName)
        val inputEmail : EditText = v.findViewById(R.id.textSignUpEmail)
        val inputPassword : EditText = v.findViewById(R.id.textSignUpPassword)
        val btnLogin : Button = v.findViewById(R.id.buttonSignUp)

        val sesionUser : SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()

        btnLogin.setOnClickListener {

            AppExecutors.instance!!.diskIO().execute {

                println("Nombre: "+ inputName.text.toString())
                println("Email: "+ inputEmail.text.toString())
                println("Password: "+ inputPassword.text.toString())

                val user : User? = db.getUserDao().getByName(inputName.text.toString())
                println(user)

                if(user != null && user.name == inputName.text.toString()) {
                    println("El nombre existe en la base de datos")
                    activity?.runOnUiThread {
                        inputName.error = getString(R.string.SignUpError)
                    }
                }else{
                    println("No existe el nombre ne la base de datos, valor correcto")

                    db.getUserDao().insert(User( inputName.text.toString(), inputEmail.text.toString(), inputPassword.text.toString() ))

                    sesionUser.putString("Actual_User",inputName.text.toString())
                    sesionUser.apply() //Guardar sharedPreference

                    AppExecutors.instance!!.diskIO().execute {

                        db.getAchievementDao().insert(Achievement(
                            resources.getString(R.string.create_1_team_title),
                            resources.getString(R.string.create_1_team_descr),
                            0,
                            inputName.text.toString()
                        ))

                    }


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
package com.devgram.pokewiki.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.devgram.pokewiki.R
import com.devgram.pokewiki.activities.InitialActivity
import com.devgram.pokewiki.roomdb.AppDatabase
import com.devgram.pokewiki.util.AppExecutors

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_user, container, false)

        val sesionUser : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val sesionUserE : SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
        val userName : TextView = v.findViewById(R.id.UserName)
        val email : TextView = v.findViewById(R.id.Email)
        val name : String? = sesionUser.getString("Actual_User","")
        val db = AppDatabase.getInstance(requireContext())

        AppExecutors.instance!!.diskIO().execute {
            val emailUser = db.getUserDao().getByName(name!!)?.email
            activity?.runOnUiThread {  email.text =  emailUser }

        }

        userName.text = name

        val btnSettings : ImageView = v.findViewById(R.id.Settings)

        btnSettings.setOnClickListener{

            val settingfragment = SettingsFragment()
            val frag = activity?.supportFragmentManager?.beginTransaction()
            frag?.replace(R.id.fragment,settingfragment)
            frag?.addToBackStack(null)
            frag?.commit()

        }


        val btnOff : Button = v.findViewById(R.id.SignOff)

        btnOff.setOnClickListener {
            sesionUserE.putString("Actual_User","")
            sesionUserE.apply() //Guardar sharedPreference

            activity?.finish()
            val intent = Intent(context, InitialActivity::class.java)
            startActivity(intent)
        }



        return v

    }

}
package com.devgram.pokewiki.activities
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.devgram.pokewiki.R
import java.util.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name : String? = sp.getString("Actual_User", "")
        val dark_mode : Boolean = sp.getBoolean("dark_mode_${name}", false)
        val language : String? = sp.getString("language_${name}", resources.getString(R.string.loc_eng))

        if(!isDarkTheme(this) and dark_mode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        if(((Locale.getDefault().toString() != "en") and (language == "en")) or
            ((Locale.getDefault().toString() != "es") and (language == "es"))){
            updateResources(language!!)
        }

        if(name == ""){
            // we used the postDelayed(Runnable, time) method
            // to send a message with a delayed time.
            Handler().postDelayed({
                val intent = Intent(this, InitialActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
        }else{
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
        }

    }

    private fun isDarkTheme(activity: Activity): Boolean {
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
        recreate()
    }


}
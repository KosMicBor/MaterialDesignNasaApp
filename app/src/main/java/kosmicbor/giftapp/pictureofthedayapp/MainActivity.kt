package kosmicbor.giftapp.pictureofthedayapp

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import kosmicbor.giftapp.pictureofthedayapp.domain.Theme
import kosmicbor.giftapp.pictureofthedayapp.utils.Router

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    companion object {
        private const val APP_PREFERENCES = "APP_PREFERENCES"
        private const val APP_PREFERENCES_NAME = "THEME"
        private const val APP_PREFERENCES_DARK_MODE = "DARK_MODE"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        var isThemeAlreadyChoosed = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getBoolean(APP_PREFERENCES_DARK_MODE, false)

        if (applicationContext.resources.configuration.isNightModeActive) {

            if (isThemeAlreadyChoosed) {
                setAppTheme()
            } else {
                isThemeAlreadyChoosed = true

                getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putBoolean(APP_PREFERENCES_DARK_MODE, isThemeAlreadyChoosed)
                    .putInt(APP_PREFERENCES_NAME, Theme.DARK_THEME.themeRes)
                    .apply()
            }

        } else {

            if (isThemeAlreadyChoosed) {
                isThemeAlreadyChoosed = false
                getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putBoolean(APP_PREFERENCES_DARK_MODE, isThemeAlreadyChoosed)
                    .putInt(APP_PREFERENCES_NAME, Theme.LIGHT_THEME.themeRes)
                    .apply()

            }
        }
        setAppTheme()

        if (savedInstanceState == null) {
            Router.openApodFragment(supportFragmentManager)
        }

        setBottomNavigationMenu()
    }

    private fun setBottomNavigationMenu() {
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_nav_bar_apod -> {
                        Router.openApodFragment(supportFragmentManager)
                        true
                    }

                    R.id.bottom_nav_view_settings -> {
                        Router.openSettings(supportFragmentManager)
                        true
                    }

                    R.id.bottom_nav_bar_earth -> {
                        Router.openEarthFragment(supportFragmentManager)
                        true
                    }

                    R.id.bottom_nav_bar_mars -> {
                        Router.openMarsFragment(supportFragmentManager)
                        true
                    }
                    else -> {
                        throw Exception(context.getString(R.string.bottom_menu_item_error))
                    }
                }
            }
        }
    }


    private fun setAppTheme() {
        val themeRes = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getInt(
            APP_PREFERENCES_NAME,
            Theme.LIGHT_THEME.themeRes
        )
        setTheme(themeRes)
    }
}
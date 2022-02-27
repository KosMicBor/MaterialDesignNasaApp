package kosmicbor.giftapp.pictureofthedayapp

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import kosmicbor.giftapp.pictureofthedayapp.domain.Theme
import kosmicbor.giftapp.pictureofthedayapp.ui.main.ApodFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val APP_PREFERENCES = "APP_PREFERENCES"
        private const val APP_PREFERENCES_NAME = "THEME"
        private const val APP_PREFERENCES_DARK_MODE = "DARK_MODE"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setContentView(R.layout.main_activity)

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
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ApodFragment.newInstance())
                .commitNow()
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
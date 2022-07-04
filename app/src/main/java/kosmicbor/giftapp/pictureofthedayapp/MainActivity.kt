package kosmicbor.giftapp.pictureofthedayapp

import android.content.Context
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kosmicbor.giftapp.pictureofthedayapp.domain.AppTheme
import kosmicbor.giftapp.pictureofthedayapp.ui.main.FragmentApod
import kosmicbor.giftapp.pictureofthedayapp.ui.main.FragmentEpic
import kosmicbor.giftapp.pictureofthedayapp.ui.main.FragmentMars
import kosmicbor.giftapp.pictureofthedayapp.ui.main.FragmentSettings
import kosmicbor.giftapp.pictureofthedayapp.utils.replaceFragment

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
                    .putInt(APP_PREFERENCES_NAME, AppTheme.DARK_THEME.themeRes)
                    .apply()
            }

        } else {

            if (isThemeAlreadyChoosed) {
                isThemeAlreadyChoosed = false
                getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putBoolean(APP_PREFERENCES_DARK_MODE, isThemeAlreadyChoosed)
                    .putInt(APP_PREFERENCES_NAME, AppTheme.LIGHT_THEME.themeRes)
                    .apply()
            }
        }
        setAppTheme()

        if (savedInstanceState == null) {
            replaceFragment(
                supportFragmentManager,
                R.id.container,
                FragmentApod.newInstance(),
                "APOD"
            )
        }

        setBottomNavigationMenu()
    }

    private fun setBottomNavigationMenu() {
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_nav_bar_apod -> {
                        replaceFragment(
                            supportFragmentManager,
                            R.id.container,
                            FragmentApod.newInstance(),
                            "APOD"
                        )
                        true
                    }

                    R.id.bottom_nav_view_settings -> {
                        replaceFragment(
                            supportFragmentManager,
                            R.id.container,
                            FragmentSettings.newInstance(),
                            "settings"
                        )
                        true
                    }

                    R.id.bottom_nav_bar_earth -> {
                        replaceFragment(
                            supportFragmentManager,
                            R.id.container,
                            FragmentEpic.newInstance(),
                            "earth"
                        )
                        true
                    }

                    R.id.bottom_nav_bar_mars -> {
                        replaceFragment(
                            supportFragmentManager,
                            R.id.container,
                            FragmentMars.newInstance(),
                            "mars"
                        )
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
            AppTheme.LIGHT_THEME.themeRes
        )

        setTheme(themeRes)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus

            if (view is TextInputEditText) {
                val outRect = Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.clearFocus();
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
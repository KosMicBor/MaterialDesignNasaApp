package kosmicbor.giftapp.pictureofthedayapp.utils

import androidx.fragment.app.FragmentManager
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.ui.main.SettingsFragment

object Router {

    fun openSettings (supportFragmentManager: FragmentManager) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.apod_main_container, SettingsFragment.newInstance())
            .addToBackStack("settings")
            .commit()
    }
}
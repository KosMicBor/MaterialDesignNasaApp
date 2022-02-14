package kosmicbor.giftapp.pictureofthedayapp.utils

import androidx.fragment.app.FragmentManager
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.ui.main.*

object Router {

    fun openSettings (supportFragmentManager: FragmentManager) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, FragmentSettings.newInstance())
            .addToBackStack("settings")
            .commit()
    }

    fun openApodFragment(supportFragmentManager: FragmentManager) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, FragmentApod.newInstance())
            .addToBackStack("APOD")
            .commit()
    }

    fun openEarthFragment(supportFragmentManager: FragmentManager) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, FragmentEpic.newInstance())
            .addToBackStack("earth")
            .commit()
    }

    fun openMarsFragment(supportFragmentManager: FragmentManager) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.container, FragmentMars.newInstance())
            .addToBackStack("mars")
            .commit()
    }
}
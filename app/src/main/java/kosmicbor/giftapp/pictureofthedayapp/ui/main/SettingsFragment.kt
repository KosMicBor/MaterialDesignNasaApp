package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentSettingsBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.Theme

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
        private const val APP_PREFERENCES = "APP_PREFERENCES"
        private const val APP_PREFERENCES_NAME = "THEME"
        private lateinit var sharedPreferences: SharedPreferences
    }

    private val binding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {

            sharedPreferences = it.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChipsListener()
        setResultButtonListener()
    }

    private fun setResultButtonListener() {
        binding.themeSettingsBtn.setOnClickListener {
            requireActivity().recreate()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun setChipsListener() {
        binding.settingsChipGroup.setOnCheckedChangeListener { chipGroup, _ ->

            when (chipGroup.checkedChipId) {
                R.id.settings_chip_light_theme -> {
                    sharedPreferences.edit()
                        .putInt(APP_PREFERENCES_NAME, Theme.LIGHT_THEME.themeRes)
                        .apply()
                }

                R.id.settings_chip_dark_theme -> {
                    sharedPreferences.edit()
                        .putInt(APP_PREFERENCES_NAME, Theme.DARK_THEME.themeRes).apply()
                }

                R.id.settings_chip_advanced_theme -> {
                    sharedPreferences.edit()
                        .putInt(APP_PREFERENCES_NAME, Theme.ADVANCED_THEME.themeRes)
                        .apply()
                }
            }
        }
    }
}


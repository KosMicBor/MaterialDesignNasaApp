package kosmicbor.giftapp.pictureofthedayapp.ui.main.settings_screen

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentSettingsBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.AppTheme

class FragmentSettings : Fragment(R.layout.fragment_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSettings()
        private const val APP_PREFERENCES = "APP_PREFERENCES"
        private const val APP_PREFERENCES_NAME = "THEME"
        private const val DESC_TITLE_START = 0
        private const val DESC_TITLE_END = 18
        private const val DESC_TITLE_HEIGHT = 200
        private const val DESC_TITLE_SIZE = 1.5f
        private const val DESC_LIGHT_THEME_START = 19
        private const val DESC_LIGHT_THEME_END = 78
        private const val DESC_DARK_THEME_START = 79
        private const val DESC_DARK_THEME_END = 133
        private const val DESC_ADV_THEME_START = 134
        private const val DESC_BULLET_SPAN_GAP = 20
        private const val DESC_BULLET_SPAN_RADIUS = 10
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
        setThemeDescriptionText()
    }

    private fun setThemeDescriptionText() {
        val spannableBuilder = SpannableStringBuilder(
            "Themes description\n" +
                    "Light Theme - default theme with original colors and fonts.\n" +
                    "Dark Theme - theme, which adapted to system dark mode.\n" +
                    "Alternative Theme - feel like you're on Mars. Theme with Mars colors."
        )

        Log.d("SPAN", spannableBuilder.length.toString())

        spannableBuilder.apply {

            context?.let { contextForSpanning ->

                val typedValue = TypedValue()

                contextForSpanning.theme.resolveAttribute(
                    com.google.android.material.R.attr.colorPrimary,
                    typedValue,
                    true
                )

                val bulletColor = MaterialColors.getColor(
                    binding.root,
                    com.google.android.material.R.attr.colorPrimary
                )

                setSpan(
                    StyleSpan(Typeface.BOLD),
                    DESC_TITLE_START,
                    DESC_TITLE_END,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    RelativeSizeSpan(DESC_TITLE_SIZE),
                    DESC_TITLE_START,
                    DESC_TITLE_END,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setSpan(
                        LineHeightSpan.Standard(DESC_TITLE_HEIGHT),
                        DESC_TITLE_START,
                        DESC_TITLE_END,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                setSpan(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        BulletSpan(DESC_BULLET_SPAN_GAP, bulletColor, DESC_BULLET_SPAN_RADIUS)
                    } else {
                        BulletSpan(DESC_BULLET_SPAN_GAP, bulletColor)
                    },
                    DESC_LIGHT_THEME_START,
                    DESC_LIGHT_THEME_END,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                setSpan(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        BulletSpan(DESC_BULLET_SPAN_GAP, bulletColor, DESC_BULLET_SPAN_RADIUS)
                    } else {
                        BulletSpan(DESC_BULLET_SPAN_GAP, bulletColor)
                    },
                    DESC_DARK_THEME_START,
                    DESC_DARK_THEME_END,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                setSpan(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        BulletSpan(DESC_BULLET_SPAN_GAP, bulletColor, DESC_BULLET_SPAN_RADIUS)
                    } else {
                        BulletSpan(DESC_BULLET_SPAN_GAP, bulletColor)
                    },
                    DESC_ADV_THEME_START,
                    spannableBuilder.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        }

        binding.themeSettingsDescription.text = spannableBuilder
    }

    private fun setResultButtonListener() {
        binding.themeSettingsBtn.setOnClickListener {
            requireActivity().recreate()
        }
    }

    private fun setChipsListener() {
        binding.settingsChipGroup.setOnCheckedChangeListener { chipGroup, _ ->

            when (chipGroup.checkedChipId) {
                R.id.settings_chip_light_theme -> {
                    sharedPreferences.edit()
                        .putInt(APP_PREFERENCES_NAME, AppTheme.LIGHT_THEME.themeRes)
                        .apply()
                }

                R.id.settings_chip_dark_theme -> {
                    sharedPreferences.edit()
                        .putInt(APP_PREFERENCES_NAME, AppTheme.DARK_THEME.themeRes).apply()
                }

                R.id.settings_chip_advanced_theme -> {
                    sharedPreferences.edit()
                        .putInt(APP_PREFERENCES_NAME, AppTheme.ADVANCED_THEME.themeRes)
                        .apply()
                }
            }
        }
    }
}


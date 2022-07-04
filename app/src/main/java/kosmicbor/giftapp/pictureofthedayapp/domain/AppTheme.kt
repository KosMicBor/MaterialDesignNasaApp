package kosmicbor.giftapp.pictureofthedayapp.domain

import kosmicbor.giftapp.pictureofthedayapp.R

enum class AppTheme (
    val themeKey: String,
    val themeRes: Int
    ) {
    LIGHT_THEME("LIGHT_THEME", R.style.Theme_PictureOfTheDayAppLight),
    DARK_THEME("DARK_THEME", R.style.Theme_PictureOfTheDayAppDark),
    ADVANCED_THEME("ADVANCED_THEME", R.style.Theme_PictureOfTheDayAppAdvanced)
}
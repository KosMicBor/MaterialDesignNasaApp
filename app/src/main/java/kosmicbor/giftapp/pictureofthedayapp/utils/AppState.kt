package kosmicbor.giftapp.pictureofthedayapp.utils

import kosmicbor.giftapp.pictureofthedayapp.domain.PictureOfTheDayDTO

sealed class AppState {
    data class Success(val value: PictureOfTheDayDTO) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
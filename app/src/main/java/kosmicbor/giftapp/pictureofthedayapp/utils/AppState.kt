package kosmicbor.giftapp.pictureofthedayapp.utils

sealed class AppState {
    data class Success<T>(val value: T) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
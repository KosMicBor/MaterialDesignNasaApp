package kosmicbor.giftapp.pictureofthedayapp.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kosmicbor.giftapp.pictureofthedayapp.BuildConfig
import kosmicbor.giftapp.pictureofthedayapp.domain.APODRepositoryImpl
import kosmicbor.giftapp.pictureofthedayapp.domain.PictureOfTheDayDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import kosmicbor.giftapp.pictureofthedayapp.utils.getDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: APODRepositoryImpl = APODRepositoryImpl()
) : ViewModel() {

    companion object {
        private const val TODAY = 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(): LiveData<AppState> {
        sendRequest(getDate(TODAY))
        return liveDataToObserve
    }

    fun sendRequest(date: String) {
        liveDataToObserve.value = AppState.Loading(null)
        val apiKey = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            AppState.Error(Throwable("Yuo need the API key!"))
        } else {
            repository.getAPODRetrofit()
                .getPictureOfTheDay(apiKey, date).enqueue(object : Callback<PictureOfTheDayDTO> {

                    override fun onResponse(
                        call: Call<PictureOfTheDayDTO>,
                        response: Response<PictureOfTheDayDTO>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.apply {
                                liveDataToObserve.value = AppState.Success(this)
                            }

                        } else {
                            val message = response.message()

                            if (message.isNotEmpty()) {
                                liveDataToObserve.value = AppState.Error(Throwable(message))
                            } else {
                                liveDataToObserve.value =
                                    AppState.Error(Throwable("Undefined error!"))
                            }
                        }
                    }

                    override fun onFailure(call: Call<PictureOfTheDayDTO>, t: Throwable) {
                        liveDataToObserve.value = AppState.Error(t)
                    }
                })
        }
    }
}
package kosmicbor.giftapp.pictureofthedayapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kosmicbor.giftapp.pictureofthedayapp.BuildConfig
import kosmicbor.giftapp.pictureofthedayapp.domain.ApodRepositoryImpl
import kosmicbor.giftapp.pictureofthedayapp.domain.ApodDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApodViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: ApodRepositoryImpl = ApodRepositoryImpl()
) : ViewModel() {

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun sendRequest(date: String) {
        liveDataToObserve.value = AppState.Loading(null)
        val apiKey = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            AppState.Error(Throwable("Yuo need the API key!"))
        } else {
            repository.getAPODRetrofit()
                .getPictureOfTheDay(apiKey, date).enqueue(object : Callback<ApodDTO> {

                    override fun onResponse(
                        call: Call<ApodDTO>,
                        response: Response<ApodDTO>
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

                    override fun onFailure(call: Call<ApodDTO>, t: Throwable) {
                        liveDataToObserve.value = AppState.Error(t)
                    }
                })
        }
    }
}
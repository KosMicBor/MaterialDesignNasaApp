package kosmicbor.giftapp.pictureofthedayapp.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kosmicbor.giftapp.pictureofthedayapp.BuildConfig
import kosmicbor.giftapp.pictureofthedayapp.domain.MarsDTO
import kosmicbor.giftapp.pictureofthedayapp.domain.MarsRepositoryImpl
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: MarsRepositoryImpl = MarsRepositoryImpl()
) : ViewModel() {

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun sendRequest(page: Int) {
        liveDataToObserve.value = AppState.Loading(null)
        val apiKey = BuildConfig.NASA_API_KEY

        if (apiKey.isBlank()) {
            AppState.Error(Throwable("Yuo need the API key!"))
        } else {
            repository.getMarsRetrofit()
                .getMarsPhotos(page, apiKey).enqueue(object : Callback<MarsDTO> {

                    override fun onResponse(
                        call: Call<MarsDTO>,
                        response: Response<MarsDTO>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.apply {
                                Log.d("MARS", this.toString())
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

                    override fun onFailure(call: Call<MarsDTO>, t: Throwable) {
                        liveDataToObserve.value = AppState.Error(t)
                    }
                })
        }
    }
}
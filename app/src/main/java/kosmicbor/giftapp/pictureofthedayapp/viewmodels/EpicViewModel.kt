package kosmicbor.giftapp.pictureofthedayapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kosmicbor.giftapp.pictureofthedayapp.BuildConfig
import kosmicbor.giftapp.pictureofthedayapp.domain.moon.SearchRepositoryImpl
import kosmicbor.giftapp.pictureofthedayapp.domain.epic.EpicRepositoryImpl
import kosmicbor.giftapp.pictureofthedayapp.domain.epic.EpicDTO
import kosmicbor.giftapp.pictureofthedayapp.domain.moon.MoonDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpicViewModel(
    private val liveDataEpic: MutableLiveData<AppState> = MutableLiveData(),
    private val liveDataMoon: MutableLiveData<AppState> = MutableLiveData(),
    private val epicRepository: EpicRepositoryImpl = EpicRepositoryImpl(),
    private val repository: SearchRepositoryImpl = SearchRepositoryImpl()
) : ViewModel() {

    private val apiKey = BuildConfig.NASA_API_KEY

    fun getEpicData(): LiveData<AppState> = liveDataEpic

    fun getMoonData(): LiveData<AppState> = liveDataMoon

    fun sendEpicRequest() {
        liveDataEpic.value = AppState.Loading(null)

        if (apiKey.isBlank()) {
            AppState.Error(Throwable("Yuo need the API key!"))
        } else {
            epicRepository.getEpicRetrofit()
                .getEPIC(apiKey).enqueue(object : Callback<List<EpicDTO>> {

                    override fun onResponse(
                        call: Call<List<EpicDTO>>,
                        response: Response<List<EpicDTO>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.apply {
                                liveDataEpic.value = AppState.Success(this)
                            }

                        } else {
                            val message = response.message()

                            if (message.isNotEmpty()) {
                                liveDataEpic.value = AppState.Error(Throwable(message))
                            } else {
                                liveDataEpic.value =
                                    AppState.Error(Throwable("Undefined error!"))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<EpicDTO>>, t: Throwable) {
                        liveDataEpic.value = AppState.Error(t)
                    }
                })
        }
    }

    fun sendMoonRequest(request: String, mediaType: String, page: String) {
        liveDataMoon.value = AppState.Loading(null)

        repository.getMoonRetrofit()
            .getSearchResult(request, mediaType, page)
            .enqueue(object : Callback<MoonDTO> {
                override fun onResponse(
                    call: Call<MoonDTO>,
                    response: Response<MoonDTO>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.apply {
                            liveDataMoon.value = AppState.Success(this)
                        }

                    } else {
                        val message = response.message()

                        if (message.isNotEmpty()) {
                            liveDataMoon.value = AppState.Error(Throwable(message))
                        } else {
                            liveDataMoon.value =
                                AppState.Error(Throwable("Undefined error!"))
                        }
                    }
                }

                override fun onFailure(call: Call<MoonDTO>, t: Throwable) {
                    liveDataMoon.value = AppState.Error(t)
                }

            })

    }
}
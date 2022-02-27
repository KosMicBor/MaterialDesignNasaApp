package kosmicbor.giftapp.pictureofthedayapp.domain.epic

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EpicRepository {
    @GET("EPIC/api/natural/images")
    fun getEPIC(
        @Query("api_key") apiKey: String
    ): Call<List<EpicDTO>>
}
package kosmicbor.giftapp.pictureofthedayapp.domain

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRepository {
    @GET("mars-photos/api/v1/rovers/curiosity/photos?sol=1000")
    fun getMarsPhotos(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Call<MarsDTO>
}
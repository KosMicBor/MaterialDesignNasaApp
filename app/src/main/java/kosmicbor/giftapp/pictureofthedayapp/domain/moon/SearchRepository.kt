package kosmicbor.giftapp.pictureofthedayapp.domain.moon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRepository {
    @GET("/search?")
    fun getSearchResult(
        @Query("q") q: String,
        @Query("media_type") mediaType: String,
        @Query("page") page: String
    ): Call<MoonDTO>
}
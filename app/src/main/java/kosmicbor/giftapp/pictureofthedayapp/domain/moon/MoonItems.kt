package kosmicbor.giftapp.pictureofthedayapp.domain.moon

import com.google.gson.annotations.SerializedName

data class MoonItems (
    @SerializedName("href")
    val href: String?,
    @SerializedName("data")
    val data: List<MoonData>,
    @SerializedName("links")
    val links: List<MoonImage>
)
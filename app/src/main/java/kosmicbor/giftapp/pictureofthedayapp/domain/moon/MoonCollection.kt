package kosmicbor.giftapp.pictureofthedayapp.domain.moon

import com.google.gson.annotations.SerializedName

data class MoonCollection(
    @SerializedName("version")
    val version: String?,

    @SerializedName("href")
    val href: String,

    @SerializedName("items")
    val items: List<MoonItems>,
)

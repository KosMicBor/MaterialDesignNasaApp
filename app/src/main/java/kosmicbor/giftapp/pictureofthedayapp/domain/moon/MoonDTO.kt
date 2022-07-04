package kosmicbor.giftapp.pictureofthedayapp.domain.moon

import com.google.gson.annotations.SerializedName

data class MoonDTO(

    @SerializedName("collection")
    val collection: MoonCollection
)
package kosmicbor.giftapp.pictureofthedayapp.domain.mars

import com.google.gson.annotations.SerializedName

data class MarsDTO(
    @SerializedName("photos")
    val photosArray: List<MarsPhoto>
)
package kosmicbor.giftapp.pictureofthedayapp.domain

import com.google.gson.annotations.SerializedName

data class MarsPhoto(
    @field:SerializedName("id") val id: Int?,
    @field:SerializedName("sol") val sol: Int?,
    @field:SerializedName("camera") val camera: RoversCamera?,
    @field:SerializedName("img_src") val imgSrc: String?,
    @field:SerializedName("earth_date") val earthDate: String?,
    @field:SerializedName("rover") val rover: MarsRover?
)

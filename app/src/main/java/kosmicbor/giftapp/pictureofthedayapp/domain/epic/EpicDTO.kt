package kosmicbor.giftapp.pictureofthedayapp.domain.epic

import com.google.gson.annotations.SerializedName
import kosmicbor.giftapp.pictureofthedayapp.domain.mars.CentroidCoordinates
import kosmicbor.giftapp.pictureofthedayapp.domain.mars.Position
import kosmicbor.giftapp.pictureofthedayapp.domain.mars.Quaternions

data class EpicDTO(
    @field:SerializedName("identifier") val identifier: String?,
    @field:SerializedName("caption") val caption: String?,
    @field:SerializedName("image") val image: String?,
    @field:SerializedName("centroid_coordinates") val centroidCoordinates: CentroidCoordinates?,
    @field:SerializedName("dscovr_j2000_position") val discoverJ2000Position: Position?,
    @field:SerializedName("lunar_j2000_position") val lunarJ2000Position: Position?,
    @field:SerializedName("sun_j2000_position") val sunJ2000Position: Position?,
    @field:SerializedName("attitude_quaternions") val attitudeQuaternions: Quaternions?,
    @field:SerializedName("date") val date: String?,
)

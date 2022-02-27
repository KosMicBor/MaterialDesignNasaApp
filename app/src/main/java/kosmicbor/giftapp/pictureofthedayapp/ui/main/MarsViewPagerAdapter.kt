package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textview.MaterialTextView
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.domain.MarsPhoto
import kosmicbor.giftapp.pictureofthedayapp.utils.EquilateralImageView

class MarsViewPagerAdapter : RecyclerView.Adapter<MarsPagerViewHolder>() {

    private val dataList = mutableListOf<MarsPhoto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPagerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.mars_layout, parent, false)
        return MarsPagerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MarsPagerViewHolder, position: Int) {
        val url = dataList[position].imgSrc
        val httpsUrl = url?.replace("http", "https")
        val imageDate = dataList[position].earthDate
        val cameraFullName = dataList[position].camera?.fullName
        val cameraId = dataList[position].camera?.id
        val roverId = dataList[position].camera?.roverId
        val roverName = dataList[position].rover?.name
        val roverLandingDate = dataList[position].rover?.landingDate
        val roverLaunchDate = dataList[position].rover?.launchDate

        holder.apply {
            imageView.load(httpsUrl) {
                error(R.drawable.ic_baseline_broken_image)
            }
            imageDateView.text = imageDate
            roverIdView.text = roverId.toString()
            roverNameView.text = roverName
            roverLaunchDateView.text = roverLaunchDate
            roverLandingDateView.text = roverLandingDate
            cameraIdView.text = cameraId.toString()
            cameraFullNameView.text = cameraFullName
        }
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<MarsPhoto>) {
        dataList.addAll(data)
        notifyDataSetChanged()
    }
}

class MarsPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageDateView: MaterialTextView = itemView.findViewById(R.id.mars_image_date)
    val roverIdView: MaterialTextView = itemView.findViewById(R.id.mars_rover_id)
    val roverNameView: MaterialTextView = itemView.findViewById(R.id.mars_rover_name)
    val roverLaunchDateView: MaterialTextView = itemView.findViewById(R.id.mars_rover_launch_date)
    val roverLandingDateView: MaterialTextView = itemView.findViewById(R.id.mars_rover_landing_date)
    val cameraIdView: MaterialTextView = itemView.findViewById(R.id.mars_camera_id)
    val cameraFullNameView: MaterialTextView = itemView.findViewById(R.id.mars_camera_full_name)
    val imageView: EquilateralImageView = itemView.findViewById(R.id.mars_main_image)
}

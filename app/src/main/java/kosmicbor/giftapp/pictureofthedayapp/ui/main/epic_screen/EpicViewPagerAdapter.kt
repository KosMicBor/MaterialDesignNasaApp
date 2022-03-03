package kosmicbor.giftapp.pictureofthedayapp.ui.main.epic_screen

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import coil.request.ImageResult
import com.facebook.shimmer.ShimmerFrameLayout
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.utils.EquilateralImageView
import kosmicbor.giftapp.pictureofthedayapp.utils.zoomImage

class EpicViewPagerAdapter : RecyclerView.Adapter<EpicPagerViewHolder>() {

    private var isExpanded = false
    private val dataList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpicPagerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.epic_layout, parent, false)
        return EpicPagerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EpicPagerViewHolder, position: Int) {

        holder.image.apply {
            load(dataList[position]) {
                error(R.drawable.ic_baseline_broken_image)
                placeholder(R.drawable.ic_earth_placeholder)
                listener(object : ImageRequest.Listener {
                    override fun onStart(request: ImageRequest) {
                        holder.shimmer.apply {
                            showShimmer(true)
                        }

                        super.onStart(request)
                    }

                    override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                        holder.shimmer.apply {
                            stopShimmer()
                            hideShimmer()
                        }
                            super.onSuccess(request, metadata)
                    }

                })
            }

            setOnClickListener {
                isExpanded = !isExpanded
                zoomImage(holder.image, holder.container, isExpanded)
            }
        }
    }



    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<String>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
}

class EpicPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: EquilateralImageView = itemView.findViewById(R.id.epic_main_image)
    val container: ConstraintLayout = itemView.findViewById(R.id.epic_layout_container)
    val shimmer: ShimmerFrameLayout = itemView.findViewById(R.id.epic_shimmer)
}

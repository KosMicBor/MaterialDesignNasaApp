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
import kosmicbor.giftapp.pictureofthedayapp.domain.EpicDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.EquilateralImageView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EpicViewPagerAdapter : RecyclerView.Adapter<EpicPagerViewHolder>() {

    companion object {
        private const val BASE_IMAGE_URL = "https://epic.gsfc.nasa.gov/archive/natural"
    }

    private val dataList = mutableListOf<EpicDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpicPagerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.epic_layout, parent, false)
        return EpicPagerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EpicPagerViewHolder, position: Int) {
        val imageDate = LocalDate.parse(
            dataList[position].date,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )

        val month = imageDate.format(DateTimeFormatter.ofPattern("MM"))

        val caption = dataList[position].caption

        val url = "${BASE_IMAGE_URL}/${imageDate.year}/${month}/${
            imageDate.dayOfMonth
        }/png/${dataList[position].image}.png"

        holder.title.text = caption
        holder.image.load(url) {
            error(R.drawable.ic_baseline_broken_image)
            placeholder(R.drawable.ic_earth_placeholder)
        }
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<EpicDTO>) {
        dataList.addAll(data)
        notifyDataSetChanged()
    }
}

class EpicPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: MaterialTextView = itemView.findViewById(R.id.epic_title)
    val image: EquilateralImageView = itemView.findViewById(R.id.epic_main_image)
}

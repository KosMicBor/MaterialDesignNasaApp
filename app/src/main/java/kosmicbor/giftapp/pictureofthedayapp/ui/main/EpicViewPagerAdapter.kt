package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.utils.EquilateralImageView


class EpicViewPagerAdapter : RecyclerView.Adapter<EpicPagerViewHolder>() {

    private val dataList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpicPagerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.epic_layout, parent, false)
        return EpicPagerViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EpicPagerViewHolder, position: Int) {

        holder.image.load(dataList[position]) {
            error(R.drawable.ic_baseline_broken_image)
            placeholder(R.drawable.ic_earth_placeholder)
        }
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<String>) {
        dataList.clear()
        dataList.addAll(data)
        Log.d("EPIC_MOON", dataList.size.toString())
        notifyDataSetChanged()
    }
}

class EpicPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: EquilateralImageView = itemView.findViewById(R.id.epic_main_image)
}

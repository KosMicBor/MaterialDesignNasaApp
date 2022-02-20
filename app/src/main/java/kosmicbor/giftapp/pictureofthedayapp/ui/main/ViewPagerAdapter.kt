package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kosmicbor.giftapp.pictureofthedayapp.domain.apod.ApodDayData

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        private const val VIEW_DATA = "VIEW_DATA"
    }

    private var data: MutableList<ApodDayData> = mutableListOf()

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        val imageFragment = FragmentApodImage()
        imageFragment.arguments = Bundle().apply {
            putParcelable(VIEW_DATA, data[position])
        }

        return imageFragment
    }

    fun updateData(list: List<ApodDayData>) {
        data.addAll(list)
        notifyDataSetChanged()
    }
}

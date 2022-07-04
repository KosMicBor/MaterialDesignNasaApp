package kosmicbor.giftapp.pictureofthedayapp.utils

import androidx.recyclerview.widget.DiffUtil
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoriteItem

class FavoritesDiffUtil(
    private val oldList: List<FavoriteItem>,
    private val newList: List<FavoriteItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].imageUrl == newList[newItemPosition].imageUrl
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].description == newList[newItemPosition].description &&
                oldList[oldItemPosition].date == newList[newItemPosition].date
    }
}
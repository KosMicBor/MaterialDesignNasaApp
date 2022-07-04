package kosmicbor.giftapp.pictureofthedayapp.ui.main.favorites_screen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoriteItem
import kosmicbor.giftapp.pictureofthedayapp.utils.FavoritesDiffUtil

class FavoritesRecyclerViewAdapter(
    private val favoritesList: MutableList<FavoriteItem>,
    private val removeListener: FavoriteItemRemoveListener
) :
    RecyclerView.Adapter<FavoritesRecyclerViewAdapter.FavoritesViewHolder>(),
    ItemTouchHelperAdapter {

    inner class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {
        val itemDate: MaterialTextView = view.findViewById(R.id.favorites_rv_item_date)
        val itemImage: ShapeableImageView = view.findViewById(R.id.favorites_rv_item_image)
        val removeBtn: ShapeableImageView = view.findViewById(R.id.remove_favorite_btn)

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_recyclerview_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.apply {
            itemDate.text = favoritesList[position].date
            itemImage.load(favoritesList[position].imageUrl) {
                error(R.drawable.ic_baseline_broken_image)
            }
            removeBtn.setOnClickListener {
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int) {
        val favoriteItem = favoritesList[position]
        favoritesList.removeAt(position)
        removeListener.onRemove(favoriteItem)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = favoritesList.size

    override fun itemMove(fromPosition: Int, toPosition: Int) {
        favoritesList.removeAt(fromPosition).apply {
            favoritesList.add(
                if (fromPosition > toPosition) {
                    toPosition - 1
                } else {
                    toPosition
                }, this
            )
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun itemDismiss(position: Int) {
        removeItem(position)
    }

    fun filterList() {
        val newList = mutableListOf<FavoriteItem>()
        newList.addAll(favoritesList)
        newList.sortBy { it.date }
        val result = DiffUtil.calculateDiff(FavoritesDiffUtil(favoritesList, newList))
        result.dispatchUpdatesTo(this)
    }

    interface FavoriteItemRemoveListener {
        fun onRemove(item: FavoriteItem)
    }
}
package kosmicbor.giftapp.pictureofthedayapp.ui.main.favorites_screen

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentFavoritesBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoriteItem
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState.Success
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState.Error
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState.Loading
import kosmicbor.giftapp.pictureofthedayapp.utils.showSnackBar
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.FavoritesViewModel

class FragmentFavorites : Fragment(R.layout.fragment_favorites),
    FavoritesRecyclerViewAdapter.FavoriteItemRemoveListener {

    companion object {
        fun newInstance() = FragmentFavorites()
    }

    private val viewModel: FavoritesViewModel by viewModels()
    private val binding: FragmentFavoritesBinding by viewBinding()
    private lateinit var favoritesList: MutableList<FavoriteItem>
    private lateinit var favoritesAdapter: FavoritesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.getFavoritesList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getdata().observe(viewLifecycleOwner) {
            fillList(it)
        }
    }

    private fun fillList(data: AppState) {
        when (data) {
            is Success<*> -> {
                favoritesList = data.value as MutableList<FavoriteItem>
                val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)

                context?.let {
                    ContextCompat.getDrawable(it, R.drawable.divider_drawable)
                        ?.apply { dividerItemDecoration.setDrawable(this) }
                }

                favoritesAdapter = FavoritesRecyclerViewAdapter(favoritesList, this)
                binding.favoritesRecyclerView.apply {

                    layoutManager =
                        LinearLayoutManager(
                            context, LinearLayoutManager.VERTICAL,
                            false
                        )

                    adapter = favoritesAdapter
                    addItemDecoration(dividerItemDecoration)

                    ItemTouchHelper(FavoritesItemTouchHelper(favoritesAdapter))
                        .attachToRecyclerView(binding.favoritesRecyclerView)

                }


                binding.fabFavorites.setOnClickListener {
                    favoritesAdapter.filterList()
                }
            }

            is Error -> {
                binding.apply {

                    root.showSnackBar(
                        root,
                        R.string.error_load_data_message,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.try_again_btn_text
                    ) {
                        viewModel.getFavoritesList()
                    }
                }
            }

            is Loading -> {
                //NOTHING TO DO
            }
        }
    }

    override fun onRemove(item: FavoriteItem) {
        viewModel.removeFromFavorites(item)
    }
}
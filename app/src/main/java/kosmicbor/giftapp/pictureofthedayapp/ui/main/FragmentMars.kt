package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentMarsBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.mars.MarsDTO

import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import kosmicbor.giftapp.pictureofthedayapp.utils.showSnackBar
import kosmicbor.giftapp.pictureofthedayapp.utils.viewHide
import kosmicbor.giftapp.pictureofthedayapp.utils.viewShow
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.MarsViewModel
import me.relex.circleindicator.CircleIndicator3

class FragmentMars : Fragment(R.layout.fragment_mars) {

    companion object {

        @JvmStatic
        fun newInstance() = FragmentMars()
        private const val DEFAULT_PAGE = 1
    }

    private lateinit var adapter: MarsViewPagerAdapter
    private val viewModel: MarsViewModel by viewModels()
    private val binding: FragmentMarsBinding by viewBinding(FragmentMarsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.sendRequest(DEFAULT_PAGE)
        }

        viewModel.getData().observe(viewLifecycleOwner) { data ->
            renderData(data)
        }

        adapter = MarsViewPagerAdapter()
        val viewPager = binding.marsViewPager
        viewPager.adapter = adapter
        val indicator: CircleIndicator3 = binding.marsIndicator
        indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success<*> -> {
                binding.marsProgressbar.viewHide()

                val response = data.value as MarsDTO
                Log.d("MARS", response.toString())
                adapter.updateData(response.photosArray)
            }

            is AppState.Error -> {
                binding.marsProgressbar.viewShow()
                binding.apply {

                    root.showSnackBar(
                        root,
                        R.string.error_load_data_message,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.try_again_btn_text
                    ) {
                        viewModel.sendRequest(DEFAULT_PAGE)
                    }
                }
            }

            is AppState.Loading -> {
                binding.marsProgressbar.viewShow()
            }
        }
    }
}
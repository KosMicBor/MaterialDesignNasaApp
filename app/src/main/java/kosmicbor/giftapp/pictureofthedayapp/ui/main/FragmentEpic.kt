package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentEpicBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.EpicDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import kosmicbor.giftapp.pictureofthedayapp.utils.showSnackBar
import kosmicbor.giftapp.pictureofthedayapp.utils.viewHide
import kosmicbor.giftapp.pictureofthedayapp.utils.viewShow
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.EpicViewModel
import me.relex.circleindicator.CircleIndicator3


class FragmentEpic : Fragment(R.layout.fragment_epic) {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentEpic()
    }

    private lateinit var adapter: EpicViewPagerAdapter
    private val viewModel: EpicViewModel by viewModels()
    private val binding: FragmentEpicBinding by viewBinding(FragmentEpicBinding::bind)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.sendRequest()
        }

        viewModel.getData().observe(viewLifecycleOwner) { data ->
            renderData(data)
        }
        val viewPager = binding.epicViewPager
        adapter = EpicViewPagerAdapter()
        viewPager.adapter = adapter

        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success<*> -> {

                binding.epicProgressbar.viewHide()

                val serverResponseData = data.value as List<EpicDTO>

                adapter.updateData(serverResponseData)
            }

            is AppState.Error -> {

                binding.epicProgressbar.viewShow()

                binding.apply {

                    root.showSnackBar(
                        root,
                        R.string.error_load_data_message,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.try_again_btn_text
                    ) {
                        viewModel.sendRequest()
                    }
                }
            }

            is AppState.Loading -> {
                binding.epicProgressbar.viewShow()
            }
        }
    }
}


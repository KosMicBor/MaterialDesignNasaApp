package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentEpicBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.epic.EpicDTO
import kosmicbor.giftapp.pictureofthedayapp.domain.moon.MoonDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import kosmicbor.giftapp.pictureofthedayapp.utils.showSnackBar
import kosmicbor.giftapp.pictureofthedayapp.utils.viewHide
import kosmicbor.giftapp.pictureofthedayapp.utils.viewShow
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.EpicViewModel
import me.relex.circleindicator.CircleIndicator3
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class FragmentEpic : Fragment(R.layout.fragment_epic) {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentEpic()
        private const val BASE_IMAGE_URL = "https://epic.gsfc.nasa.gov/archive/natural"
        private const val DEFAULT_LINK_INDEX = 0
    }


    private var isEarthGallerySelected = false
    private lateinit var adapter: EpicViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private val viewModel: EpicViewModel by viewModels()
    private val binding: FragmentEpicBinding by viewBinding(FragmentEpicBinding::bind)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.sendEpicRequest()
        }

        viewModel.getEpicData().observe(viewLifecycleOwner) { data ->
            renderEpicData(data)
        }
        viewModel.getMoonData().observe(viewLifecycleOwner) { data ->
            renderMoonData(data)
        }
        viewPager = binding.epicViewPager
        adapter = EpicViewPagerAdapter()
        viewPager.adapter = adapter

        val indicator: CircleIndicator3 = binding.epicIndicator
        indicator.setViewPager(viewPager)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)
        setFabListener()
    }

    private fun setFabListener() {
        binding.epicFab.setOnClickListener {

            if (isEarthGallerySelected) {
                binding.epicFab.setImageResource(R.drawable.ic_moon_icon)
                viewModel.sendEpicRequest()
            } else {
                binding.epicFab.setImageResource(R.drawable.ic_earth_icon)
                viewModel.sendMoonRequest("moon", "image", "1")
            }

            isEarthGallerySelected = !isEarthGallerySelected
        }
    }

    private fun renderMoonData(data: AppState) {
        when (data) {
            is AppState.Success<*> -> {

                binding.epicProgressbar.viewHide()

                val urlList = mutableListOf<String>()
                val serverResponseData = data.value as MoonDTO
                val items = serverResponseData.collection.items

                for (i in 0..10) {

                    val randomUrl =
                        serverResponseData
                            .collection
                            .items[Random.nextInt(items.size)]
                            .links[DEFAULT_LINK_INDEX].href?.apply {
                            urlList.add(this)
                        }
                }

                adapter.updateData(urlList)
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
                        viewModel.sendMoonRequest("moon", "image", "1")
                    }
                }
            }

            is AppState.Loading -> {
                binding.epicProgressbar.viewShow()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderEpicData(data: AppState) {
        when (data) {
            is AppState.Success<*> -> {

                binding.epicProgressbar.viewHide()

                val urlList = mutableListOf<String>()
                val serverResponseData = data.value as List<EpicDTO>

                serverResponseData.forEach {
                    val imageDate = LocalDate.parse(
                        it.date,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    )

                    val month = imageDate.format(DateTimeFormatter.ofPattern("MM"))

                    val url = "${BASE_IMAGE_URL}/${imageDate.year}/${month}/${
                        imageDate.dayOfMonth
                    }/png/${it.image}.png"

                    urlList.add(url)
                }

                adapter.updateData(urlList)
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
                        viewModel.sendEpicRequest()
                    }
                }
            }

            is AppState.Loading -> {
                binding.epicProgressbar.viewShow()
            }
        }
    }
}


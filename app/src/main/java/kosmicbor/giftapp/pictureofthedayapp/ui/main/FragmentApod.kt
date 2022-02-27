package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentApodBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.ApodDayData
import kosmicbor.giftapp.pictureofthedayapp.domain.ApodDTO
import kosmicbor.giftapp.pictureofthedayapp.utils.*
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.ApodViewModel

class FragmentApod : Fragment(R.layout.fragment_apod) {

    companion object {
        fun newInstance() = FragmentApod()
        private const val TODAY = 0
        private const val YESTERDAY = 1
        private const val DAY_BEFORE_YESTERDAY = 2
    }

    private val viewModel: ApodViewModel by viewModels()
    private lateinit var adapter: ViewPagerAdapter
    private var dataFromServerList = mutableListOf<ApodDayData>()
    private val viewPager by lazy {
        binding.apodViewPager
    }
    private val binding: FragmentApodBinding by viewBinding(
        FragmentApodBinding::bind
    )
    private val listOfDays = listOf(TODAY, YESTERDAY, DAY_BEFORE_YESTERDAY)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        openWikipediaOnClick()
        setViewPager()

        viewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        fillViewPager()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillViewPager() {
        listOfDays.forEach {
            viewModel.sendRequest(getDate(it))
        }
    }

    private fun setTabLayout(sortedData: List<ApodDayData>) {
        val tabLayout = binding.apodTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = sortedData[position].date
        }.attach()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setViewPager() {
        adapter = ViewPagerAdapter(this)
        binding.apodViewPager.adapter = adapter
    }

    private fun openWikipediaOnClick() {
        binding.apply {
            apodTextInputLayout.setEndIconOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data =
                        Uri.parse(
                            "https://en.wikipedia.org/wiki/${apodTextInputEditText.text.toString()}"
                        )
                })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success<*> -> {

                binding.apodProgressbar.viewHide()

                val serverResponseData = data.value as ApodDTO
                val url = serverResponseData.url
                val title = serverResponseData.title
                val description = serverResponseData.explanation
                val date = serverResponseData.date

                if (url.isNullOrEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.empty_link_error_message),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                } else {

                    dataFromServerList.add(ApodDayData(url, title, description, date))
                    if (dataFromServerList.size == listOfDays.size) {
                        val sortedData = dataFromServerList.sortedByDescending { it.date }
                        adapter.updateData(sortedData)
                        setTabLayout(sortedData)
                    }

                }
            }

            is AppState.Error -> {
                binding.apply {

                    root.showSnackBar(
                        root,
                        R.string.error_load_data_message,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.try_again_btn_text
                    ) {
                        listOfDays.forEach {
                            viewModel.sendRequest(getDate(it))
                        }
                    }
                }
            }

            is AppState.Loading -> {
                binding.apodProgressbar.viewShow()
            }
        }
    }
}

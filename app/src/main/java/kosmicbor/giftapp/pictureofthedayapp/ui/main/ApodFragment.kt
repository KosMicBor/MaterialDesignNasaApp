package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import kosmicbor.giftapp.pictureofthedayapp.MainActivity
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.ApodFragmentBinding
import kosmicbor.giftapp.pictureofthedayapp.utils.*
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.PictureOfTheDayViewModel

class ApodFragment : Fragment(R.layout.apod_fragment) {

    companion object {
        fun newInstance() = ApodFragment()
        private var isMain = true
        private const val TODAY = 0
        private const val YESTERDAY = 1
        private const val DAY_BEFORE_YESTERDAY = 2
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetTitle: MaterialTextView
    private lateinit var bottomSheetDescription: MaterialTextView
    private val viewModel: PictureOfTheDayViewModel by viewModels()
    private val binding: ApodFragmentBinding by viewBinding(
        ApodFragmentBinding::bind
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setBottomAppBar()

        bottomSheetTitle = binding.root.findViewById(R.id.bottom_sheet_description_title)
        bottomSheetDescription = binding.root.findViewById(R.id.bottom_sheet_description)

        viewModel.getData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        openWikipediaOnClick()
        setChipsListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setChipsListener() {
        binding.apodChipGroup.setOnCheckedChangeListener { chipGroup, _ ->

            when (chipGroup.checkedChipId) {

                R.id.apod_chip_today -> {
                    viewModel.sendRequest(getDate(TODAY))
                }

                R.id.apod_chip_yesterday -> {
                    viewModel.sendRequest(getDate(YESTERDAY))
                }

                R.id.apod_chip_before_yesterday -> {
                    viewModel.sendRequest(getDate(DAY_BEFORE_YESTERDAY))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.apod_actionbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.app_bar_favorites -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.snackbar_favorites),
                    Snackbar.LENGTH_SHORT
                )
                    .setAnchorView(binding.apodFab)
                    .show()
            }

            R.id.app_bar_settings -> {
                Router.openSettings(requireActivity().supportFragmentManager)
            }

            R.id.apod_appbar_search -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.snackbar_search),
                    Snackbar.LENGTH_SHORT
                )
                    .setAnchorView(binding.apodFab)
                    .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar() {
        val context = (context as MainActivity).also {
            it.setSupportActionBar(binding.apodBottomAppbar)
        }

        binding.apodFab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.apply {
                    apodBottomAppbar.apply {
                        navigationIcon = null
                        fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                        replaceMenu(R.menu.bottom_appbar_other_screen)
                    }

                    apodFab.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_back_fab_24
                        )
                    )
                }
            } else {
                isMain = true
                binding.apply {
                    apodBottomAppbar.apply {
                        navigationIcon =
                            ContextCompat.getDrawable(context, R.drawable.ic_baseline_menu_24)
                        fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                        replaceMenu(R.menu.apod_actionbar_menu)
                    }

                    apodFab.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_add_24
                        )
                    )

                }
            }
        }
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

    private fun setBottomSheetBehavior(bottomSheetLayout: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success -> {

                binding.apodProgressbar.viewHide()

                val serverResponseData = data.value
                val url = serverResponseData.url
                val title = serverResponseData.title
                val description = serverResponseData.explanation

                if (url.isNullOrEmpty()) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.empty_link_error_message),
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(binding.apodFab)
                        .show()
                } else {
                    binding.apodMainImage.load(url) {
                        lifecycle(this@ApodFragment)
                        error(R.drawable.ic_baseline_broken_image)
                        placeholder(R.drawable.ic_baseline_broken_image)
                    }
                    bottomSheetTitle.text = title
                    bottomSheetDescription.text = description
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
                        viewModel.sendRequest(getDate(0))
                    }
                }
            }

            is AppState.Loading -> {
                binding.apodProgressbar.viewShow()
            }
        }
    }
}
package kosmicbor.giftapp.pictureofthedayapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.request.ImageRequest
import coil.request.ImageResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textview.MaterialTextView
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentApodImageBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.apod.ApodDayData
import kosmicbor.giftapp.pictureofthedayapp.utils.zoomImage

class FragmentApodImage : Fragment(R.layout.fragment_apod_image) {

    companion object {
        private const val VIEW_DATA = "VIEW_DATA"
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isExpanded: Boolean = false
    private lateinit var bottomSheetTitle: MaterialTextView
    private lateinit var bottomSheetDescription: MaterialTextView
    private val binding: FragmentApodImageBinding by viewBinding(FragmentApodImageBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetTitle = binding.root.findViewById(R.id.bottom_sheet_description_title)
        bottomSheetDescription = binding.root.findViewById(R.id.bottom_sheet_description)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        arguments?.takeIf {
            it.containsKey(VIEW_DATA)
        }?.apply {
            val data = this.getParcelable<ApodDayData>(VIEW_DATA)?.let {
                binding.apply {
                    binding.apodMainImage.load(it.url) {
                        lifecycle(this@FragmentApodImage)
                        error(R.drawable.ic_baseline_broken_image)
                        placeholder(R.drawable.ic_baseline_broken_image)
                        listener(object : ImageRequest.Listener {
                            override fun onStart(request: ImageRequest) {
                                binding.apodImageShimmer.apply {
                                    showShimmer(true)
                                }

                                super.onStart(request)
                            }

                            override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                                binding.apodImageShimmer.apply {
                                    stopShimmer()
                                    hideShimmer()
                                }
                                super.onSuccess(request, metadata)
                            }
                        })
                    }

                    apodMainImage.setOnClickListener {
                        isExpanded = !isExpanded
                        zoomImage(apodMainImage, apodFragmentImageContainer, isExpanded)
                    }

                    bottomSheetTitle.text = it.title
                    bottomSheetDescription.text = it.description
                }
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheetLayout: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
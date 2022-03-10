package kosmicbor.giftapp.pictureofthedayapp.ui.main.apod_screen

import android.graphics.*
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.request.ImageRequest
import coil.request.ImageResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kosmicbor.giftapp.pictureofthedayapp.R
import kosmicbor.giftapp.pictureofthedayapp.databinding.FragmentApodImageBinding
import kosmicbor.giftapp.pictureofthedayapp.domain.apod.ApodDayData
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoriteItem
import kosmicbor.giftapp.pictureofthedayapp.utils.zoomImage
import kosmicbor.giftapp.pictureofthedayapp.viewmodels.ApodViewPagerItemViewModel

class FragmentApodImage : Fragment(R.layout.fragment_apod_image) {

    companion object {
        private const val VIEW_DATA = "VIEW_DATA"
    }

    private val viewModel: ApodViewPagerItemViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var isExpanded: Boolean = false
    private lateinit var bottomSheetTitle: ShapeableImageView
    private lateinit var bottomSheetDescription: MaterialTextView
    private lateinit var bottomSheetContainer: ConstraintLayout
    private lateinit var responseData: ApodDayData
    private var isInFavorite = false
    private val binding: FragmentApodImageBinding by viewBinding(FragmentApodImageBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetTitle = binding.root.findViewById(R.id.bottom_sheet_description_title)
        bottomSheetDescription = binding.root.findViewById(R.id.bottom_sheet_description)
        bottomSheetContainer = binding.root.findViewById(R.id.bottom_sheet_container)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))

        if (savedInstanceState == null) {
            arguments?.takeIf {
                it.containsKey(VIEW_DATA)
            }?.apply {
                this.getParcelable<ApodDayData>(VIEW_DATA)?.let {
                    responseData = it
                }
            }
            viewModel.isItemFavorite(responseData.url)
        }


        viewModel.getData().observe(viewLifecycleOwner) {
            if (it) {
                binding.apodFavoriteBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                isInFavorite = true
            }
        }

        arguments?.takeIf {
            it.containsKey(VIEW_DATA)
        }?.apply {
            this.getParcelable<ApodDayData>(VIEW_DATA)?.apply {
                responseData = this

                binding.apply {
                    apodMainImage.load(responseData.url) {
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

                            override fun onSuccess(
                                request: ImageRequest,
                                metadata: ImageResult.Metadata
                            ) {
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

                    apodFavoriteBtn.setOnClickListener {
                        isInFavorite = !isInFavorite
                        if (isInFavorite) {
                            binding.apodFavoriteBtn
                                .setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                            responseData.url?.apply {
                                viewModel.addToFavorites(
                                    FavoriteItem(
                                        date = responseData.date,
                                        title = responseData.title,
                                        description = responseData.description,
                                        imageUrl = responseData.url!!
                                    )
                                )
                            }

                        } else {
                            binding.apodFavoriteBtn
                                .setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                            responseData.url?.let {
                                Log.d("URL_FRAG", it)
                                viewModel.removeFromFavorites(it)
                            }
                        }
                    }

                    bottomSheetTitle.setImageBitmap(drawTextOnPath(responseData.title))

                    bottomSheetDescription.text = responseData.description
                }
            }
        }
    }

    private fun drawTextOnPath(title: String?): Bitmap? {

        val bitmap = Bitmap.createBitmap(
            1000,
            150,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        val textColor = MaterialColors.getColor(
            binding.root,
            com.google.android.material.R.attr.colorPrimary
        )

        val textPaint = TextPaint().apply {
            isAntiAlias = true
            color = textColor
            textSize = 60F
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val leftSize = canvas.width / 2F - textPaint.measureText(title)
        val topSize = canvas.height / 2F
        val rightSize = canvas.width / 2F + textPaint.measureText(title)
        val bottomSize = canvas.height / 2F + canvas.height / 4F

        val enclosingRect = RectF(leftSize, topSize, rightSize, bottomSize)

        val path = Path()
        path.addArc(
            enclosingRect,
            240F,
            180F
        )

        if (title != null) {
            canvas.drawTextOnPath(
                title,
                path,
                0F,
                0F,
                textPaint
            )
        }

        return bitmap
    }

    private fun setBottomSheetBehavior(bottomSheetLayout: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
package kosmicbor.giftapp.pictureofthedayapp

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.google.android.material.imageview.ShapeableImageView

class LaunchActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_SCREEN_ANIMATION_DELAY = 2000L
        private const val START_RADIUS = 0F
        private const val VALUE_TWO = 2
    }

    private lateinit var nasaLogo: ShapeableImageView
    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        nasaLogo = findViewById(R.id.nasa_launcher_image)
        nasaLogo.post {
            makeLogoRevealAnimation()
        }
    }

    private fun makeLogoRevealAnimation() {

        val cx = nasaLogo.width / VALUE_TWO
        val cy = nasaLogo.height / VALUE_TWO
        val finalRadius = nasaLogo.width.coerceAtLeast(nasaLogo.height).toFloat()

        val circularReveal = ViewAnimationUtils.createCircularReveal(
            nasaLogo,
            cx,
            cy,
            START_RADIUS,
            finalRadius
        )

        circularReveal.duration = SPLASH_SCREEN_ANIMATION_DELAY
        nasaLogo.visibility = View.VISIBLE
        circularReveal.doOnEnd {
            handler.post {
                startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
                finish()
                this.overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }
        circularReveal.start()

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        hideStatusBarAllVersions()
    }

    @Suppress("DEPRECATION")
    private fun hideStatusBarAllVersions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            window.insetsController?.apply {

                hide(android.R.style.Theme_Material_NoActionBar)
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}
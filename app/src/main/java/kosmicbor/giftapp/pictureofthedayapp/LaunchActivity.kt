package kosmicbor.giftapp.pictureofthedayapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_SCREEN = 2000L
    }

    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        handler.postDelayed({
            startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
            finish()
            this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }, SPLASH_SCREEN)
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
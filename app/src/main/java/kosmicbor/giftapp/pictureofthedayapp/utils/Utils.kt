package kosmicbor.giftapp.pictureofthedayapp.utils

import android.os.Build
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

fun View.viewShow() {
    visibility = View.VISIBLE
}

fun View.viewHide() {
    visibility = View.GONE
}

fun View.showSnackBar(
    view: View,
    @StringRes stringRes: Int,
    length: Int = Snackbar.LENGTH_INDEFINITE,
    @StringRes actionText: Int,
    action: (View) -> Unit
) {
    Snackbar.make(
        view, stringRes,
        length
    ).setAction(actionText, action).show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDate(day: Int): String {
    val currentDate = LocalDateTime.now()
    val neededDate = currentDate.minus(Period.ofDays(day))
    return neededDate.format(DateTimeFormatter.ISO_DATE)
}

fun replaceFragment(
    supportFragmentManager: FragmentManager,
    container: Int,
    fragment: Fragment,
    fragmentName: String?
) {
    supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .replace(container, fragment)
        .addToBackStack(fragmentName)
        .commit()
}

fun addFragment(
    supportFragmentManager: FragmentManager,
    container: Int,
    fragment: Fragment,
    fragmentName: String?
) {
    supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .add(container, fragment)
        .addToBackStack(fragmentName)
        .commit()
}

fun zoomImage(image: ImageView, container: ViewGroup, expandFlag: Boolean) {

    TransitionManager.beginDelayedTransition(
        container, TransitionSet()
            .addTransition(ChangeBounds())
            .addTransition(ChangeImageTransform())
    )

    val params: ViewGroup.LayoutParams = image.layoutParams

    params.height =
        if (expandFlag) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

    image.apply {
        layoutParams = params
        scaleType =
            if (expandFlag) {
                ImageView.ScaleType.CENTER_CROP
            } else {
                ImageView.ScaleType.FIT_CENTER
            }
        Log.d("IMAGE", image.layoutParams.toString())
    }
}
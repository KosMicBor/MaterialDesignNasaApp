package kosmicbor.giftapp.pictureofthedayapp.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

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
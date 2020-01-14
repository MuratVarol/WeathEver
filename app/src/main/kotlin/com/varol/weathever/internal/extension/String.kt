package com.varol.weathever.internal.extension

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.varol.weathever.R
import com.varol.weathever.internal.util.Constants.Date.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

private const val HEX_CHARS = "0123456789ABCDEF"
private const val PATTERN = "[^?0-9]+"

fun String?.getInt(): Int {
    return this?.let {
        val replacedString: String? = this.replace(Regex(PATTERN), String.EMPTY)
        if (replacedString.isNullOrBlank()) 0 else replacedString.toInt()
    } ?: 0
}

fun String.toSpanned(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            this.replace("\n", "<br />"),
            Html.FROM_HTML_MODE_LEGACY
        )
    } else
        @Suppress("DEPRECATION")
        Html.fromHtml(this.replace("\n", "<br />"))
}

fun String.regexToNumber(): String? =
    Regex(pattern = "-?\\d+(.\\d+)*(\\,\\d+)?").find(this)?.value


fun String?.toDate(): Date? {
    return try {
        val format = SimpleDateFormat(
            ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT,
            Locale.getDefault()
        )
        format.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String?.emptyIfNull(): String {
    return if (this.isNullOrBlank()) String.EMPTY else this
}

val String.Companion.EMPTY: String
    get() = ""

fun String.appendPercentage(): String {
    return String.format("%s %s", this, "%".padStart(0))
}

fun String.appendMeterPerSecond(): String {
    return String.format("%s %s", this, "m/s".padStart(0))
}

fun String.appendCelsius(): String {
    return String.format("%s %s", this, "m/s".padStart(0))
}

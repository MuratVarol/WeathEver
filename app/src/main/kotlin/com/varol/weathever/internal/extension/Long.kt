package com.varol.weathever.internal.extension

import com.varol.weathever.internal.util.Constants
import java.text.SimpleDateFormat
import java.util.*

fun Long?.toDate(): Date {
    if (this == null)
        return Calendar.getInstance().time
    return Date(this)
}

fun Long?.toFormattedDate(): String? {
    if (this == null)
        return null
    val format =
        SimpleDateFormat(Constants.Date.DATE_FORMAT_DAY_MONTH_WEEKDAY_AND_TIME, Locale.getDefault())
    return format.format(this.toDate())
}

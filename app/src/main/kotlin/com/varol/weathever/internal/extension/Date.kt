package com.varol.weathever.internal.extension

import com.varol.weathever.internal.util.Constants
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toShortDateString(): String {
    return SimpleDateFormat(Constants.Date.ISO_8601_EXTENDED_DATE_FORMAT, Locale.US)
            .format(this)
}

fun Date.toShortDateUiString(): String {
    return SimpleDateFormat(Constants.Date.DATE_FORMAT_UI_SHORT, Locale.US)
        .format(this)
}

fun Date.toTimeStamp(): Long = this.time

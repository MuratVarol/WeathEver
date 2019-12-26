package com.varol.weathever.internal.extension

fun Int?.notZero(): Boolean {
    return this != 0
}

fun Int?.zeroIfNull() = this ?: 0
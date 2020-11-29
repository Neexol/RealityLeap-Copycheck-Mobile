package ru.rtuitlab.copycheck.utils.extensions

import android.content.Context
import android.widget.Toast

fun Context.showLongToast(message: String?) = longToast(message).show()

fun Context.showShortToast(message: String?) = shortToast(message).show()

fun Context.shortToast(message: String?) = toast(message, Toast.LENGTH_SHORT)

fun Context.longToast(message: String?) = toast(message, Toast.LENGTH_LONG)

fun Context.toast(message: String?, duration: Int): Toast = Toast.makeText(this, message, duration)
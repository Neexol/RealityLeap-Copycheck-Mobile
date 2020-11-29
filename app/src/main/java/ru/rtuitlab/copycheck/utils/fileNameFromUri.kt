package ru.rtuitlab.copycheck.utils

import android.net.Uri
import java.io.File

fun Uri?.fileName() = this?.let { File(path!!).name } ?: ""
package ru.rtuitlab.copycheck.utils

import ru.rtuitlab.copycheck.models.CopycheckResult

data class HistoryItem(
    val copycheckResult: CopycheckResult? = null,
    val fileName: String? = null,
    var isFavourite: Boolean = false
)
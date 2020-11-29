package ru.rtuitlab.copycheck.utils

import ru.rtuitlab.copycheck.models.CopycheckResult

sealed class SongResult {
    data class Recognized(val copycheckResult: CopycheckResult): SongResult()
    data class NotRecognized(val fileName: String): SongResult()
}
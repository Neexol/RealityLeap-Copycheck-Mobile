package ru.rtuitlab.copycheck.persistence

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.rtuitlab.copycheck.utils.HistoryItem

class HistoryPrefs(
    context: Context
) {
    private companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val PREFERENCES_KEY = "CopycheckPrefs"
    }

    private val prefs = context.getSharedPreferences(
        PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )

    private val gson = Gson()
    private val historyItemListType = object : TypeToken<List<HistoryItem>>() {}.type

    private fun saveHistory(historyItems: List<HistoryItem>) {
        prefs.edit(commit = true) {
            putString(HISTORY_KEY, gson.toJson(historyItems))
        }
    }

    fun getHistory(): List<HistoryItem> {
        return prefs.getString(HISTORY_KEY, null)?.let {
            gson.fromJson(it, historyItemListType)
        } ?: emptyList()
    }

    fun addToHistory(historyItem: HistoryItem) = saveHistory(getHistory() + historyItem)

    fun changeFavourite(historyItem: HistoryItem) {
        val history = getHistory()
        history.find {
            it.copycheckResult != null && it.copycheckResult == historyItem.copycheckResult
                    || it.fileName != null && it.fileName == historyItem.fileName
        }!!.apply { isFavourite = !isFavourite }
        saveHistory(history)
    }
}
package ru.rtuitlab.copycheck.recyclers

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.models.RaoSearchResult

class MatchesRAOAdapter(
    private val context: Context,
    private val data: List<RaoSearchResult>
) : RecyclerView.Adapter<MatchesRAOAdapter.MatchRAOHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MatchRAOHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_match_rao, parent, false)
        )

    override fun onBindViewHolder(holder: MatchRAOHolder, position: Int) = holder.bind(position)

    inner class MatchRAOHolder internal constructor(view: View): RecyclerView.ViewHolder(view) {
        private val titleTV: TextView = view.findViewById(R.id.rao_title_holder)
        private val genreTV: TextView = view.findViewById(R.id.genre_holder)
        private val authorsTV: TextView = view.findViewById(R.id.authors_holder)

        fun bind(position: Int) {
            titleTV.text = data[position].title

            val genre = SpannableString(context.getString(R.string.genre_holder, data[position].genre))
            genre.setSpan(ForegroundColorSpan(Color.WHITE), 0, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            genreTV.text = genre

            val authors = SpannableString(context.getString(R.string.authors_holder, data[position].authors))
            authors.setSpan(ForegroundColorSpan(Color.WHITE), 0, 10, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            authorsTV.text = authors
        }
    }
}
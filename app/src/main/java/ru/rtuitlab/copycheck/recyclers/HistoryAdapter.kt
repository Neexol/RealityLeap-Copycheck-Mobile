package ru.rtuitlab.copycheck.recyclers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.models.CopycheckResult
import ru.rtuitlab.copycheck.utils.HistoryItem

class HistoryAdapter(
    private val context: Context,
    private val data: List<HistoryItem>
) : RecyclerView.Adapter<HistoryAdapter.HistoryItemHolder>() {

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_history_item, parent, false)
        )

    override fun onBindViewHolder(holder: HistoryItemHolder, position: Int) = holder.bind(position)

    inner class HistoryItemHolder internal constructor(view: View): RecyclerView.ViewHolder(view) {
        private val titleTV: TextView = view.findViewById(R.id.title_holder)
        private val favouriteIV: ImageView = view.findViewById(R.id.favourite_image)
        private val authorTV: TextView = view.findViewById(R.id.author_holder)
        private val copyrightTV: TextView = view.findViewById(R.id.copyright_holder)
        private val indicatorView: View = view.findViewById(R.id.copyright_status_indicator)

        init {
            view.setOnClickListener {
                data[adapterPosition].copycheckResult?.let {
                    clickListener?.onHistoryItemClick(it)
                }
            }
            favouriteIV.setOnClickListener {
                data[adapterPosition].apply { isFavourite = !isFavourite }
                this.bind(adapterPosition)
//                notifyItemChanged(adapterPosition)
                clickListener?.onFavouriteToggle(data[adapterPosition])
            }
        }

        fun bind(position: Int) {
            val hi = data[position]

            if (hi.copycheckResult != null) {
                val ccResult = hi.copycheckResult
                authorTV.isVisible = true
                copyrightTV.isVisible = true
                indicatorView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        when (ccResult.copyrightResult.resultStatus) {
                            2 -> R.color.full_reg
                            1 -> R.color.half_reg
                            else -> R.color.no_reg
                        }
                    )
                )
                titleTV.text = ccResult.recognitionResult.result.title
                authorTV.text = ccResult.recognitionResult.result.artist

                ccResult.appleResult?.let {
                    copyrightTV.setCompoundDrawablesWithIntrinsicBounds(
                        if (it.explicit) R.drawable.ic_explicit else 0,
                        0,
                        0,
                        0
                    )
                    copyrightTV.text = it.copyright
                    copyrightTV.setOnClickListener {
                        ccResult.recognitionResult.result.songLink?.let { link ->
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        }
                    }
                } ?:run {
                    copyrightTV.isVisible = false
                }
            } else {
                val fileName = hi.fileName!!
                titleTV.text = fileName
                authorTV.isVisible = false
                copyrightTV.isVisible = false
                indicatorView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.card_color
                    )
                )
            }

            val isFavourite = hi.isFavourite
            favouriteIV.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (isFavourite) R.drawable.ic_star else R.drawable.ic_star_outline
                )
            )
        }
    }

    private var clickListener: OnHistoryItemClickListener? = null

    fun setOnHistoryItemClickListener(onHistoryItemClickListener: OnHistoryItemClickListener) {
        clickListener = onHistoryItemClickListener
    }

    interface OnHistoryItemClickListener {
        fun onHistoryItemClick(copycheckResult: CopycheckResult)
        fun onFavouriteToggle(historyItem: HistoryItem)
    }
}

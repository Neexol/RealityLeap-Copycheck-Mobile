package ru.rtuitlab.copycheck.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.databinding.FragmentSongBinding
import ru.rtuitlab.copycheck.recyclers.MatchesRAOAdapter
import ru.rtuitlab.copycheck.viewmodels.MainViewModel


class SongFragment : Fragment(R.layout.fragment_song) {
    private val viewBinding: FragmentSongBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillData()
        viewBinding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun fillData() {
        val ccResult = viewModel.selectedCopycheckResult

        viewBinding.titleHolder.text = ccResult.recognitionResult.result.title
        viewBinding.authorHolder.text = ccResult.recognitionResult.result.artist

        ccResult.appleResult?.let {
            viewBinding.copyrightHolder.setCompoundDrawablesWithIntrinsicBounds(
                if (it.explicit) R.drawable.ic_explicit else 0,
                0,
                0,
                0
            )
            viewBinding.copyrightHolder.text = it.copyright
            viewBinding.copyrightHolder.setOnClickListener {
                ccResult.recognitionResult.result.songLink?.let { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            }
        } ?:run {
            viewBinding.copyrightHolder.isVisible = false
        }

        viewBinding.copyrightStatusIndicator.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                when (ccResult.copyrightResult.resultStatus) {
                    2 -> R.color.full_reg
                    1 -> R.color.half_reg
                    else -> R.color.no_reg
                }
            )
        )
        if (ccResult.copyrightResult.data.isNotEmpty()) {
            viewBinding.recyclerView.adapter = MatchesRAOAdapter(
                requireContext(),
                ccResult.copyrightResult.data
            )
        } else {
            viewBinding.matchesLabel.text = getString(R.string.no_matches_found)
        }

        viewBinding.favouriteImage.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                if (ccResult.isFavourite) R.drawable.ic_star else R.drawable.ic_star_outline
            )
        )
    }
}
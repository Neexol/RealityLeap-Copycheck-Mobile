package ru.rtuitlab.copycheck.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.databinding.FragmentFavouritesBinding
import ru.rtuitlab.copycheck.models.CopycheckResult
import ru.rtuitlab.copycheck.recyclers.HistoryAdapter
import ru.rtuitlab.copycheck.utils.HistoryItem
import ru.rtuitlab.copycheck.viewmodels.MainViewModel

class FavouritesFragment : Fragment(R.layout.fragment_favourites), HistoryAdapter.OnHistoryItemClickListener {
    private val viewBinding: FragmentFavouritesBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onHistoryItemClick(copycheckResult: CopycheckResult) {
        viewModel.selectedCopycheckResult = copycheckResult
        findNavController().navigate(R.id.action_favouritesFragment_to_songFragment2)
    }

    override fun onFavouriteToggle(historyItem: HistoryItem) {
        viewModel.changeFavourite(historyItem.copy())
        val hiList = (viewBinding.recyclerView.adapter as HistoryAdapter).data
        (viewBinding.recyclerView.adapter as HistoryAdapter).data = hiList.filter { it != historyItem }
        viewBinding.recyclerView.adapter!!.notifyItemRemoved(hiList.indexOf(historyItem))
    }

    private fun initRecyclerView() {
        viewBinding.recyclerView.adapter = HistoryAdapter(
            requireContext(),
            viewModel.getHistory().filter { it.isFavourite }
        ).apply {
            setOnHistoryItemClickListener(this@FavouritesFragment)
        }
    }
}
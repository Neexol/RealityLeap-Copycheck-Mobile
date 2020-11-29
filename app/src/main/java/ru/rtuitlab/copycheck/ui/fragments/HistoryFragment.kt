package ru.rtuitlab.copycheck.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.databinding.FragmentHistoryBinding
import ru.rtuitlab.copycheck.models.CopycheckResult
import ru.rtuitlab.copycheck.recyclers.HistoryAdapter
import ru.rtuitlab.copycheck.utils.HistoryItem
import ru.rtuitlab.copycheck.viewmodels.MainViewModel

class HistoryFragment : Fragment(R.layout.fragment_history), HistoryAdapter.OnHistoryItemClickListener {
    private val viewBinding: FragmentHistoryBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = HistoryAdapter(requireContext(), viewModel.getHistory()).apply {
            setOnHistoryItemClickListener(this@HistoryFragment)
        }
    }

    override fun onHistoryItemClick(copycheckResult: CopycheckResult) {
        viewModel.selectedCopycheckResult = copycheckResult
        findNavController().navigate(R.id.action_historyFragment_to_songFragment3)
    }

    override fun onFavouriteToggle(historyItem: HistoryItem) {
        viewModel.changeFavourite(historyItem.copy())
    }
}
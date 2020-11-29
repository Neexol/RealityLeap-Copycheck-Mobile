package ru.rtuitlab.copycheck.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.databinding.FragmentCheckBinding
import ru.rtuitlab.copycheck.models.CopycheckResult
import ru.rtuitlab.copycheck.server.handle.Resource
import ru.rtuitlab.copycheck.utils.extensions.hideProgress
import ru.rtuitlab.copycheck.utils.extensions.showProgress
import ru.rtuitlab.copycheck.server.handle.Status
import ru.rtuitlab.copycheck.utils.SongResult
import ru.rtuitlab.copycheck.utils.extensions.showLongToast
import ru.rtuitlab.copycheck.utils.extensions.showShortToast
import ru.rtuitlab.copycheck.viewmodels.MainViewModel

class CheckFragment : Fragment(R.layout.fragment_check) {

    private val getSong = registerForActivityResult(GetContent()) { uri ->
        uri?.let {
            viewModel.selectedUri = it
            checkUriStatus()
        }
    }

    private val viewBinding: FragmentCheckBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUriStatus()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        viewBinding.selectFileBtn.setOnClickListener {
            getSong.launch("audio/*")
        }
        viewBinding.reselectFileBtn.setOnClickListener {
            getSong.launch("audio/*")
        }
        viewBinding.checkSongBtn.setOnClickListener {
            viewModel.uploadFile()
        }
    }

    private fun setObservers() {
        viewModel.songResultResource.observe(viewLifecycleOwner) {
            processSongResultResource(it)
        }
    }

    private fun checkUriStatus() {
        val isUriSelected = viewModel.selectedUri != null
        viewBinding.holderFileSelected.isVisible = isUriSelected
        viewBinding.holderFileUnselected.isVisible = !isUriSelected
        if (isUriSelected) {
            viewBinding.fileNameHolder.text = viewModel.fileName
        }
        viewModel.songResultResource.value?.let {
            if (it.status == Status.LOADING) {
                viewBinding.checkSongBtn.showProgress()
                viewBinding.reselectFileBtn.isClickable = false
            }
        }
    }

    private fun processSongResultResource(songResultResource: Resource<SongResult>) {
        when (songResultResource.status) {
            Status.SUCCESS -> {
                viewBinding.checkSongBtn.hideProgress(R.string.check)
                viewBinding.reselectFileBtn.isClickable = true
                when (val songResult = songResultResource.data!!) {
                    is SongResult.Recognized -> navigateToSongInfo(songResult.copycheckResult)
                    is SongResult.NotRecognized -> requireContext().showShortToast(getString(R.string.not_recognize_song))
                }
                viewModel.resetUriSelect()
                checkUriStatus()
            }
            Status.LOADING -> {
                viewBinding.checkSongBtn.showProgress()
                viewBinding.reselectFileBtn.isClickable = false
            }
            Status.ERROR -> {
                viewBinding.checkSongBtn.hideProgress(R.string.check)
                viewBinding.reselectFileBtn.isClickable = true
                requireContext().showLongToast(songResultResource.message)
            }
        }
    }

    private fun navigateToSongInfo(copycheckResult: CopycheckResult) {
        requireContext().showShortToast(copycheckResult.recognitionResult.result.artist)
    }
}
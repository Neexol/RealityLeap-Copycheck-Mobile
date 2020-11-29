package ru.rtuitlab.copycheck.ui.fragments

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.databinding.FragmentSongBinding

class SongFragment : Fragment(R.layout.fragment_song) {
    private val viewBinding: FragmentSongBinding by viewBinding()
}
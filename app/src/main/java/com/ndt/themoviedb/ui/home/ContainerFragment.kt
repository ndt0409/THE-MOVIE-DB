package com.ndt.themoviedb.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ndt.themoviedb.databinding.FragmentContainerBinding

class ContainerFragment : Fragment() {
    private lateinit var binding: FragmentContainerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContainerBinding.inflate(layoutInflater)
        return binding.root
    }
}

package com.lucascamilo.fitstrike.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lucascamilo.fitstrike.databinding.FragmentHomeBinding
import com.lucascamilo.fitstrike.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.txvUserName.text = userName
            binding.shimmerUserName.stopShimmer()
            binding.shimmerUserName.visibility = View.GONE
        }

        homeViewModel.getUserName()
        binding.shimmerUserName.startShimmer()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
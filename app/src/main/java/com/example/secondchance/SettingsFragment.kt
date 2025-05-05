package com.example.secondchance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.secondchance.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.backToListButton3.setOnClickListener {
            bundleOf()
            findNavController().navigateUp()
        }

        return binding.root
    }


}
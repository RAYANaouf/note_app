package com.example.notes_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentAboutUsBinding


class AboutUsFragment : Fragment() {

    private lateinit var binding : FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set up the binding
        binding = FragmentAboutUsBinding.inflate(inflater)

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = AboutUsFragment()
    }
}
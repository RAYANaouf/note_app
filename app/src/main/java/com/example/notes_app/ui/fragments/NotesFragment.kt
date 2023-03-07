package com.example.notes_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentNotesBinding
import com.example.notes_app.modul.MyViewModul


class NotesFragment : Fragment() {

    private lateinit var binding : FragmentNotesBinding
    private lateinit var m_ViewModul : MyViewModul

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set the view module
        m_ViewModul = ViewModelProvider(this).get(MyViewModul::class.java)

        //set the binding
        binding = FragmentNotesBinding.inflate(inflater)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = NotesFragment()
    }
}
package com.example.notes_app.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentAddNoteBinding
import com.example.notes_app.ui.activities.OnClickNavigator

class AddNoteFragment : Fragment() {

    private var cat_id: Int? = null
    private lateinit var binding : FragmentAddNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cat_id = it.getInt(ARG_CAT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // set the binding
        binding = FragmentAddNoteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance(cat_id: Int) =
            AddNoteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CAT_ID, cat_id)
                }
            }
    }
}
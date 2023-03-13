package com.example.notes_app.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.databinding.FragmentNotesBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.recyclers.adapter.NotesAdapter
import com.example.notes_app.ui.activities.DialogViewer

public const val ARG_CAT_ID="cat_id"

class NotesFragment : Fragment() {

    private lateinit var binding : FragmentNotesBinding
    private lateinit var m_ViewModul : MyViewModel
    private lateinit var m_open_dialog : DialogViewer

    private var m_catId : Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DialogViewer){
            m_open_dialog = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            m_catId = getInt(ARG_CAT_ID)
        }

        Toast.makeText(requireContext() , "${m_catId}" , Toast.LENGTH_LONG).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set the view module
        m_ViewModul = ViewModelProvider(this).get(MyViewModel::class.java)

        //set the binding
        binding = FragmentNotesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesFragmentAddFab.setOnClickListener {
            m_open_dialog.add_note(m_catId)
        }

        binding.notesFragmentNotesRv.adapter = NotesAdapter(this , m_ViewModul)
        binding.notesFragmentNotesRv.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }

    companion object {

        @JvmStatic
        fun newInstance(cat_id : Int) = NotesFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CAT_ID,cat_id)
            }
        }
    }
}
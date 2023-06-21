

package com.example.notes_app.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.databinding.FragmentNotesBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.recyclers.adapter.NotesAdapter
import com.example.notes_app.recyclers.item_decoration.NoteDecoration
import com.example.notes_app.ui.interfaces.DialogViewer
import com.example.notes_app.ui.interfaces.OnClickNavigator

public const val ARG_CAT_ID="cat_id"

class DiariesFragment : Fragment() {

    private lateinit var binding : FragmentNotesBinding
    private lateinit var m_ViewModul : MyViewModel
    private lateinit var m_open_dialog : DialogViewer
    private lateinit var m_onClickNavigator : OnClickNavigator

    private var m_catId : Int = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DialogViewer){
            m_open_dialog = context
        }
        if(context is OnClickNavigator){
            m_onClickNavigator = context
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            m_catId = getInt(ARG_CAT_ID)
        }

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

        binding.notesFragmentNotesRv.adapter = NotesAdapter(requireContext() , this , m_ViewModul , m_catId)
        binding.notesFragmentNotesRv.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.notesFragmentNotesRv.addItemDecoration(NoteDecoration(requireContext() , 5f , 0f))
    }

    override fun onStart() {
        super.onStart()
        requireActivity().invalidateOptionsMenu()

    }

    companion object {

        @JvmStatic
        fun newInstance(cat_id : Int) = DiariesFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CAT_ID,cat_id)
            }
        }
    }
}
package com.example.notes_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentDiaryBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.recyclers.adapter.DiaryContentAdapter
import com.example.notes_app.recyclers.item_decoration.NoteContentDecoration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


private const val ARG_ID = "ID"

class DiaryFragment : Fragment() {

    private lateinit var m_binding   : FragmentDiaryBinding
    private lateinit var m_viewModel : MyViewModel

    //my info id && note (get it by its id)
    private var m_id : Int = 0
    private lateinit var m_note : Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            m_id = it.getInt(ARG_ID)
        }

        //viewModel
        m_viewModel = ViewModelProvider(this)[MyViewModel::class.java]



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        m_binding = FragmentDiaryBinding.inflate(inflater)

        return m_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()

    }

    override fun onStart() {
        super.onStart()
        requireActivity().invalidateOptionsMenu()
    }

    private fun setView(){
        var calendar = Calendar.getInstance()
        var time = String.format("%02d/%02d/%02d" , calendar[Calendar.MONTH]+1 , calendar[Calendar.DAY_OF_MONTH] , calendar[Calendar.YEAR] % 100  )
        m_binding.diaryFragmentTimeTv.text = time

        m_viewModel.getNoteById(m_id).observe(this){
            m_note = it
            m_binding.diaryFragmentEmojiIv.setImageResource(m_note.icon)
            m_binding.diaryFragmentTitleTv.setText(m_note.title)
        }

        var adapter = DiaryContentAdapter(m_id , this , requireContext() , m_viewModel )
        m_binding.recycler.adapter = adapter
        m_binding.recycler.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false )
        m_binding.recycler.addItemDecoration(NoteContentDecoration(requireContext()))


    }

    companion object {

        @JvmStatic
        fun newInstance(id: Int) =
            DiaryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID, id)
                }
            }
    }
}


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
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.recyclers.adapter.NotesAdapter
import com.example.notes_app.recyclers.item_decoration.NoteDecoration
import com.example.notes_app.ui.interfaces.DialogViewer
import com.example.notes_app.ui.interfaces.OnClickNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public const val ARG_CAT_ID="cat_id"

class DiariesFragment : Fragment() {

    private lateinit var binding : FragmentNotesBinding
    private lateinit var m_ViewModul : MyViewModel
    private lateinit var m_onClickNavigator : OnClickNavigator

    //adapter to manipulate the data
    private lateinit var m_adapter : NotesAdapter

    private var m_catId : Long = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnClickNavigator){
            m_onClickNavigator = context
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            m_catId = getLong(ARG_CAT_ID)
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

        m_adapter = NotesAdapter(requireContext() , this , m_ViewModul , m_catId)
        binding.notesFragmentNotesRv.adapter = m_adapter
        binding.notesFragmentNotesRv.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.notesFragmentNotesRv.addItemDecoration(NoteDecoration(requireContext() , 5f , 0f))
    }

    override fun onStart() {
        super.onStart()
        requireActivity().invalidateOptionsMenu()

    }

    fun search(s : String , type : String) {
        if (type=="title"){
            GlobalScope.launch {
                var data = m_ViewModul.getNoteByTitle(s)
                withContext(Dispatchers.Main){
                    m_adapter.setData(data)
                }

            }
        }
        else if (type=="hashtag"){
            GlobalScope.launch {
                var data_relation = m_ViewModul.getDiaryHashtagRelationByHashtag(s)
                var notes = ArrayList<Note>()
                data_relation.forEach {
                    var i=0
                    for (note in notes){
                        if (note.id == it.diaryId ){
                            i=1
                        }
                    }
                    if (i==1){

                    }
                    else{
                        var note = m_ViewModul.getNoteByIdRow(it.diaryId)
                        notes.add(note)
                    }
                }
                withContext(Dispatchers.Main){
                    m_adapter.setData(notes)
                }

            }
        }
        else if (type=="content"){
            GlobalScope.launch {
                var data_content = m_ViewModul.getNoteContentsByText(s)
                var notes = ArrayList<Note>()
                data_content.forEach {
                    var i=0
                    for (note in notes){
                        if (note.id == it.note_id){
                            i=1
                        }
                    }
                    if (i==1){

                    }
                    else{
                        var note = m_ViewModul.getNoteByIdRow(it.note_id)
                        notes.add(note)
                    }

                }
                withContext(Dispatchers.Main){
                    m_adapter.setData(notes)
                }

            }
        }
        else if (type=="date"){
            GlobalScope.launch {
                var note = m_ViewModul.getNoteByDate(s)
                var data = ArrayList<Note>()
                if (note!=null){
                    data.add(note)
                }

                withContext(Dispatchers.Main){
                    m_adapter.setData(data)
                }

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(cat_id : Long) = DiariesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CAT_ID,cat_id)
            }
        }
    }
}
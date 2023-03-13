package com.example.notes_app.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.R
import com.example.notes_app.databinding.DialogAddNoteBinding
import com.example.notes_app.databinding.FragmentAddNoteDialogBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Note

public const val ARG_CAT_ID="cat_id"

class AddNoteDialogFragment : DialogFragment() {

    //binding
    private lateinit var binding : DialogAddNoteBinding
    //view model
    private lateinit var m_viewModel : MyViewModel

    private var m_catId : Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            m_catId = getInt(ARG_CAT_ID)
        }
        Toast.makeText(requireContext() , "${m_catId}" , Toast.LENGTH_LONG).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set up the view model
        m_viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //set the background to transparent
        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //set up binding
        binding = DialogAddNoteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNoteDialogCancelBtn.setOnClickListener {
            this.dialog?.dismiss()
        }

        binding.addNoteDialogAddBtn.setOnClickListener {
            var name = binding.addNoteDialogTitleTv.text.toString()
            var description = binding.addNoteDialogDescriptionTv.text.toString()
//            m_viewModel.addNote(Note())
            this.dialog?.dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(cat_id : Int ) = AddNoteDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CAT_ID , cat_id)
            }
        }
    }

    private fun add_note(){

    }
}
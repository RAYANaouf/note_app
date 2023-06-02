package com.example.notes_app.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.notes_app.databinding.DialogAddTasksBinding
import com.example.notes_app.ui.interfaces.AddTaskInterface

open class AddTasksDialog: DialogFragment() {

    private lateinit var m_binding : DialogAddTasksBinding
    private lateinit var m_addTasksInterface  : AddTaskInterface


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set the binding
        m_binding = DialogAddTasksBinding.inflate(inflater)

        if (parentFragment is AddTaskInterface){
            m_addTasksInterface = parentFragment as AddTaskInterface
        }


        return m_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAllOnClicks()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    }


    private fun setAllOnClicks(){
        m_binding.addTaskDialogCancelTv.setOnClickListener {
            dialog?.dismiss()
        }
        m_binding.addTaskDialogAddTv.setOnClickListener {
            m_addTasksInterface.addTask(m_binding.addTaskDialogTaskTextEt.text.toString())
            dialog?.dismiss()
        }
    }

    companion object{
        fun newInstance():AddTasksDialog= AddTasksDialog()
    }
}
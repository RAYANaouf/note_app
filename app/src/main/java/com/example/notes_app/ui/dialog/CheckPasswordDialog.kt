package com.example.notes_app.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.databinding.DialogCheckPasswordBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.ui.fragments.MainFragment
import kotlinx.coroutines.*

class CheckPasswordDialog : DialogFragment() {

    private lateinit var m_binding : DialogCheckPasswordBinding
    private lateinit var m_viewModel : MyViewModel
    private lateinit var m_listener  : MainFragment.CheckPasswordDialogListener
    private var m_email=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set up binding
        m_binding = DialogCheckPasswordBinding.inflate(inflater)
        //set up the view model
        m_viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        return m_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun setOnClicks(){
        m_binding.checkPasswordDialogCancelTv.setOnClickListener {
            dialog?.dismiss()
        }
        m_binding.checkPasswordDialogDoneTv.setOnClickListener {
            m_binding.checkPasswordDialogTaskTextTiet.visibility=View.INVISIBLE
            m_binding.checkPasswordDialogCheckingPb.visibility=View.VISIBLE
            GlobalScope.launch {
                var user = async {
                    m_viewModel.getUserByEmail(m_email)
                }.await()


                withContext(Dispatchers.Main){
                    m_binding.checkPasswordDialogTaskTextTiet.visibility=View.VISIBLE
                    m_binding.checkPasswordDialogCheckingPb.visibility=View.GONE

                    var lock = user.password != m_binding.checkPasswordDialogTaskTextTiet.text.toString()
                    m_listener.onChecked(lock)
                    if (lock){
                        m_binding.checkPasswordDialogTaskTextTiet.setText("")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        this.dismiss()
    }

    companion object{
        fun newInstance(email:String , listener : MainFragment.CheckPasswordDialogListener) : CheckPasswordDialog {
            var dialog = CheckPasswordDialog()
            dialog.m_email=email
            dialog.m_listener = listener
            return dialog
        }
    }
}
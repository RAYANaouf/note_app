package com.example.notes_app.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.DialogCheckPasswordBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.ui.fragments.MainFragment
import kotlinx.coroutines.*



class CheckPasswordDialog : DialogFragment() {

    private lateinit var m_binding : DialogCheckPasswordBinding
    private lateinit var m_viewModel : MyViewModel
    private lateinit var m_listener  : MainFragment.CheckPasswordDialogListener
    private lateinit var m_register  : RegesterHandler
    private var m_email =""
    private var m_type  =""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set up binding
        m_binding = DialogCheckPasswordBinding.inflate(inflater)
        //set up the view model
        m_viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        //regester
        m_register = RegesterHandler(requireContext())

        return m_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
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
            m_binding.checkPasswordDialogEntryTiet.visibility=View.INVISIBLE
            m_binding.checkPasswordDialogCheckingPb.visibility=View.VISIBLE
            GlobalScope.launch {
                var user = async {
                    m_viewModel.getUserByEmail(m_email)
                }.await()



                if(m_type== CHECK_PASSWORD){
                    withContext(Dispatchers.Main){
                        m_binding.checkPasswordDialogEntryTiet.visibility=View.VISIBLE
                        m_binding.checkPasswordDialogCheckingPb.visibility=View.GONE

                        var lock = user.password != m_binding.checkPasswordDialogEntryTiet.text.toString()
                        m_listener.onChecked(lock)
                        if (lock){
                            m_binding.checkPasswordDialogEntryTiet.setText("")
                        }
                    }
                }
                else if(m_type== CHANGE_EMAIL){

                    withContext(Dispatchers.Main){
                        m_binding.checkPasswordDialogEntryTiet.visibility=View.VISIBLE
                        m_binding.checkPasswordDialogCheckingPb.visibility=View.GONE

                        user.email = m_binding.checkPasswordDialogEntryTiet.text.toString()
                        m_register.change_user_email(user.email )
                    }

                    var job = m_viewModel.updateUser(user)
//                    m_register.change_user_email(user.email)
                    job.join()


                    withContext(Dispatchers.Main){
                        m_listener.onChecked(false)
                    }

                }
                else if(m_type== CHANGE_PASSWORD){
                    withContext(Dispatchers.Main){
                        user.password = m_binding.checkPasswordDialogEntryTiet.text.toString()
                    }

                    var job = m_viewModel.updateUser(user)
                    job.join()

                    withContext(Dispatchers.Main){
                        m_binding.checkPasswordDialogEntryTiet.visibility=View.VISIBLE
                        m_binding.checkPasswordDialogCheckingPb.visibility=View.GONE
                        this@CheckPasswordDialog.dismiss()
                    }
                }
            }
        }
    }

    fun setView(){
        if (m_type==CheckPasswordDialog.CHECK_PASSWORD)
            m_binding.checkPasswordDialogLabelTv.setText("enter the password")
        else if (m_type==CheckPasswordDialog.CHANGE_PASSWORD)
            m_binding.checkPasswordDialogLabelTv.setText("enter the new password")
        else if (m_type==CheckPasswordDialog.CHANGE_EMAIL)
            m_binding.checkPasswordDialogLabelTv.setText("enter the new email adress")

    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().invalidateOptionsMenu()
    }

    companion object{
        fun newInstance( type : String , email:String , listener : MainFragment.CheckPasswordDialogListener) : CheckPasswordDialog {
            var dialog = CheckPasswordDialog()
            dialog.m_email    = email
            dialog.m_listener = listener
            dialog.m_type     = type
            return dialog
        }


        const val CHECK_PASSWORD  = "password_check"
        const val CHANGE_PASSWORD = "password_change"
        const val CHANGE_EMAIL    = "email_change"

    }
}
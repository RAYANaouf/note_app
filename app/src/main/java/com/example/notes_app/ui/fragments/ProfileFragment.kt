package com.example.notes_app.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.databinding.FragmentProfileBinding
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.recyclers.adapter.NotesAdapter
import com.example.notes_app.recyclers.item_decoration.NoteDecoration
import com.example.notes_app.ui.dialog.CheckPasswordDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {

    private lateinit var  m_binding   : FragmentProfileBinding
    private lateinit var  m_viewModel : MyViewModel
    private lateinit var  m_handler   : RegesterHandler

    private var           m_email     : String = ""
    private lateinit var  m_user      : User

    private lateinit var m_adapter    : NotesAdapter
    private lateinit var m_dialog     : CheckPasswordDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view model
        m_viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        m_handler   = RegesterHandler(requireContext())

        //get email
        m_email     = m_handler.conn_user()
        Toast.makeText(requireContext() , "$m_email" , Toast.LENGTH_LONG).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // view binding
        m_binding = FragmentProfileBinding.inflate(inflater)


        return m_binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()

        setOnClicks()

    }

    override fun onStart() {
        super.onStart()
        requireActivity().invalidateOptionsMenu()
    }


    private fun setOnClicks(){

        var listener = object: MainFragment.CheckPasswordDialogListener {
            override fun onChecked( lock: Boolean ) {
                if (lock){

                }
                else{
                    m_email = m_handler.conn_user()
                    setView()
                    m_dialog.dismiss()

                }
            }
        }

        m_binding.profileFragmentChangeEmailTv.setOnClickListener {
            m_dialog = CheckPasswordDialog.newInstance(CheckPasswordDialog.CHECK_PASSWORD , m_email , object: MainFragment.CheckPasswordDialogListener {
                override fun onChecked( lock: Boolean ) {
                    if (lock){
                        Toast.makeText(requireContext() , "the password isn't correct" , Toast.LENGTH_SHORT).show()
                    }
                    else{

                        m_dialog = CheckPasswordDialog.newInstance(CheckPasswordDialog.CHANGE_EMAIL , m_email , listener)
                        m_dialog.show(childFragmentManager , "")

                    }
                }
            })
            m_dialog.show(childFragmentManager , "")


        }

        m_binding.profileFragmentChangePasswordTv.setOnClickListener {
            m_dialog = CheckPasswordDialog.newInstance(CheckPasswordDialog.CHECK_PASSWORD , m_email , object: MainFragment.CheckPasswordDialogListener {
                override fun onChecked( lock: Boolean ) {
                    if (lock){
                        Toast.makeText(requireContext() , "the password isn't correct" , Toast.LENGTH_SHORT).show()
                    }
                    else{

                        m_dialog = CheckPasswordDialog.newInstance(CheckPasswordDialog.CHANGE_EMAIL , m_email , listener)
                        m_dialog.show(childFragmentManager , "")

                    }
                }
            })
            m_dialog.show(childFragmentManager , "")


        }
    }

    fun setView(){
        GlobalScope.launch {
            m_user = m_viewModel.getUserByEmail(m_email)

            withContext(Dispatchers.Main){

                var image_arrayList = Base64.decode(m_user.photo , Base64.DEFAULT)

                var image_bitmap    = BitmapFactory.decodeByteArray(image_arrayList , 0 , image_arrayList.size)

                m_binding.profileFragmentProfilePictureSiv.setImageBitmap(image_bitmap)
                m_binding.profileFragmentNameTv.setText("${m_user.f_name}   ${m_user.l_name}")
                m_binding.profileFragmentDesTv.setText("email : ${m_user.email}")

                m_adapter = NotesAdapter(requireContext() , this@ProfileFragment , m_viewModel , -1)
                m_binding.profileFragmentPagesRv.adapter = m_adapter
                m_binding.profileFragmentPagesRv.layoutManager = StaggeredGridLayoutManager(1,
                    StaggeredGridLayoutManager.VERTICAL)
                m_binding.profileFragmentPagesRv.addItemDecoration(NoteDecoration(requireContext() , 5f , 0f))

            }

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ProfileFragment()
    }
}
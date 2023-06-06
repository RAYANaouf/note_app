package com.example.notes_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notes_app.R

private  const val ARG_ID = "id"

class ThemeFragment : Fragment() {

    private  var m_id =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            m_id = getInt(ARG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_theme, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(id : Int) =
            ThemeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ID , id)
                }
            }
    }
}
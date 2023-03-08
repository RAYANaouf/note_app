package com.example.notes_app.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.notes_app.R
import com.example.notes_app.databinding.FragmentAddCategoryDialogBinding


class AddCategoryDialogFragment : DialogFragment() {

    //view binding
    private lateinit var binding : FragmentAddCategoryDialogBinding



//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        //set up the view binding
//        binding = FragmentAddCategoryDialogBinding.inflate(inflater)
//        return binding.root
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var dialogBuilder = AlertDialog.Builder(context).setView(R.layout.fragment_add_category_dialog)

         var dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    companion object {

        @JvmStatic
        fun newInstance() = AddCategoryDialogFragment()
    }
}
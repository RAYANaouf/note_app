package com.example.notes_app.ui.dialog

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.notes_app.R
import com.example.notes_app.databinding.DialogImageViewerBinding

private const val ARG_IMG= "image"

class ImageViewer : DialogFragment() {

    private lateinit var m_binding : DialogImageViewerBinding
    private  var m_img : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            m_img = getString(ARG_IMG)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        var dialog = Dialog(requireContext(),com.google.android.material.R.style.ShapeAppearanceOverlay_MaterialComponents_MaterialCalendar_Window_Fullscreen)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        m_binding = DialogImageViewerBinding.inflate(inflater)

        return m_binding.root
    }

    override fun onStart() {
        super.onStart()

        var byteImg = Base64.decode(m_img,0)
        var bitmapImg = BitmapFactory.decodeByteArray(byteImg , 0 , byteImg.size)

        m_binding.dialogImageViewerImageIv.setImageBitmap(bitmapImg)
    }



    companion object{

        public fun newInstance(image : String): ImageViewer {
            return ImageViewer().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMG, image)
                }
            }
        }


    }

}
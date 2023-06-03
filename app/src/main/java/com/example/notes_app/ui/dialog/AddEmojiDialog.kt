package com.example.notes_app.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notes_app.databinding.DialogAddEmojiBinding
import com.example.notes_app.recyclers.adapter.EmojiAdapter
import com.example.notes_app.ui.fragments.AddNoteFragment
import com.example.notes_app.ui.fragments.SetEmoji

class AddEmojiDialog : DialogFragment ()  {

    private lateinit var m_binding : DialogAddEmojiBinding
    private lateinit var m_setEmoji : SetEmoji

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        m_binding = DialogAddEmojiBinding.inflate(layoutInflater)
        return m_binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = EmojiAdapter(object : OnClickEmojiListener{
            override fun onClick(res: Int) {
                m_setEmoji.onSet(res)
                dialog?.dismiss()
            }
        })
        m_binding.addEmojiDialogEmojiRecyclerRv.adapter = adapter
        m_binding.addEmojiDialogEmojiRecyclerRv.layoutManager = GridLayoutManager(requireContext() , 5)
    }


    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


    public interface OnClickEmojiListener{
        fun onClick(res:Int)
    }


    companion object{
        public fun nesInstance(setEmoji : SetEmoji):AddEmojiDialog{
            val dialog = AddEmojiDialog()
            dialog.m_setEmoji = setEmoji
            return dialog
        }
    }


}
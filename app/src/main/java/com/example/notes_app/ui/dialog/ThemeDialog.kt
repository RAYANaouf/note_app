package com.example.notes_app.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.notes_app.databinding.DialogNoteThemeBinding
import com.example.notes_app.modul.dataClasses.MyTabs
import com.example.notes_app.ui.fragments.ThemeFragment
import com.example.notes_app.viewPagerAdapter.ThemeViewPagerAdapter

class ThemeDialog : DialogFragment() {

    private lateinit var m_binding : DialogNoteThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        m_binding=DialogNoteThemeBinding.inflate(inflater)
        return m_binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        m_binding.dialogNoteThemeTabLayoutTl.setupWithViewPager(m_binding.dialogNoteThemeViewPagerVp)
        var adapter = ThemeViewPagerAdapter(childFragmentManager)
        adapter.addTab(MyTabs(ThemeFragment.newInstance(0),"background"))
        adapter.addTab(MyTabs(ThemeFragment.newInstance(1),"color background"))
        m_binding.dialogNoteThemeViewPagerVp.adapter = adapter


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object{
        fun newInstance():ThemeDialog{
            return ThemeDialog()
        }
    }

}
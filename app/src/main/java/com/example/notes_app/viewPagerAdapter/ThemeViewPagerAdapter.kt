package com.example.notes_app.viewPagerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.notes_app.R
import com.example.notes_app.modul.dataClasses.MyTabs

class ThemeViewPagerAdapter: FragmentStatePagerAdapter {

    private var m_tabs = ArrayList<MyTabs>()

    constructor(fragmentManager : FragmentManager) : super(fragmentManager) {

    }

    fun addTab(tab : MyTabs){
        this.m_tabs.add(tab)
    }

    override fun getCount(): Int {
        return m_tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return m_tabs[position].get_name()
    }

    override fun getItem(position: Int): Fragment {
        return m_tabs[position].get_fragment()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val tabView = LayoutInflater.from(container.context).inflate(R.layout.holder_tabs, null) as TextView
        tabView.text = getPageTitle(position)
        container.addView(tabView)
        return super.instantiateItem(container, position)
    }


}
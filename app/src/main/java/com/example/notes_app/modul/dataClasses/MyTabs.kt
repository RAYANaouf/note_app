package com.example.notes_app.modul.dataClasses

import androidx.fragment.app.Fragment

class MyTabs {
    private var m_fragment : Fragment
    private var m_tab_name : String

    constructor(fragment : Fragment , name : String){
        this.m_tab_name = name
        this.m_fragment       = fragment
    }

    fun get_fragment() : Fragment = m_fragment
    fun get_name ()    :String    = m_tab_name

    fun set_fragment   (fragment : Fragment)      { this.m_fragment=fragment}
    fun set_name (name : String) { this.m_tab_name=name}

}
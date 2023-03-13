package com.example.notes_app.recyclers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.Holder> {

    var m_notes : ArrayList<Note> = ArrayList()
    var m_viewModel : MyViewModel
    var m_owner : LifecycleOwner

    constructor(owner : LifecycleOwner , viewModel : MyViewModel){
        this.m_owner = owner
        this.m_viewModel = viewModel
        get_allNotes()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_category , null , false)
        return Holder(view , this )
    }

    override fun getItemCount(): Int {
        return m_notes.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    class Holder : RecyclerView.ViewHolder{

        var root : View
        var adapter : NotesAdapter
        var description : TextView

        constructor(itemView : View , adapter: NotesAdapter):super(itemView){
            this.root=itemView
            this.adapter=adapter
            this.description = root.findViewById(R.id.category_description)
        }

        fun bind(position : Int){
            //do some things
        }
    }


    fun get_allNotes(){
        m_viewModel.getAllNotes().observe(m_owner){
            m_notes = ArrayList()
            for (note in it){
                m_notes.add(note)
            }
            notifyDataSetChanged()
        }
    }




}
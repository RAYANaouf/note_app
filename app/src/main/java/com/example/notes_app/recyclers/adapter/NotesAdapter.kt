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
import org.w3c.dom.Text

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.Holder> {

    var m_notes : ArrayList<Note> = ArrayList()
    var m_viewModel : MyViewModel
    var m_owner : LifecycleOwner
    var m_cat_id : Int

    constructor(owner : LifecycleOwner , viewModel : MyViewModel , cat_id : Int){
        this.m_owner = owner
        this.m_viewModel = viewModel
        this.m_cat_id = cat_id
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
        var name : TextView
        var description : TextView
        var amount : TextView

        constructor(itemView : View , adapter: NotesAdapter):super(itemView){
            this.root=itemView
            this.adapter=adapter
            this.name = itemView.findViewById(R.id.categoryHolder_name)
            this.description = root.findViewById(R.id.categoryHolder_description)
            this.amount = root.findViewById(R.id.categoryHolder_amount)
        }

        fun bind(position : Int){
            amount.setText("${adapter.m_notes[position].cat_id}")
            name.setText("${adapter.m_notes[position].title}")
            description.setText("${adapter.m_notes[position].content}")
        }

    }


    fun get_allNotes(){
        m_viewModel.getNoteByCategory(m_cat_id).observe(m_owner){
            m_notes = ArrayList()
            for (note in it){
                m_notes.add(note)
            }
            notifyDataSetChanged()
        }
    }




}
package com.example.notes_app.recyclers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.R
import com.example.notes_app.modul.room_database.data_classes.Hashtag
import com.google.android.material.card.MaterialCardView

class HashtagAdapter : RecyclerView.Adapter<HashtagAdapter.Holder>() {

    private var m_hashtags = ArrayList<Hashtag>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_hashtag , parent , false)
        var holder = Holder(view,this)
        return holder
    }

    override fun getItemCount(): Int {
        return  m_hashtags.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    fun set_contents(hashtags : ArrayList<Hashtag>){
        this.m_hashtags = hashtags
        notifyDataSetChanged()
    }


    fun clear_contents(){
        this.m_hashtags.clear()
        notifyDataSetChanged()
    }

    fun add_hashtag(hashtag : Hashtag){
        this.m_hashtags.add(hashtag)
        notifyItemInserted(m_hashtags.size)
    }


    class Holder : RecyclerView.ViewHolder{

        var m_materialCardView : MaterialCardView
        var m_adapter : HashtagAdapter
        var m_hashtag : TextView

        constructor(itemView: View , adapter : HashtagAdapter):super(itemView){
            this.m_adapter = adapter
            this.m_materialCardView = itemView.findViewById(R.id.holderHashtag_container_mcv)
            this.m_hashtag = itemView.findViewById(R.id.holderHashtag_hashtag_tv)
        }

        fun bind(pos:Int){
            m_hashtag.setText(m_adapter.m_hashtags[pos].hashtag)

            // Measure the width of the text and adjust the CardView width accordingly
            val textWidth = m_hashtag.paint.measureText(m_adapter.m_hashtags[pos].hashtag)
//            val layoutParams = m_materialCardView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//            layoutParams.width = textWidth.toInt() // Convert to an appropriate width value
//            m_materialCardView.layoutParams = layoutParams
        }
    }
}
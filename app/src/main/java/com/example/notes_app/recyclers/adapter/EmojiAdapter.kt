package com.example.notes_app.recyclers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.ui.dialog.AddEmojiDialog

class EmojiAdapter  : RecyclerView.Adapter<EmojiAdapter.Holder>{

    private var m_emoji = ArrayList<Int>()
    private var m_listener : AddEmojiDialog.OnClickEmojiListener

    constructor(listener :  AddEmojiDialog.OnClickEmojiListener){

        m_listener = listener

        m_emoji.add(R.drawable.emoji1)
        m_emoji.add(R.drawable.emoji2)
        m_emoji.add(R.drawable.emoji3)
        m_emoji.add(R.drawable.emoji4)
        m_emoji.add(R.drawable.emoji5)
        m_emoji.add(R.drawable.emoji7)
        m_emoji.add(R.drawable.emoji6)
        m_emoji.add(R.drawable.emoji8)
        m_emoji.add(R.drawable.emoji9)
        m_emoji.add(R.drawable.emoji10)
        m_emoji.add(R.drawable.emoji12)
        m_emoji.add(R.drawable.emoji13)
        m_emoji.add(R.drawable.emoji14)
        m_emoji.add(R.drawable.emoji15)
        m_emoji.add(R.drawable.emoji16)
        m_emoji.add(R.drawable.emoji17)
        m_emoji.add(R.drawable.emoji18)
        m_emoji.add(R.drawable.emoji19)
        m_emoji.add(R.drawable.emoji20)
        m_emoji.add(R.drawable.emoji21)
        m_emoji.add(R.drawable.emoji22)
        m_emoji.add(R.drawable.emoji23)
        m_emoji.add(R.drawable.emoji24)
        m_emoji.add(R.drawable.emoji25)
        m_emoji.add(R.drawable.emoji26)
        m_emoji.add(R.drawable.emoji27)
        m_emoji.add(R.drawable.emoji28)
        m_emoji.add(R.drawable.emoji29)
        m_emoji.add(R.drawable.emoji30)
        m_emoji.add(R.drawable.emoji31)
        m_emoji.add(R.drawable.emoji35)
        m_emoji.add(R.drawable.emoji100)
        m_emoji.add(R.drawable.emoji128)
        m_emoji.add(R.drawable.emoji129)
        m_emoji.add(R.drawable.emoji130)
        m_emoji.add(R.drawable.emoji140)
        m_emoji.add(R.drawable.emoji150)
        m_emoji.add(R.drawable.emoji160)
        m_emoji.add(R.drawable.emoji170)
        m_emoji.add(R.drawable.emoji180)
        m_emoji.add(R.drawable.emoji190)
        m_emoji.add(R.drawable.emoji210)
        m_emoji.add(R.drawable.emoji220)
        m_emoji.add(R.drawable.emoji230)
        m_emoji.add(R.drawable.emoji240)
        m_emoji.add(R.drawable.emoji250)
        m_emoji.add(R.drawable.emoji260)
        m_emoji.add(R.drawable.emoji270)















    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_emoji , parent , false)
        return Holder(view , this)
    }

    override fun getItemCount(): Int {
        return m_emoji.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
        holder.container.setOnClickListener {
            m_listener.onClick(m_emoji[position])
        }
    }


    class Holder : RecyclerView.ViewHolder{

        var adapter : EmojiAdapter
        var container : ConstraintLayout
        var emj : ImageView

        constructor(itemView: View , adapter : EmojiAdapter):super(itemView){
            this.adapter = adapter
            container = itemView.findViewById(R.id.holderEmoji_container_cl)
            emj = itemView.findViewById(R.id.holderEmoji_emoji_iv)
        }

        fun bind(pos : Int){
            emj.setImageResource(adapter.m_emoji[pos])
        }
    }
}
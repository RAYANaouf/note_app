package com.example.notes_app.recyclers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.ui.fragments.MainFragment

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.Holder> {

    private var m_OnClickListener : MainFragment.DailyAdapterListener

    constructor(listener : MainFragment.DailyAdapterListener){
        this.m_OnClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_active_daily , parent , false)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.root.setOnClickListener {
            m_OnClickListener.onClick()
        }
    }

    class Holder : RecyclerView.ViewHolder{

        var root : ConstraintLayout

        constructor(itemView : View):super(itemView){
            root = itemView.findViewById(R.id.holderActiveDaily_container_csl)
        }
    }
}
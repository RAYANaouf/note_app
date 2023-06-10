package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.ui.fragments.MainFragment
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.Holder> {

    private var m_OnClickListener : MainFragment.DailyAdapterListener
    private var m_accountHandler : RegesterHandler
    private var m_viewModule : MyViewModel
    private var m_user : User
    private var m_photoBitmap : Bitmap

    constructor(listener : MainFragment.DailyAdapterListener, context : Context, m_viewModule: MyViewModel){
        this.m_OnClickListener = listener
        this.m_accountHandler  = RegesterHandler(context)
        var email = m_accountHandler.conn_user()
        this.m_viewModule = m_viewModule

        runBlocking {
            m_user = async {
                m_viewModule.getUserByEmail(email)
            }.await()
        }

        var byteArray = Base64.decode(m_user.photo , Base64.DEFAULT)
        m_photoBitmap = BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.size)

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
        holder.photo.setImageBitmap(m_photoBitmap)

    }

    class Holder : RecyclerView.ViewHolder{

        var root  : ConstraintLayout
        var photo : ShapeableImageView

        constructor(itemView : View):super(itemView){
            root = itemView.findViewById(R.id.holderActiveDaily_container_csl)
            photo = itemView.findViewById(R.id.holderActiveDaily_photo_siv)
        }
    }
}
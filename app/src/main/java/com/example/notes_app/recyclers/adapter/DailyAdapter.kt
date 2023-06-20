package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.ui.dialog.CheckPasswordDialog
import com.example.notes_app.ui.fragments.MainFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.*
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import kotlin.collections.ArrayList

class DailyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
    private var m_OnClickListener : MainFragment.DailyAdapterListener
    private var m_accountHandler : RegesterHandler
    private var m_viewModule : MyViewModel
    private var m_context: Context
    private lateinit var m_user : User
    private var m_photoBitmap : Bitmap
    private  var m_content = ArrayList<Note>()
    private var m_owner   : LifecycleOwner

    constructor(listener : MainFragment.DailyAdapterListener, context : Context, m_viewModule: MyViewModel , owner : LifecycleOwner){
        this.m_OnClickListener = listener
        this.m_owner = owner
        this.m_context = context
        this.m_accountHandler  = RegesterHandler(m_context)
        var email = m_accountHandler.conn_user()
        this.m_viewModule = m_viewModule

        runBlocking {
            launch {
                m_user =     m_viewModule.getUserByEmail(email)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            m_viewModule.getAllNotes().observe(m_owner){
                m_content.addAll(it)
                notifyDataSetChanged()
            }
        }

        var byteArray = Base64.decode(m_user.photo , Base64.DEFAULT)
        m_photoBitmap = BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.size)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0 ){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_diary , parent , false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            return HolderDaily(view , this)
        }else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_close_diary , parent , false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            return HolderLockedDaily(view , this)
        }


    }

    override fun getItemCount(): Int {
        return m_content.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is HolderLockedDaily){
            holder.bind(position)
        }
        else if (holder is HolderDaily)
        {
            holder.bind(position)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (m_content[position].lock == false ){
            0
        }
        else{
            1
        }

    }

    class HolderLockedDaily : RecyclerView.ViewHolder{

        var m_adapter   : DailyAdapter
        var m_container : MaterialCardView
        var m_date      : TextView
        var ratingBar   : MaterialRatingBar
        var m_emoji     : ImageView

        constructor(itemView : View , adapter: DailyAdapter):super(itemView){
            this.m_adapter = adapter
            this.m_container = itemView.findViewById(R.id.holderCloseDaily_container_mcv)
            this.m_date    = itemView.findViewById(R.id.holderCloseDaily_date_tv)
            ratingBar      = itemView.findViewById(R.id.holderCloseDaily_rateDay_mrb)
            m_emoji        = itemView.findViewById(R.id.holderCloseDaily_emoji_iv)
        }

        fun bind(pos : Int){
            ratingBar.rating = m_adapter.m_content[pos].rate

            m_date.setText(m_adapter.m_content[pos].date)

            m_emoji.setImageResource(m_adapter.m_content[pos].icon)

            m_container.setOnClickListener {
                m_adapter.m_OnClickListener.onClickLocked(m_adapter.m_content[pos].id , m_adapter.m_content[pos].rate)
            }

        }
    }
    class HolderDaily : RecyclerView.ViewHolder{

        var m_adapter     : DailyAdapter
        var m_date        : TextView
        var m_container     : MaterialCardView
        var m_ratingBar     : MaterialRatingBar
        var m_emoji       : ImageView
        var m_title       : TextView
        var m_description : TextView

        constructor(itemView : View , adapter: DailyAdapter):super(itemView){
            this.m_adapter = adapter
            this.m_date    = itemView.findViewById(R.id.holderDaily_date_tv)
            this.m_container      = itemView.findViewById(R.id.holderDaily_container_mcv)
            m_ratingBar      = itemView.findViewById(R.id.holderDaily_rateDay_mrb)
            m_emoji        = itemView.findViewById(R.id.holderDaily_emoji_iv)
            m_title        = itemView.findViewById(R.id.holderDaily_title_tv)
            m_description  = itemView.findViewById(R.id.holderDaily_description_tv)
        }

        fun bind(pos : Int){

            m_ratingBar.rating = m_adapter.m_content[pos].rate
            m_date.setText(m_adapter.m_content[pos].date)
            m_emoji.setImageResource(m_adapter.m_content[pos].icon)
            m_title.setText(m_adapter.m_content[pos].title)
            m_description.setText(m_adapter.m_content[pos].description)
            if (m_adapter.m_content[pos].color!=0){
                m_container.setStrokeColor(m_adapter.m_content[pos].color)
                m_ratingBar.backgroundTintList= ColorStateList.valueOf(m_adapter.m_content[pos].color)
                m_ratingBar.progressTintList = ColorStateList.valueOf(m_adapter.m_content[pos].color)
                m_ratingBar.secondaryProgressTintList = ColorStateList.valueOf(m_adapter.m_content[pos].color)

            }

            m_container.setOnClickListener {
                m_adapter.m_OnClickListener.onClickOpened(m_adapter.m_content[pos].id , m_adapter.m_content[pos].rate)
            }



        }
    }
}
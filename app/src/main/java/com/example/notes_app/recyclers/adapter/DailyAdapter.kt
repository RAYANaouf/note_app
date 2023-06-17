package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.ui.fragments.MainFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.*
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.util.*
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
    private var m_date = ""

    constructor(listener : MainFragment.DailyAdapterListener, context : Context, m_viewModule: MyViewModel , owner : LifecycleOwner){
        var calendar = Calendar.getInstance()
        this.m_date = String.format("%02d/%02d/%02d" , calendar[Calendar.MONTH]+1 , calendar[Calendar.DAY_OF_MONTH] , calendar[Calendar.YEAR] )
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

           launch {

           }
        }

        CoroutineScope(Dispatchers.Main).launch {
            m_viewModule.getAllNotes().observe(m_owner){
                var calendar = Calendar.getInstance()
                var date = String.format("%02d/%02d/%02d",calendar[Calendar.MONTH]+1 , calendar[Calendar.DAY_OF_MONTH] , calendar[Calendar.YEAR])
                m_content.add(Note(cat_id = 1 ,date = date , theme = -1 , title = "" , content = ""))
                m_content.addAll(it)
                notifyDataSetChanged()
            }
        }



        var byteArray = Base64.decode(m_user.photo , Base64.DEFAULT)
        m_photoBitmap = BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.size)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if (viewType==0){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_daily_active , parent , false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            return HolderActiveDaily(view , this)
        }
        else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_daily , parent , false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            return HolderDaily(view , this)
        }



    }

    override fun getItemCount(): Int {
        return m_content.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is HolderActiveDaily){
            holder.bind(position)
        }
        else if (holder is HolderDaily)
        {
            holder.bind(position)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class HolderActiveDaily : RecyclerView.ViewHolder{

        var m_adapter : DailyAdapter
        var m_date    : TextView
        var root      : ConstraintLayout
        var photo     : ShapeableImageView
        var ratingBar : MaterialRatingBar
        var m_emoji   : ImageView

        constructor(itemView : View , adapter: DailyAdapter):super(itemView){
            this.m_adapter = adapter
            this.m_date    = itemView.findViewById(R.id.holderActiveDaily_date_tv)
            root           = itemView.findViewById(R.id.holderActiveDaily_container_csl)
            photo          = itemView.findViewById(R.id.holderActiveDaily_photo_siv)
            ratingBar      = itemView.findViewById(R.id.holderActiveDaily_rateDay_mrb)
            m_emoji        = itemView.findViewById(R.id.holderActiveDaily_emoji_siv)
        }

        fun bind(pos : Int){
            photo.setImageBitmap(m_adapter.m_photoBitmap)
            ratingBar.rating = m_adapter.m_content[pos].rate

            m_date.setText(m_adapter.m_date)

            ratingBar.setOnTouchListener(object:OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event!!.action === MotionEvent.ACTION_UP) {
                        // User released the rating bar, handle the action here
                        val rating: Float = ratingBar.getRating()
                        // Perform your desired action with the rating value

                        m_adapter.m_OnClickListener.onClick(rating)

                    }
                    return false
                }

            })

            ratingBar.setOnRatingChangeListener { ratingBar, rating ->
                if (rating>7){
                    m_emoji.setImageResource(R.drawable.emoji7)
                }
                else if(rating>6){
                    m_emoji.setImageResource(R.drawable.emoji7)
                }
                else if(rating>5){
                    m_emoji.setImageResource(R.drawable.emoji6)
                }
                else if(rating>4){
                    m_emoji.setImageResource(R.drawable.emoji5)
                }
                else if(rating>3){
                    m_emoji.setImageResource(R.drawable.emoji4)
                }
                else if(rating>2){
                    m_emoji.setImageResource(R.drawable.emoji3)
                }
                else if(rating>1){
                    m_emoji.setImageResource(R.drawable.emoji2)
                }
                else {
                    m_emoji.setImageResource(R.drawable.emoji1)
                }
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
            m_description.setText(m_adapter.m_content[pos].content)
            if (m_adapter.m_content[pos].color!=0){
                m_container.setStrokeColor(m_adapter.m_content[pos].color)
                m_ratingBar.backgroundTintList= ColorStateList.valueOf(m_adapter.m_content[pos].color)
                m_ratingBar.progressTintList = ColorStateList.valueOf(m_adapter.m_content[pos].color)
                m_ratingBar.secondaryProgressTintList = ColorStateList.valueOf(m_adapter.m_content[pos].color)

            }



        }
    }
}
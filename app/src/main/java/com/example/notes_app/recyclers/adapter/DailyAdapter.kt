package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.classes.RegesterHandler
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.data_classes.User
import com.example.notes_app.ui.fragments.MainFragment
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.util.Calendar

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.Holder> {

    private var m_OnClickListener : MainFragment.DailyAdapterListener
    private var m_accountHandler : RegesterHandler
    private var m_viewModule : MyViewModel
    private var m_context: Context
    private var m_user : User
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
            m_user = async {
                m_viewModule.getUserByEmail(email)
            }.await()

           launch {
               m_viewModule.getAllNotes().observe(m_owner){
                   var calendar = Calendar.getInstance()
                   var date = String.format("%02d/%02d/%02d",calendar[Calendar.MONTH]+1 , calendar[Calendar.DAY_OF_MONTH] , calendar[Calendar.YEAR])
                   m_content.add(Note(cat_id = 1 ,date = date , theme = -1 , title = "" , content = ""))
                   for (note in it){
                       m_content.add(note)
                   }
                   notifyDataSetChanged()
               }
           }
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
        return m_content.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        if(position==0){

            holder.photo.setImageBitmap(m_photoBitmap)
            holder.ratingBar.setOnTouchListener(object:OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event!!.action === MotionEvent.ACTION_UP) {
                        // User released the rating bar, handle the action here
                        val rating: Float = holder.ratingBar.getRating()
                        // Perform your desired action with the rating value

                        m_OnClickListener.onClick(rating)

                    }
                    return false
                }

            })
        }
        else
        {
            holder.ratingBar.rating = m_content[position].rate 
        }

    }

    class Holder : RecyclerView.ViewHolder{

        var root  : ConstraintLayout
        var photo : ShapeableImageView
        var ratingBar : MaterialRatingBar

        constructor(itemView : View):super(itemView){
            root = itemView.findViewById(R.id.holderActiveDaily_container_csl)
            photo = itemView.findViewById(R.id.holderActiveDaily_photo_siv)
            ratingBar = itemView.findViewById(R.id.holderActiveDaily_rateDay_mrb)
        }
    }
}
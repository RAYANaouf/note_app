package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Category
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainCategoriesAdapter : RecyclerView.Adapter<MainCategoriesAdapter.Holder> {

    private var m_owner : LifecycleOwner
    private var m_viewModul : MyViewModel
    private var m_mainCategories : ArrayList<Category> = ArrayList()
    private var m_onClickListener : OnClickListener
    private var m_context         : Context

    constructor(context : Context, owner : LifecycleOwner, viewModul: MyViewModel, onClickListener: OnClickListener){
        this.m_owner = owner
        this.m_viewModul = viewModul
        this.m_context   = context
        getCategories()
        m_onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_main_category , null , false)
        return Holder(view , this)
    }

    override fun getItemCount(): Int {
        return m_mainCategories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.apply {
            bind(position)
            setOnClickListener( m_mainCategories[position].id )
        }

    }


    class Holder : RecyclerView.ViewHolder{

        var m_adapter : MainCategoriesAdapter
        var recycler  : RecyclerView
        var r_adapter : HashtagAdapter
        var m_rootView : View
        var logo : ShapeableImageView
        var name : TextView
        var description : TextView

        constructor(itemView : View , adapter : MainCategoriesAdapter ):super(itemView){
            this.m_adapter = adapter
            this.recycler  = itemView.findViewById(R.id.mainCategoryHolder_hashtags_rv)
            this.r_adapter = HashtagAdapter()
            this.recycler.adapter = this.r_adapter
            this.recycler.layoutManager = LinearLayoutManager(m_adapter.m_context , LinearLayoutManager.HORIZONTAL , false)
            this.m_rootView = itemView
            this.logo = itemView.findViewById(R.id.mainCategory_logo)
            this.name = itemView.findViewById(R.id.mainCategory_name)
            this.description = itemView.findViewById(R.id.mainCategory_description)
        }

        fun bind(position : Int ){

            var byteArray_img = Base64.decode(m_adapter.m_mainCategories[position].image,Base64.DEFAULT)
            var bitmape_img = BitmapFactory.decodeByteArray(byteArray_img,0,byteArray_img.size)

            logo.setImageBitmap(bitmape_img)
            name.setText("${m_adapter.m_mainCategories[position].name}")
            description.setText("${m_adapter.m_mainCategories[position].description}")


            GlobalScope.launch {
                var relations = m_adapter.m_viewModul.getHashtagsByCategoryId(m_adapter.m_mainCategories[position].id)
                relations.forEach {
                    var hashtag = m_adapter.m_viewModul.getHashtagById(it.hashtagId)
                    withContext(Dispatchers.Main){
                        r_adapter.add_hashtag(hashtag)
                    }
                }
            }
        }

        fun setOnClickListener(position : Long){
            m_rootView.setOnClickListener {
                m_adapter.m_onClickListener.onClick_mainCategory(position)
            }
        }
    }


    fun getCategories(){
        m_viewModul.getAllCategories().observe(m_owner){
            m_mainCategories = ArrayList()
            for (i in 0 until it.size){
                m_mainCategories.add(it[i])
            }
            notifyDataSetChanged()
        }
    }

    interface OnClickListener{
        fun onClick_mainCategory(cat_id : Long)
    }

}
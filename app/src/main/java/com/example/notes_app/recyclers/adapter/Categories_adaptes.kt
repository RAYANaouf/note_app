package com.example.notes_app.recyclers.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.Category
import com.google.android.material.imageview.ShapeableImageView

class Categories_adaptes : RecyclerView.Adapter<Categories_adaptes.Holder> {

    private var m_viewModul:MyViewModel
    private var m_owner : LifecycleOwner
    private var m_categories : ArrayList<Category> = ArrayList()
    private var m_onClickListener : OnClickListener


    constructor(owner: LifecycleOwner, myViewModel : MyViewModel, onClickListener: OnClickListener){
        this.m_viewModul = myViewModel
        this.m_owner = owner
        getCategories()
        m_onClickListener = onClickListener
    }

    fun getCategories(){
        m_viewModul.getAllCategories().observe(m_owner){
            m_categories = ArrayList()

            if (it.size>3){
                for (i in 3 .. it.size-1){
                    m_categories.add(it[i])
                }
                notifyDataSetChanged()
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_category,null,false)

        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
//        view.setOnClickListener {
//            m_onClickNavigator.onClick_to_notesFragment()
//        }

        return Holder(view , this)
    }

    override fun getItemCount(): Int {
        return m_categories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(m_categories[position])
        holder.set_onClickListener(m_categories[position].id)
    }



    class Holder : RecyclerView.ViewHolder{

        var m_adapter : Categories_adaptes
        var m_root : View
        var shapeableImageView : ShapeableImageView
        var name : TextView
        var description : TextView

        constructor(itemView: View , adapter : Categories_adaptes) : super(itemView){
            shapeableImageView = itemView.findViewById(R.id.categoryHolder_logo)
            name = itemView.findViewById(R.id.categoryHolder_name)
            description = itemView.findViewById(R.id.categoryHolder_description)
            this.m_adapter = adapter
            this.m_root = itemView
        }

        fun bind(cat : Category){
            name.setText(cat.name)
            description.setText("${cat.description}")
            var byteArray = Base64.decode(cat.image , Base64.DEFAULT)
            var bitmap_img = BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.size)
            shapeableImageView.setImageBitmap(bitmap_img)
        }

        fun set_onClickListener(cat_id : Long){
            m_root.setOnClickListener {
                m_adapter.m_onClickListener.onClick_category(cat_id)
            }
        }
    }


    interface OnClickListener{
        fun onClick_category(cat_id : Long)
    }

}
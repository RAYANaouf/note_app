package com.example.notes_app.recyclers.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.modul.MyViewModul
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.ui.dialog.OnClickNavigator
import com.google.android.material.imageview.ShapeableImageView

class Category_adaptes : RecyclerView.Adapter<Category_adaptes.Holder> {

    private var m_viewModul:MyViewModul
    private var m_owner : LifecycleOwner
    private var m_categories : ArrayList<Category> = ArrayList()
    private var m_onClickNavigator : OnClickNavigator


    constructor(owner: LifecycleOwner , myViewModul : MyViewModul , onClickNavigator: OnClickNavigator){
        this.m_viewModul = myViewModul
        this.m_owner = owner
        getCategories()
        m_onClickNavigator = onClickNavigator
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
        view.setOnClickListener {
            m_onClickNavigator.onClick_to_notesFragment()
        }

        return Holder(view , this)
    }

    override fun getItemCount(): Int {
        return m_categories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(m_categories[position])
    }



    class Holder : RecyclerView.ViewHolder{

        var m_adapter : Category_adaptes
        var shapeableImageView : ShapeableImageView
        var name : TextView
        var description : TextView
        var amount : TextView

        constructor(itemView: View , adapter : Category_adaptes) : super(itemView){
            shapeableImageView = itemView.findViewById(R.id.category_logo)
            name = itemView.findViewById(R.id.category_name)
            description = itemView.findViewById(R.id.category_description)
            amount = itemView.findViewById(R.id.category_amount)
            this.m_adapter = adapter
        }

        fun bind(cat : Category){
            name.setText(cat.name)
            description.setText("${cat.description}")
            var byteArray = Base64.decode(cat.image , Base64.DEFAULT)
            var bitmap_img = BitmapFactory.decodeByteArray(byteArray , 0 , byteArray.size)
            shapeableImageView.setImageBitmap(bitmap_img)
        }
    }

}
package com.example.notes_app.recyclers.adapter

import android.content.Context
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
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import com.google.android.material.checkbox.MaterialCheckBox

class DiaryContentAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  var m_viewModel : MyViewModel
    private var m_contents = ArrayList<NoteContent>()
    private var m_context : Context
    private var m_id = 0

    constructor(id : Int, owner : LifecycleOwner, context : Context, viewModel : MyViewModel){

        this.m_context = context
        this.m_viewModel = viewModel



        m_viewModel.getAllNoteContents(id).observe(owner){
            m_contents.addAll(it)
            notifyDataSetChanged()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_diary_content_text,null,false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            var viewholder = TextHolder(view, this)
            return viewholder
        }
        if(viewType == 1){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_note_content_checkbox,null,false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            var viewholder = CheckboxHolder(view, this)
            return viewholder
        }
        else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_note_content_image,null,false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            var viewholder = ImageHolder(view, this)
            return viewholder
        }
    }

    override fun getItemCount(): Int {
        return m_contents.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TextHolder){
            holder.bind(position)

        }
        else if (holder is CheckboxHolder){
            holder.bind(position)
        }
        else if(holder is ImageHolder){
            holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return m_contents[position].type
    }



    class TextHolder : RecyclerView.ViewHolder{
        var text : TextView
        var m_adapter : DiaryContentAdapter
        constructor(itemView : View, adapter : DiaryContentAdapter):super(itemView){
            text = itemView.findViewById(R.id.holderDiaryContentText_text_tv)
            this.m_adapter =adapter
        }

        fun bind(pos:Int){
            text.setText(m_adapter.m_contents[pos].cont)
        }
    }

    class ImageHolder : RecyclerView.ViewHolder{
        var image : ImageView
        var m_adapter : DiaryContentAdapter
        constructor(itemView : View, adapter : DiaryContentAdapter):super(itemView){
            image = itemView.findViewById(R.id.holderNoteContentImage_image_iv)
            this.m_adapter =adapter
        }

        fun bind(pos:Int){
            val image_bytes = Base64.decode(m_adapter.m_contents[pos].cont, 0)
            val image_bitmap = BitmapFactory.decodeByteArray(image_bytes, 0, image_bytes.size)

            image.setImageBitmap(image_bitmap)
        }
    }

    class CheckboxHolder : RecyclerView.ViewHolder{
        var checkbox : MaterialCheckBox
        var m_adapter : DiaryContentAdapter
        constructor(itemView : View, adapter : DiaryContentAdapter):super(itemView){
            checkbox = itemView.findViewById(R.id.holderNoteContent_checkbox_cb)
            this.m_adapter =adapter
        }

        fun bind(pos:Int){
            checkbox.text = this.m_adapter.m_contents[pos].cont
            checkbox.isChecked = this.m_adapter.m_contents[pos].is_check
            // Prevent the checkbox from being checked or unchecked by the user
            checkbox.isClickable = false
            checkbox.isFocusable = false
        }

    }

}
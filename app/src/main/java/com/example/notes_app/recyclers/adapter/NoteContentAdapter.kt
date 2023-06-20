package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import com.example.notes_app.ui.fragments.AddNoteFragment
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText

class NoteContentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var contents : ArrayList<NoteContent>
    private var text_count     : Int = 0
    private var checkbox_count : Int = 0
    private var img_count      : Int = 0
    private var count          : Int = 0
    private var m_context : Context

    private var m_onClickListener : AddNoteFragment.OnClickListener

    constructor(contents : ArrayList<NoteContent>  , context : Context , onClickListener : AddNoteFragment.OnClickListener){
        this.contents = contents
        this.m_context = context
        this.m_onClickListener = onClickListener
    }

    /*******************         to create the holder          ********************/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if(viewType == 0){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_des_note_content,null,false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            var viewholder = DesHoldre(view , this)
            return viewholder
        }
        else if(viewType == 1){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_note_content_checkbox,null,false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            var viewholder = CheckboxHolder(view , this)
            return viewholder
        }
        else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_note_content_image,null,false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            var viewholder = ImageHolder(view , this)
            return viewholder
        }
    }

    /*******************         to get the count           ********************/

    override fun getItemCount(): Int {
        return contents.size

    }


    /********************           on bind method             ********************/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is DesHoldre){
            holder.bind(position)

        }
        else if (holder is CheckboxHolder){
            var checkbox = holder.checkbox
            checkbox.text = this.contents[position].cont
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                contents[position].is_check=isChecked
            }
        }
        else if(holder is ImageHolder){

            val imageBytes = Base64.decode(contents[position].cont, 0)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            holder.image.setImageBitmap(image)

            holder.image.setOnClickListener {
                m_onClickListener.OnCickImage(contents[position].cont)
            }
        }
    }


    /*******************         set the view type         ********************/
    override fun getItemViewType(position: Int): Int {
        return contents[position].type
    }

    /***********************  my functions  ***********************************/

    fun addItem(content:NoteContent){
        when(content.type){
            0->{
                content.type_count    = this.text_count
                content.glonal_count  = this.count
                this.text_count++
                this.count++
            }
            1->{
                content.type_count = this.checkbox_count
                content.glonal_count = this.count
                this.checkbox_count++
                this.count++
            }
            2->{
                content.type_count = this.img_count
                content.glonal_count = this.count
                this.img_count++
                this.count++
            }
        }

        contents.add(contents.size,content)
        notifyItemInserted(contents.size)
    }

    fun get_all_item():ArrayList<NoteContent>{
        return this.contents
    }



    /*******************         the holder inner class         ********************/
    class DesHoldre : RecyclerView.ViewHolder{
        var des : EditText
        var adapter : NoteContentAdapter
        constructor(itemView : View , adapter : NoteContentAdapter):super(itemView){
            des = itemView.findViewById(R.id.holderDesNoteContent_des_tv)
            this.adapter =adapter
        }

        fun bind(pos : Int){

            des.setText(adapter.contents[pos].cont)
            if (adapter.contents[pos].cont=="" && pos>0){
                des.hint = "sent : $pos"
            }

            var textWatcher = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.contents[adapterPosition].cont=s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            }
            des.removeTextChangedListener(textWatcher)
            des.addTextChangedListener (textWatcher)

        }
    }

    class ImageHolder : RecyclerView.ViewHolder{
        var image : ImageView
        var adapter : NoteContentAdapter
        constructor(itemView : View , adapter : NoteContentAdapter):super(itemView){
            image = itemView.findViewById<ImageView>(R.id.holderNoteContentImage_image_iv)
            this.adapter =adapter
        }
    }

    class CheckboxHolder : RecyclerView.ViewHolder{
        var checkbox : MaterialCheckBox
        var adapter : NoteContentAdapter
        constructor(itemView : View , adapter : NoteContentAdapter):super(itemView){
            checkbox = itemView.findViewById(R.id.holderNoteContent_checkbox_cb)
            this.adapter =adapter
        }
    }

}
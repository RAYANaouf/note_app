package com.example.notes_app.recyclers.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.R
import com.example.notes_app.modul.MyViewModel
import com.example.notes_app.modul.room_database.data_classes.DiaryHashtagJoin
import com.example.notes_app.modul.room_database.data_classes.Hashtag
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.recyclers.item_decoration.HashtagDicoration
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.*
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import org.w3c.dom.Text

class NotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var m_content : ArrayList<Note> = ArrayList()
    var m_context  : Context
    var m_viewModel : MyViewModel
    var m_owner : LifecycleOwner
    var m_cat_id : Int

    constructor( context : Context , owner : LifecycleOwner , viewModel : MyViewModel , cat_id : Int){
        this.m_owner = owner
        this.m_viewModel = viewModel
        this.m_cat_id = cat_id
        this.m_context = context
        get_allNotes()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{

//        var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_notev2 , null , false)
//        return Holder(view , this )

        if (viewType == 0 ){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_diary , parent , false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            var holder = HolderDaily(view, HashtagAdapter(), this)
            return  holder

        }else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_close_diary , parent , false)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            return HolderLockedDaily(view, this)
        }

    }

    override fun getItemCount(): Int {
        return m_content.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bind(position)

        if(holder is HolderLockedDaily){
            holder.bind(position)
        }
        else if (holder is HolderDaily)
        {


            GlobalScope.launch {
                var relation : List<DiaryHashtagJoin> = async { m_viewModel.getHashtagsByDiaryId(m_content[position].id) }.await()
                var hashtags = ArrayList<Hashtag>()

                for (r in relation){
                    hashtags.add(Hashtag(r.hashtagId))
                }

                withContext(Dispatchers.Main){
                    holder.bind(position , hashtags)
                }
            }


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



//    class Holder : RecyclerView.ViewHolder{
//
//        var root : MaterialCardView
//        var adapter : NotesAdapter
//        var name : TextView
//        var content : TextView
//        var date    : TextView
//        var separator : View
//        var icon : ShapeableImageView
//
//        constructor(itemView : View , adapter: NotesAdapter):super(itemView){
//            this.root=itemView.findViewById(R.id.noteHolder_root_mcv)
//            this.adapter   =adapter
//            this.name      = itemView.findViewById(R.id.noteHolder_name_tv)
//            this.content   = root.findViewById(R.id.noteHolder_content_tv)
//            this.date      = root.findViewById(R.id.noteHolder_date_tv)
//            this.separator = root.findViewById(R.id.noteHolder_separator_v)
//            this.icon      = root.findViewById(R.id.noteHolder_icon_siv)
//        }
//
//        fun bind(position : Int){
//            name.setText("${adapter.m_notes[position].title}")
//            content.setText("${adapter.m_notes[position].description}")
//            date.setText("${adapter.m_notes[position].date}")
//            root.setStrokeColor(adapter.m_notes[position].color)
//
//            var lighter_version = ColorUtils.blendARGB(adapter.m_notes[position].color, Color.WHITE, 0.7f)
//
//            separator.setBackgroundColor(lighter_version)
//
//            icon.setImageResource(adapter.m_notes[position].icon)
//        }
//
//    }


    fun get_allNotes(){
        if(m_cat_id>=0){
            m_viewModel.getNoteByCategory(m_cat_id).observe(m_owner){
                m_content = ArrayList()
                for (note in it){
                    m_content.add(note)
                }
                notifyDataSetChanged()
            }
        }
        else{
            m_viewModel.getAllNotes().observe(m_owner){
                m_content = ArrayList()
                for (note in it){
                    m_content.add(note)
                }
                notifyDataSetChanged()
            }
        }

    }


    class HolderLockedDaily : RecyclerView.ViewHolder{

        var m_adapter   : NotesAdapter
        var m_container : MaterialCardView
        var m_date      : TextView
        var ratingBar   : MaterialRatingBar
        var m_emoji     : ImageView

        constructor(itemView : View ,  adapter: NotesAdapter):super(itemView){
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
//                m_adapter.m_OnClickListener.onClickLocked(m_adapter.m_content[pos].id , m_adapter.m_content[pos].rate)
            }

        }
    }
    class HolderDaily : RecyclerView.ViewHolder{

        var m_adapter          : NotesAdapter
        var m_recycler         : RecyclerView
        var m_hashtag_adapter  : HashtagAdapter?
        var m_date             : TextView
        var m_container        : MaterialCardView
        var m_ratingBar        : MaterialRatingBar
        var m_emoji            : ImageView
        var m_title            : TextView
        var m_description      : TextView

        constructor(itemView : View , hashtagAdapter: HashtagAdapter  , adapter: NotesAdapter):super(itemView){
            this.m_adapter                     = adapter
            this.m_hashtag_adapter             = hashtagAdapter
            this.m_recycler                    = itemView.findViewById(R.id.holderDaily_hashtags_rv)
            this.m_recycler.adapter            = m_hashtag_adapter
            /*****************       set the flexbox layout manager     &&  item decoration    *************************/
            var flexBoxLayoutManager           = FlexboxLayoutManager(m_adapter.m_context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap      = FlexWrap.WRAP
            this.m_recycler.layoutManager      = flexBoxLayoutManager

            //item decoration
            this.m_recycler.addItemDecoration(HashtagDicoration(m_adapter.m_context , 1f,1f,1f,1f))
            /***************************************************************************************/

            this.m_date      = itemView.findViewById(R.id.holderDaily_date_tv)
            this.m_container = itemView.findViewById(R.id.holderDaily_container_mcv)
            m_ratingBar      = itemView.findViewById(R.id.holderDaily_rateDay_mrb)
            m_emoji          = itemView.findViewById(R.id.holderDaily_emoji_iv)
            m_title          = itemView.findViewById(R.id.holderDaily_title_tv)
            m_description    = itemView.findViewById(R.id.holderDaily_description_tv)

        }

        fun bind(pos : Int , hashtags: ArrayList<Hashtag>){

            m_ratingBar.rating = m_adapter.m_content[pos].rate
            m_date.setText(m_adapter.m_content[pos].date)
            m_emoji.setImageResource(m_adapter.m_content[pos].icon)
            m_title.setText(m_adapter.m_content[pos].title)
            if (m_adapter.m_content[pos].description == ""){
                m_description.visibility = ViewGroup.GONE
            }
            else{
                m_description.setText(m_adapter.m_content[pos].description)
            }

                m_container.setStrokeColor(m_adapter.m_content[pos].color)
                m_ratingBar.backgroundTintList= ColorStateList.valueOf(m_adapter.m_content[pos].color)
                m_ratingBar.progressTintList = ColorStateList.valueOf(m_adapter.m_content[pos].color)
                m_ratingBar.secondaryProgressTintList = ColorStateList.valueOf(m_adapter.m_content[pos].color)


            m_container.setOnClickListener {
//                m_adapter.m_OnClickListener.onClickOpened(m_adapter.m_content[pos].id , m_adapter.m_content[pos].rate)
            }

            m_hashtag_adapter?.set_contents(hashtags)



        }
    }





}
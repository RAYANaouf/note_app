package com.example.notes_app.ui.bottomSheet

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes_app.databinding.BottomSheetHashtagBinding
import com.example.notes_app.modul.room_database.data_classes.Hashtag
import com.example.notes_app.recyclers.adapter.HashtagAdapter
import com.example.notes_app.recyclers.item_decoration.HashtagDicoration
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HashtagBottomSheet : BottomSheetDialogFragment() {

    private lateinit var m_binding : BottomSheetHashtagBinding
    private lateinit var m_hashtags : ArrayList<Hashtag>
    private lateinit var m_adapter : HashtagAdapter





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        m_binding = BottomSheetHashtagBinding.inflate(inflater)
        return m_binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setOnClicks()
    }

    private fun setView(){
        m_adapter = HashtagAdapter()
        m_binding.bottomSheethashtagHashtagrecyclerRv.adapter = m_adapter

        /*****************       set the flexbox layout manager     &&  item decoration    *************************/
        var flexBoxLayoutManager           = FlexboxLayoutManager(requireContext())
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW
        flexBoxLayoutManager.flexWrap      = FlexWrap.WRAP
        m_binding.bottomSheethashtagHashtagrecyclerRv.layoutManager      = flexBoxLayoutManager

        //item decoration
        m_binding.bottomSheethashtagHashtagrecyclerRv.addItemDecoration(HashtagDicoration(requireContext() , 4f,4f,3f,3f))
        /***************************************************************************************/

        m_adapter.set_contents(m_hashtags)

        m_binding.bottomSheethashtagHashtagFieldTiet.requestFocus()


    }

    private fun setOnClicks(){

        m_binding.bottomSheethashtagHashtagFieldTiet.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var origin = s.toString()
                var updated = origin.replace(" ","_")
                if(origin != updated){
                    m_binding.bottomSheethashtagHashtagFieldTiet.setText("$updated")
                    m_binding.bottomSheethashtagHashtagFieldTiet.setSelection(updated.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        m_binding.bottomSheethashtagSendIv.setOnClickListener {
            m_adapter.add_hashtag(Hashtag(" # ${m_binding.bottomSheethashtagHashtagFieldTiet.text.toString()}"))
            m_binding.bottomSheethashtagHashtagFieldTiet.setText("")

        }
    }

    companion object{
        fun newInstantce(hashtags : ArrayList<Hashtag>) : HashtagBottomSheet{
            var bottomSheet=HashtagBottomSheet()
            bottomSheet.m_hashtags = hashtags
            return bottomSheet
        }
    }



}
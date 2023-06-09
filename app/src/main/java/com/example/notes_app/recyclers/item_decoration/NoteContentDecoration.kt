package com.example.notes_app.recyclers.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.recyclers.adapter.NoteContentAdapter

class NoteContentDecoration: RecyclerView.ItemDecoration {
    private var m_context : Context

    constructor(context: Context ){
        this.m_context = context
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if(state.itemCount == 0){
            outRect.left = dpToPx(30f)
            outRect.right = dpToPx(30f)
            outRect.top   = dpToPx(10f)
            outRect.bottom = dpToPx(10f)
        }
        else {
            outRect.left = dpToPx(10f)
            outRect.right = dpToPx(10f)
            outRect.top   = dpToPx(5f)
            outRect.bottom = dpToPx(5f)
        }





        }

    private fun dpToPx(dp : Float):Int{
        return (dp * m_context.resources.displayMetrics.density).toInt()
    }

}
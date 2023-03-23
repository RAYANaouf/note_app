package com.example.notes_app.recyclers.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class NoteDecoration : RecyclerView.ItemDecoration {

    private var m_b_margin : Float
    private var m_r_margin : Float
    private var m_l_margin : Float
    private var m_context : Context

    constructor(context : Context, b_margin : Float, l_r_margin : Float ){

        this.m_b_margin = b_margin
        this.m_l_margin = l_r_margin
        this.m_r_margin = l_r_margin

        this.m_context = context
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        var position = parent.getChildAdapterPosition(view)
        var count = parent.adapter?.itemCount ?: 0

        var layoutParam = view.layoutParams
        if (layoutParam is StaggeredGridLayoutManager.LayoutParams){
            outRect.bottom = dpToPx(m_b_margin)

            if (layoutParam.spanIndex % 2 == 0){
                outRect.right = dpToPx(m_r_margin)
            }
            else{

            }

        }


    }

    private fun dpToPx(dp : Float):Int {

        var px = dp * m_context.resources.displayMetrics.density

        return px.toInt()
    }
}
package com.example.notes_app.recyclers.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CategoryDecoration : RecyclerView.ItemDecoration {

    private var m_context : Context
    private var m_margin : Float

    constructor(context: Context , margin : Float){
        this.m_context = context
        this.m_margin = margin
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        var position = parent.getChildAdapterPosition(view)
        var item_count = parent.adapter?.itemCount ?: 0

        outRect.top = dpToPx(m_margin)
        outRect.right = dpToPx(m_margin)
        outRect.left = dpToPx(m_margin)

    }

    private fun dpToPx(dp : Float) : Int{
        var px = (dp * m_context.resources.displayMetrics.density)
        return px.toInt()
    }



}
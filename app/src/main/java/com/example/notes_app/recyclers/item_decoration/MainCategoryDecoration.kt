package com.example.notes_app.recyclers.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MainCategoryDecoration : RecyclerView.ItemDecoration {

    private var m_context : Context
    private var m_l_r_margin : Float
    private var m_t_b_margin : Float

    constructor(context: Context, l_r_margin : Float, t_b_margin : Float){
        this.m_context = context
        this.m_l_r_margin = l_r_margin
        this.m_t_b_margin = t_b_margin
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
        outRect.left = dpToPx(m_l_r_margin)
        outRect.top = dpToPx(m_t_b_margin)
        outRect.bottom = dpToPx(m_t_b_margin)

        if (position == item_count-1){
            outRect.right = dpToPx(m_l_r_margin)
        }

    }

    private fun dpToPx(dp : Float) : Int{
        var px = (dp * m_context.resources.displayMetrics.density)
        return px.toInt()
    }
}
package com.example.notes_app.recyclers.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HashtagDicoration : RecyclerView.ItemDecoration {

    private var m_context : Context
    private var m_r_margin : Float
    private var m_l_margin : Float
    private var m_t_margin : Float
    private var m_b_margin : Float

    constructor(context: Context , l_margin : Float , r_margin : Float , t_margin : Float , b_margin : Float){
        this.m_context = context
        this.m_r_margin = r_margin
        this.m_l_margin = l_margin
        this.m_b_margin = b_margin
        this.m_t_margin = t_margin
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = pxTodp(m_t_margin).toInt()
        outRect.bottom = pxTodp(m_b_margin).toInt()
        outRect.right = pxTodp(m_r_margin).toInt()
        outRect.left = pxTodp(m_l_margin).toInt()



    }

    private fun pxTodp( x : Float) : Float{

        var X = m_context.resources.displayMetrics.density * x

        return X
    }

}
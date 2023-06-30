package com.dragonguard.android.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//리사이클러뷰 수직 마진
class VerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {

    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }
}
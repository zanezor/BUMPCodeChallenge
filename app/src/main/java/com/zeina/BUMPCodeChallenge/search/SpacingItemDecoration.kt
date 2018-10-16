package com.zeina.BUMPCodeChallenge.search

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacingItemDecoration(val itemOffset:Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            bottom = itemOffset
            top = itemOffset
            left = itemOffset
            right = itemOffset
        }
    }
}
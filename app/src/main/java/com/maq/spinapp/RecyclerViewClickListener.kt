package com.maq.spinapp

import android.view.View
import com.maq.spinapp.data.Item


interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(view: View, item: Item)
}
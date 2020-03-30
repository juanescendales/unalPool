package com.example.unalpool.ViewModels

import com.example.unalpool.R
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder


class MyTripItem : Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.trip_row_mytrip
    }
}
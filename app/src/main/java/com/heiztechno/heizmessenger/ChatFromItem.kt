package com.heiztechno.heizmessenger

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatFromItem: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
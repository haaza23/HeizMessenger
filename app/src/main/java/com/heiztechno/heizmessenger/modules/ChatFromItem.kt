package com.heiztechno.heizmessenger.modules

import com.heiztechno.heizmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtMessageDst.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
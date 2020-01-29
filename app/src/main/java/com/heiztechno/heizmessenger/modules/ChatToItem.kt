package com.heiztechno.heizmessenger.modules

import com.heiztechno.heizmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtMessageSelf.text = text
     }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
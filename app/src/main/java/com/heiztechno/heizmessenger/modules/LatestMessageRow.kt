package com.heiztechno.heizmessenger.modules

import com.heiztechno.heizmessenger.ChatMessage
import com.heiztechno.heizmessenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtMessageChat.text = chatMessage.text
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}
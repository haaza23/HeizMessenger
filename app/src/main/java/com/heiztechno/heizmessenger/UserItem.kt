package com.heiztechno.heizmessenger

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UserItem: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
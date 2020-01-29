package com.heiztechno.heizmessenger.modules

import com.heiztechno.heizmessenger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtUsername.text = user.username             //Setear usuario a usuario en el RecyclerView

        Picasso.get().load(user.profilePicture).into(viewHolder.itemView.imgPerfil)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message                //Que el recyclerView se llene con ese layout
    }
}
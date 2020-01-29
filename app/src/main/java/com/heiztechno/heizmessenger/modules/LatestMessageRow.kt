package com.heiztechno.heizmessenger.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.heiztechno.heizmessenger.ChatMessage
import com.heiztechno.heizmessenger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtMessageChat.text = chatMessage.text

        val chatMessageId: String
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid)
            chatMessageId = chatMessage.toId
        else
            chatMessageId = chatMessage.fromId

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatMessageId")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(User::class.java)

                    viewHolder.itemView.txtUserChat.text = chatPartnerUser?.username
                    Picasso.get().load(chatPartnerUser?.profilePicture).into(viewHolder.itemView.imgChat)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}
package com.heiztechno.heizmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)         //Obtengo el usuario del intent

        supportActionBar?.title = user.username                                                 //Pongo como titulo de la activity el usuario

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        rclChat.adapter = adapter
    }
}

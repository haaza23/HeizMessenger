package com.heiztechno.heizmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.heiztechno.heizmessenger.modules.ChatFromItem
import com.heiztechno.heizmessenger.modules.ChatToItem
import com.heiztechno.heizmessenger.modules.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

lateinit var btnSend: Button
lateinit var edtMensaje: EditText
val adapter = GroupAdapter<GroupieViewHolder>()

var toUser: User? = null

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        adapter.clear()
        btnSend = findViewById(R.id.btnEnviar)
        edtMensaje = findViewById(R.id.edtTexto)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)         //Obtengo el usuario del intent
        supportActionBar?.title = user.username                             //Pongo como titulo de la activity el usuario

        btnSend.setOnClickListener {
            performSendMessage()
        }

        printMessages()
        rclChat.adapter = adapter
    }

    private fun performSendMessage(){
        val position = rclChat.adapter!!.itemCount            //Para que vaya al ultimo mensaje

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if(fromId == null) return

        //Creo un nodo en la db "desde" cada usuario "hacia" cada usuario con los mensajes
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()   //Para que carguen los mensajes "recibidos"

        val text = edtMensaje.text.toString()
        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis()/1000)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                edtTexto.text.clear()
                rclChat.smoothScrollToPosition(position)            //Para que vaya al ultimo mensaje
                adapter.notifyDataSetChanged()
            }

        toReference.setValue(chatMessage)           //Cargo el mensaje como fromId en el usuario toId

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-message/$fromId/$toId")   //Solo cambia el texto del ultimo mensaje en la db
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-message/$toId/$fromId")   //Solo cambia el texto del ultimo mensaje en la db
        latestMessageToRef.setValue(chatMessage)



    }

    private fun printMessages(){

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        reference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if(chatMessage != null){
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(
                            ChatToItem(
                                chatMessage.text
                            )
                        )
                    } else{
                        adapter.add(
                            ChatFromItem(
                                chatMessage.text
                            )
                        )
                    }
                }
                rclChat.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {            //Es necesario declarar los metodos
                                                                                    //Aunque no se usen
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })

    }
}

package com.heiztechno.heizmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.heiztechno.heizmessenger.NewMessageActivity.Companion.USER_KEY
import com.heiztechno.heizmessenger.modules.LatestMessageRow
import com.heiztechno.heizmessenger.modules.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlin.collections.HashMap


class LatestMessagesActivity : AppCompatActivity() {

    companion object{
        var currentUser: User? = null
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessagesMap = HashMap<String, ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        rclLastMessages.adapter = adapter
        rclLastMessages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val fab: View = findViewById(R.id.btnFab)           //Floating button (es NECESARIO la declaracion en el gradle de androidX
        fab.setOnClickListener{

            /*Snackbar.make(it, "Here's a Snackbar", Snackbar.LENGTH_LONG)    NOTIFICACION ALTERNATIVA A TOAST
                .setAction("Action", null)
                .show()
            */

            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }

        addClickUser()
        setupLatestMessages()
        fetchCurrentUser()
        verifyUserLoggedIn()
    }

    private fun addClickUser(){

        adapter.setOnItemClickListener { item, view ->
            Log.d("LatestMsg", "Pasa por aca")
            val intent = Intent(view.context, ChatActivity::class.java)

            val row = item as LatestMessageRow

            intent.putExtra(USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }
    }

    private fun setupLatestMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-message/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerView()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {    //refresca la pantalla cuando recibe un msj nuevo
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerView()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private fun refreshRecyclerView(){
        //No es la solucion mas eficaz ya que cte esta borrando y pidiendo los datos de la db
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun verifyUserLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {       //Verifica que haya un usuario logeado o vuelve al login
        when(item?.itemId){
            R.id.btnLogOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {            //Pone el "Cerrar sesion" en la parte superior
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

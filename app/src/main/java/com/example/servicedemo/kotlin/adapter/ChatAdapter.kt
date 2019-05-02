package com.example.servicedemo.kotlin.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.servicedemo.R
import com.example.servicedemo.kotlin.`class`.CustomMessage
import com.example.servicedemo.kotlin.inflate
import kotlinx.android.synthetic.main.item_received_message.view.*
import kotlinx.android.synthetic.main.item_sent_message.view.*

class ChatAdapter(private val listMessage:List<CustomMessage>, private val listener:ChatListener, private val myUserName:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT_MESSAGE = 1
    private val VIEW_TYPE_RECEIVED_MESSAGE = 2

    override fun getItemViewType(position: Int): Int {
        return if (listMessage[position].UserName.equals(myUserName)) VIEW_TYPE_SENT_MESSAGE else VIEW_TYPE_RECEIVED_MESSAGE
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_SENT_MESSAGE -> SentMessageViewHolder(parent.inflate(R.layout.item_sent_message))
            VIEW_TYPE_RECEIVED_MESSAGE -> ReceivedMessageViewHolder(parent.inflate(R.layout.item_received_message))
            else -> SentMessageViewHolder(parent.inflate(R.layout.item_sent_message))
        }
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = listMessage[position]
        if (holder is SentMessageViewHolder){
            holder.itemView.tvSentMessage.text = message.Message
            holder.itemView.tvSentTime.text = System.currentTimeMillis().toString()
            holder.itemView.setOnClickListener { listener.onItemClick(position) }
        }else if(holder is ReceivedMessageViewHolder){
            holder.itemView.tvReceivedMessage.text = message.Message
            holder.itemView.tvReceiveTime.text = System.currentTimeMillis().toString()
            holder.itemView.setOnClickListener { listener.onItemClick(position) }
        }
    }


    interface ChatListener{
        fun onItemClick(position:Int)
    }

    inner class SentMessageViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ReceivedMessageViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}
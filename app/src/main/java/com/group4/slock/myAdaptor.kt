package com.group4.slock

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MyAdaptor(private val devicelist: ArrayList<Array<String>> ): RecyclerView.Adapter<MyAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.card_view_design, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myBuff: Array<String> = devicelist[position]
        holder.textView.text = getText(myBuff[0])
        holder.textView1.text = myBuff[1]
        holder.textView.setOnClickListener {
            //TODO:Idher ka code figure out karna hai
        }
    }

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice){
        //Yeh code chal nahi raha
        val id: UUID = device.uuids?.get(0)!!.uuid
        val bts = device.createRfcommSocketToServiceRecord(id)
        bts?.connect()
    }

    private fun getText(s: String?): String{
        if (s == null){
            return "Unknown"
        }else{
            return s
        }
    }

    override fun getItemCount(): Int {
        return devicelist.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textView: TextView = itemView.findViewById(R.id.devname)
        var textView1: TextView = itemView.findViewById(R.id.address)
    }

}


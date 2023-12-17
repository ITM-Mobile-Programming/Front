package com.hwido.pieceofdayfront.BluetoothClient.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hwido.pieceofdayfront.R


@SuppressLint("MissingPermission")
class PairingAdapter(var List : MutableList<BluetoothDevice>, val context : Context ) :
    RecyclerView.Adapter<PairingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_device,parent,false)
        return ViewHolder(v)
    }


    interface ItemClick{
        fun onClick(view:View, position: Int)
    }

    var itemClick : ItemClick? = null


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(itemClick != null){
            holder?.itemView!!.setOnClickListener { v->
                itemClick!!.onClick(v,position)
            }
        }
        holder.bindItems(List[position])
    }



    override fun getItemCount(): Int {
        return List.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(item : BluetoothDevice){
            val device_name = itemView.findViewById<TextView>(R.id.tvName)
            val device_address = itemView.findViewById<TextView>(R.id.tvMacAddress)

            device_name.text = item.name
            device_address.text = item.address


        }
    }

    //업데이트 부분 추가
    fun updateData(lottoList : MutableList<BluetoothDevice>) {
        List  = lottoList
        notifyDataSetChanged()
    }


}
package com.hwido.pieceofdayfront.Bluetooth.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hwido.pieceofdayfront.Bluetooth.activity.ScanActivity
import com.hwido.pieceofdayfront.R


@SuppressLint("MissingPermission")
class DeviceAdapter(
    private var pairedList: ArrayList<BluetoothDevice>,
    private var onConnectListener: ScanActivity.OnConnectListener
) :
    RecyclerView.Adapter<DeviceAdapter.DeviceItemViewHolder>() {

    class DeviceItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)

        return DeviceItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceItemViewHolder, position: Int) {
        val device = pairedList[position]

        holder.view.findViewById<TextView>(R.id.tvName).text = device.name
        holder.view.findViewById<TextView>(R.id.tvMacAddress).text = device.address

        //누르면 connectToServer를 한다
        holder.view.setOnClickListener {
            onConnectListener.connectToServer(device)
        }
    }

    override fun getItemCount(): Int = pairedList.size






}

package com.hwido.pieceofdayfront.Bluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.widget.Toast
import com.hwido.pieceofdayfront.Bluetooth.net.BTConstant
import com.hwido.pieceofdayfront.Bluetooth.net.BluetoothClient
import com.hwido.pieceofdayfront.Bluetooth.net.BluetoothServer


@SuppressLint("MissingPermission")
enum class AppController {
    Instance;

    lateinit var mainActivity: Activity

    private val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    lateinit var bluetoothClient: BluetoothClient
    lateinit var bluetoothServer: BluetoothServer

    fun init(activity: Activity, btClient: BluetoothClient, btServer: BluetoothServer) {
        mainActivity = activity
        bluetoothClient = btClient
        bluetoothServer = btServer

        bluetoothOn()
    }

    fun bluetoothOn() {
        if (btAdapter == null) {
            Toast.makeText(mainActivity, "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show()
        } else {
            if (btAdapter.isEnabled) {
                //Toast.makeText(mainActivity, "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(mainActivity, "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show()
                val intentBluetoothEnable = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                mainActivity.startActivityForResult(
                    intentBluetoothEnable,
                    BTConstant.BT_REQUEST_ENABLE
                )
            }
        }
    }

    fun bluetoothOff() {
        if (btAdapter.isEnabled) {
            btAdapter.disable()
            Toast.makeText(mainActivity, "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            //Toast.makeText(mainActivity, "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }


}

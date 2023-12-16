package com.hwido.pieceofdayfront.Bluetooth.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hwido.pieceofdayfront.Bluetooth.AppController
import com.hwido.pieceofdayfront.Bluetooth.adapter.DeviceAdapter
import com.hwido.pieceofdayfront.Bluetooth.adapter.PairingAdapter

import com.hwido.pieceofdayfront.Bluetooth.net.BluetoothClient
import com.hwido.pieceofdayfront.R


@SuppressLint("MissingPermission")
class ScanActivity : AppCompatActivity() {

    private var rvDevices: RecyclerView? = null
    private lateinit var btClient: BluetoothClient
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var strongestDevice: BluetoothDevice? = null

    //새로추가
    private val foundDevices = mutableListOf<BluetoothDevice>()
    private lateinit var rvAdapter: PairingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        btClient = AppController.Instance.bluetoothClient

        rvDevices = findViewById(R.id.pairedDevices)
        rvDevices?.let {
            it.setHasFixedSize(true)
            //
            it.layoutManager = LinearLayoutManager(AppController.Instance.mainActivity)
            //paired dev
            rvDevices?.adapter = DeviceAdapter(btClient.getPairedDevices(), onConnectListener)
            // 스캔하는부분 추가필요
            // 들어가서 스캔하는 작업이 필요합니다 찾기랑 연결 부분 구형
        }



        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        val filterPairing = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(pairingReceiver, filterPairing)

        val scanButton = findViewById<Button>(R.id.btnScan2)

        ///하면 scanButton을 누르면 scan 시갖
        //시작하면 연결 item view

        //서치 성공
        scanButton.setOnClickListener {
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            bluetoothAdapter.startDiscovery()
            foundDevices.clear()

            rvAdapter.notifyDataSetChanged()

            Handler(Looper.getMainLooper()).postDelayed({
                //여기서 기기탐색이 안되니깐 팅기는거다
                val strongestDevicePair = foundDevicesWithRssi.maxBy { it.second }
                strongestDevice = strongestDevicePair?.first

                strongestDevice?.let {
                    // 가장 신호가 강한 디바이스 처리
                    Log.d("BluetoothDevice", "Strongest Device: ${it.name}, RSSI: ${strongestDevicePair.second}")
//                    onConnectListener.connectToServer(strongestDevice)

                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        vibrator.vibrate(300)
                    }

                    //다이아 로그 띄우고 일단 확인 가능하게 한다 yes 하면은 연결되게 한다 아니면 취소 하고
                    AlertDialog.Builder(this)
                        .setMessage(
                            "Strongest Device: ${it.name}, ${it.address}it is right??"
                        )
                        //세팅으로 간다
                        .setPositiveButton("Connect to Device") { _, _ ->
                            try {
                                it.createBond()
//                                onConnectListener.connectToServer(strongestDevice)
                            } catch (e: ActivityNotFoundException) {
                                e.printStackTrace()
                            }
                        }
                        .setNegativeButton("CANCEL") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }, 7000)

        }
        //bonding 관리



        val recyclerView = findViewById<RecyclerView>(R.id.searchDevices)
        rvAdapter = PairingAdapter(foundDevices,baseContext)
        recyclerView.adapter = rvAdapter

        rvAdapter.itemClick = object: PairingAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
//                pairDevice(foundDevices[position])
//                onConnectListener.connectToServer(foundDevices[position])

            }

        }

        recyclerView.layoutManager = LinearLayoutManager(this)
    }



    private var foundDevicesWithRssi = mutableListOf<Pair<BluetoothDevice, Int>>()


    // Create a BroadcastReceiver for ACTION_FOUND.
    // start discovery하면 계속 콜이된다
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    //블루 투스 연결
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let { foundDevices.add(it) }
//                    Log.d("ITM","${foundDevices.toString()}")

                    rvAdapter.updateData(foundDevices)

                    rvAdapter.notifyDataSetChanged()
                    //여기서 업데이트 한다
                    //rssi 결정
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    foundDevicesWithRssi.add(Pair(device, rssi) as Pair<BluetoothDevice, Int>)

                    // RSSI 값 처리
                    Log.d("BluetoothDevice", "Device: " + device!!.getName() + " RSSI: " + rssi);
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                }


            }
        }
    }

//안넘어감
    private val pairingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("ITM","pairing")
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == intent.action) {

                // 이거 괜찮나?
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                Log.d("ITM","pairing check ${device.toString()}")
                when (device?.bondState) {

                    BluetoothDevice.BOND_BONDED -> {
                        // 페어링 완료 처리 // 왜 일치하지 않는지 bound까지는 되는데
                        if (device?.address == strongestDevice?.address) {
                            // 페어링 완료된 장치가 strongestDevice와 동일하면 연결 시도
                            onConnectListener.connectToServer(device)
                        }else{
                            Log.d("ITM","연결까지는 실패")
                        }
                    }

                    BluetoothDevice.BOND_BONDING -> {
                        // 페어링 진행 중 처리
                    }
                    BluetoothDevice.BOND_NONE -> {
                        // 페어링 해제 처리
                    }
                }
            }
        }
    }

//    //목록에서 가져온다
//    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
//        if(bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
////            _isConnected.update { isConnected }
//            onConnectListener.connectToServer(bluetoothDevice)
//        } else {
//            CoroutineScope(Dispatchers.IO).launch {
//                Log.d("ITM","강한 세기의 기기가 아닙니다")
//            }
//        }
//    }


    interface OnConnectListener {
        fun connectToServer(device: BluetoothDevice)
    }

    private val onConnectListener: OnConnectListener = object : OnConnectListener {
        override fun connectToServer(device: BluetoothDevice) {
            btClient.connectToServer(device)
            finish()
        }
    }

    companion object {
        fun startForResult(context: Activity, requestCode: Int) =
            context.startActivityForResult(
                Intent(context, ScanActivity::class.java), requestCode
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
        unregisterReceiver(pairingReceiver)
    }

}
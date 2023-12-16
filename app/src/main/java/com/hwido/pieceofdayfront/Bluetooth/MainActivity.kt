package com.hwido.pieceofdayfront.Bluetooth


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hwido.pieceofdayfront.Bluetooth.activity.ScanActivity
import com.hwido.pieceofdayfront.Bluetooth.net.BTConstant.BT_REQUEST_ENABLE
import com.hwido.pieceofdayfront.Bluetooth.net.BluetoothClient
import com.hwido.pieceofdayfront.Bluetooth.net.BluetoothServer
import com.hwido.pieceofdayfront.Bluetooth.net.SocketListener
import com.hwido.pieceofdayfront.R

import java.util.*


class MainActivity : AppCompatActivity() {

    private var sbLog = StringBuilder()
    private var btClient: BluetoothClient = BluetoothClient()
    private var btServer: BluetoothServer = BluetoothServer()

    private lateinit var svLogView: ScrollView
    private lateinit var tvLogView: TextView
    private lateinit var etMessage: EditText

    private var handler: Handler = Handler()


    /** 위치 권한 SDK 버전 29 이상**/
    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissionsUpApi29Impl = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
    )

    /** 위치 권한 SDK 버전 29 이하**/
    @RequiresApi(Build.VERSION_CODES.M)
    private val permissionsDownApi29Impl = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.WRITE_CONTACTS
    )
    // 권한 요청에 대한 콜백 처리
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 권한 요청 결과 처리
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                // 모든 권한이 승인된 경우
                Toast.makeText(this, "permission 성공",Toast.LENGTH_SHORT).show()
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "permission 실패",Toast.LENGTH_SHORT).show()
            }
        }

//    Toast.makeText(this, "permission 성공",Toast.LENGTH_SHORT).show()
//
//} else {
//    showRotationalDialogForPermission()

    private fun requestPermissionsBasedOnVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12(API 레벨 31) 이상인 경우
            requestMultiplePermissions.launch(permissionsUpApi29Impl)
        } else {
            // Android 12 미만인 경우
            requestMultiplePermissions.launch(permissionsDownApi29Impl)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionsBasedOnVersion()
//        requestPermission()


        AppController.Instance.init(this, btClient,btServer)

        initUI()
        setListener()

        btClient.setOnSocketListener(mOnSocketListener)

        //서버
        btServer.setOnSocketListener(mOnSocketListener)
        btServer.accept()
    }

    // 여기서 아마 UI 가 겹칠거임
    private fun initUI() {
        svLogView = findViewById(R.id.svLogView) //데이터 전송
        tvLogView = findViewById(R.id.tvLogView) // 스크롤뷰 안에 textView 있다
        etMessage = findViewById(R.id.etMessage)// 메시지 입력 edit
    }

    private fun setListener() {
        //이 부분 어떻게 할 건지 결정해야한다
        findViewById<Button>(R.id.btnAccept).setOnClickListener {
            btServer.accept()
        }

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            btServer.stop()
        }


        findViewById<Button>(R.id.btnSendData).setOnClickListener {
            if (etMessage.text.toString().isNotEmpty()) {
                btClient.sendData(etMessage.text.toString())
            }
        }

        findViewById<Button>(R.id.PairedDevicesCheck).setOnClickListener {
            ScanActivity.startForResult(this, 102)
        }

        findViewById<Button>(R.id.btnDisconnect).setOnClickListener {
            btClient.disconnectFromServer()
        }

    }

    // 텍스트 뷰에 post 한다
    private fun log(message: String) {
        sbLog.append(message.trimIndent() + "\n")
        handler.post {
            tvLogView.text = sbLog.toString()
            svLogView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private val mOnSocketListener: SocketListener = object : SocketListener {
        override fun onConnect() {
            //연결 됬을
            log("Connect!\n")
        }

        override fun onDisconnect() {
            log("Disconnect!\n")
        }

        override fun onError(e: Exception?) {
            e?.let { log(e.toString() + "\n") }
        }

        override fun onReceive(msg: String?) {

            msg?.let { log("Receive : $it\n") }
        }

        override fun onSend(msg: String?) {
            msg?.let { log("Send : $it\n") }
        }

        override fun onLogPrint(msg: String?) {
            msg?.let { log("$it\n") }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            BT_REQUEST_ENABLE -> if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(applicationContext, "블루투스 활성화", Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(applicationContext, "취소", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



//    private fun requestPermission() {
//
//        if (Build.VERSION.SDK_INT >= 29) {
//            Log.d("ITM","29이상권한요청")
//            // PermissionSupport.java 클래스 객체 생성
//            requestMultiplePermissions.launch(
//                permissionsUpApi29Impl
//            )
//        }else {
//            Log.d("ITM", "29이하권한요청")
//            var shouldRequestPermissions = false
//            for (permission in permissionsDownApi29Impl) {
//                val chk = checkCallingOrSelfPermission(permission)
//                if (chk == PackageManager.PERMISSION_DENIED) {
//                    Log.d("ITM", "29이하권한요청 체크중")
//                    shouldRequestPermissions = true
//                    break
//                }
//            }
//            if (shouldRequestPermissions) {
//                Log.d("ITM", "29이하권한요청 하러감")
//                requestPermissions(permissionsDownApi29Impl, 0)
//            }
//
//        }
//    }







//    //permission 해결 필요
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 0) {
//            Log.d("ITM", "29이하권한 수락 진행중 ")
//            for (element in grantResults) {
//                if (element == PackageManager.PERMISSION_GRANTED) {
//                } else {
//                    TedPermission.create()
//                        .setPermissionListener(object : PermissionListener {
//                            override fun onPermissionGranted() {
//                                Log.d("ITM", "29이하권한 가지고 있다면 ")
//                                //업으면 다이어로그 띄운다
//                            }
//
//                            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                                Log.d("ITM", "29이하권한 거절 ")
//                            }
//                        })
//                        .setDeniedMessage("You have permission to set up.")
//                        .setPermissions(
//                            Manifest.permission.BLUETOOTH,
//                            Manifest.permission.BLUETOOTH_ADMIN,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        )
//                        .setGotoSettingButton(true)
//                        .check();
//                }
//            }
//        }
//    }



    override fun onDestroy() {
        super.onDestroy()
        AppController.Instance.bluetoothOff()
    }



}
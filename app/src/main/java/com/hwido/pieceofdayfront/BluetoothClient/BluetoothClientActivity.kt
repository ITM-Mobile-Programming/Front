package com.hwido.pieceofdayfront.BluetoothClient

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hwido.pieceofdayfront.BluetoothClient.activity.ScanActivity
import com.hwido.pieceofdayfront.BluetoothClient.net.BTConstantServer
import com.hwido.pieceofdayfront.BluetoothClient.net.BluetoothClient
import com.hwido.pieceofdayfront.BluetoothClient.net.SocketListener
import com.hwido.pieceofdayfront.DT.WriteDataRequestTransfer
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.ActivityBluetoothClientBinding

import com.hwido.pieceofdayfront.login.LoginMainpage

class BluetoothClientActivity : AppCompatActivity() {
    private val  SpringServerCall= SpringServerAPI()
    private var BluetoothFreindCode = ""
    private lateinit var binding : ActivityBluetoothClientBinding
    private var BluetoothMYCode = ""
    private var sbLog = StringBuilder()
    private var btClient: BluetoothClient = BluetoothClient()

    private var Share :String = ""
    private lateinit var svLogView: ScrollView
    private lateinit var tvLogView: TextView
    private lateinit var etMessage: EditText

    private var handler: Handler = Handler()


    val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            applicationContext,
            LoginMainpage.FILE_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

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
                Toast.makeText(this, "permission 성공", Toast.LENGTH_SHORT).show()
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "permission 실패", Toast.LENGTH_SHORT).show()
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
        binding = ActivityBluetoothClientBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val accessToken =
            sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

//        val accessToken =
//            sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

        requestPermissionsBasedOnVersion()

        AppControllerClient.Instance.init(this, btClient)

        initUI()
        setListener()

        btClient.setOnSocketListener(mOnSocketListener)
        //서버

//        btServer.accept()

        //버튼누르면 permission 체크하고
        //"codeAndContent", transferData
//        val transferData =
//            WriteDataRequestTransfer(longToInt, mycode, title, content, location, weather)

        val share = intent.getSerializableExtra("codeAndContent") as WriteDataRequestTransfer

        Share = share.toString()
        //코드를 다른 사용자한테 보낸다 보내고 친구 확인하고
        //확인 후에 친구 추가

        Log.d("ITM", "공유전 데이터 ${share?.code}, ${share?.title}") // 본인 코드가 아니라 친구 코드여야한다


//        sharedData.code
        // 공유 데이터가 온다면 code 받아서 확인하고 확인되면


        // 공유데이터을 기반으로 받은데이터를 전송한다
        // 이건 친구 코드 받아오면ㅋ 콜백으로 처리해야 된다
//        SpringServerCall.checkIfFriend(accessToken, BluetoothFreindCode)

        //시작하면 다이어로그 보여준다


    }

    // 여기서 아마 UI 가 겹칠거임
    private fun initUI() {
        svLogView = binding.svLogView //데이터 전송
        tvLogView = binding.tvLogView // 스크롤뷰 안에 textView 있다
        etMessage = binding.etMessage// 메시지 입력 edit
    }

    private fun setListener() {
        //이 부분 어떻게 할 건지 결정해야한다


        //버튼을 누르면 보낸다는 생각인데
        binding.btnSendData.setOnClickListener {
//            if (etMessage.text.toString().isNotEmpty()) {
//                btClient.sendData(etMessage.text.toString())
//            }

            btClient.sendData(Share)//코드를 보낸다
        }

        binding.PairedDevicesCheck.setOnClickListener {
            ScanActivity.startForResult(this, 102)
        }

        binding.btnDisconnect.setOnClickListener {
            btClient.disconnectFromServer()
        }

    }

    // 텍스트 뷰에 post 한다
    // 곧있으면 날릴예정
    private fun log(message: String) {
        sbLog.append(message.trimIndent() + "\n")
        handler.post {
            tvLogView.text = sbLog.toString()
            svLogView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private val mOnSocketListener: SocketListener = object : SocketListener {
        override fun onConnect() {
            log("Connect!\n")
            Log.d("ITM", "블루투스 연결완료")
            //바로 연결 됬을떄 내 코드를 친구 한테 보내 준다
        }

        override fun onDisconnect() {
            log("Disconnect!\n")
        }

        override fun onError(e: Exception?) {
            e?.let { log(e.toString() + "\n") }
        }

        override fun onReceive(msg: String?) {
            //바로 연결 됬을떄 내코드를 친구 한테 보내준다 친구한테 받은 코드로
//            SpringServerCall.checkIfFriend(accessToken, BluetoothFreindCode)
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
            BTConstantServer.BT_REQUEST_ENABLE -> if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(applicationContext, "블루투스 활성화", Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(applicationContext, "취소", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppControllerServer.Instance.bluetoothOff()
    }



}
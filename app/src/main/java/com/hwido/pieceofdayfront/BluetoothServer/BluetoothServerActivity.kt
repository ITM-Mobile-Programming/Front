package com.hwido.pieceofdayfront.BluetoothServer

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.hwido.pieceofdayfront.BluetoothClient.AppControllerServer
import com.hwido.pieceofdayfront.BluetoothClient.net.BTConstantServer
import com.hwido.pieceofdayfront.BluetoothClient.net.BluetoothServer
import com.hwido.pieceofdayfront.BluetoothClient.net.SocketListenerServer
import com.hwido.pieceofdayfront.DT.WriteDataRequestTransfer
import com.hwido.pieceofdayfront.MainMainpage
import com.hwido.pieceofdayfront.R
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.ActivityBluetoothServerBinding
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageContentBinding
import com.hwido.pieceofdayfront.login.LoginMainpage

class BluetoothServerActivity : AppCompatActivity() {
//    private val  SpringServerCall= SpringServerAPI()
//    private var BluetoothFreindCode = ""
    private var sbLog = StringBuilder()
    private var btServer: BluetoothServer = BluetoothServer()
    private var accessToken :String = ""

    private var data : WriteDataRequestTransfer? = null

    private val  SpringServerCall= SpringServerAPI()
    private lateinit var binding : ActivityBluetoothServerBinding
//    private lateinit var svLogView: ScrollView
//    private lateinit var tvLogView: TextView
//    private lateinit var etMessage: EditText

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
        binding = ActivityBluetoothServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        setListener()


        accessToken =
            sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

//        val accessToken =
//            sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()

        requestPermissionsBasedOnVersion()
//        requestPermission()


        AppControllerServer.Instance.init(this, btServer)

        //서버
        btServer.setOnSocketListener(mOnSocketListener)
        btServer.accept()

        binding.mainDiarysharepageContentBtn.setOnClickListener {

            //이어쓰기 완료하면 보내준다
            val friendcode =data?.code;
            val content = binding.shareContent.text.toString();
            val diaryId = data?.diaryId
            //함수 구현 해야된다
            //call back은 Unit으로 설정하고 3번째 intent로 가는 걸로 한다
            //이어쓰기 완료 인터페이즈 구현
            //그리고 누르면 바로 프레그먼트 3번쨰 페이지로 넘어가야한다

            Log.d("ITTTTM","이건가? $diaryId")
            SpringServerCall.relayWrite(accessToken, diaryId!!, friendcode!!, content,   onSuccess = {->
                Log.d("ITTTTM","이건가? $diaryId")
                // 성공 시 실행될 코드
//                Log.d("ITM", "리스트 콜백 ${diaryList.reversed()}")
//                val intent =
                val intent = Intent(this, MainMainpage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("FRAGMENT_NAME", "MainShareFragment")
                startActivity(intent)

            }, onFailure = {
                // 실패 시 실행될 코드
                Toast.makeText(this, "NONO", Toast.LENGTH_SHORT).show()
            })


        }
        //버튼누르면 permission 체크하고
        //"codeAndContent", transferData
//        val transferData =
//            WriteDataRequestTransfer(longToInt, mycode, title, content, location, weather)
//
//        val sharedData = intent.getSerializableExtra("codeAndContent") as WriteDataRequestTransfer
//
//        //코드를 다른 사용자한테 보낸다 보내고 친구 확인하고
//        //확인 후에 친구 추가
//
//        Log.d("ITM", "공유전 데이터 ${sharedData.code}, ${sharedData.title}") // 본인 코드가 아니라 친구 코드여야한다

//        sharedData.code
        // 공유 데이터가 온다면 code 받아서 확인하고 확인되면


        // 공유데이터을 기반으로 받은데이터를 전송한다
        // 이건 친구 코드 받아오면ㅋ 콜백으로 처리해야 된다
//        SpringServerCall.checkIfFriend(accessToken, BluetoothFreindCode)
        //시작하면 다이어로그 보여준다
    }

    // 여기서 아마 UI 가 겹칠거임
    private fun initUI() {
//        svLogView = binding.svLogViewServer //데이터 전송
//        tvLogView = binding.tvLogViewServer // 스크롤뷰 안에 textView 있다
//        etMessage = binding.etMessageServer // 메시지 입력 edit
    }

    private fun setListener() {
        binding.btnAcceptServer.setOnClickListener {
            btServer.accept()
        }

        binding.btnStopServer.setOnClickListener {
            btServer.stop()
        }


//        binding.btnSendDataServer.setOnClickListener {
////            if (etMessage.text.toString().isNotEmpty()) {
////                btServer.sendData(etMessage.text.toString())
////            }
//        }

    }

    // 텍스트 뷰에 post 한다
    // 곧있으면 날릴예정
//    private fun log(message: String) {
//        sbLog.append(message.trimIndent() + "\n")
//        handler.post {
//            tvLogView.text = sbLog.toString()
//            svLogView.fullScroll(ScrollView.FOCUS_DOWN)
//        }
//    }

    private val mOnSocketListener: SocketListenerServer = object : SocketListenerServer {
        override fun onConnect() {
//            log("Connect!\n")
            //바로 연결 됬을떄 내 코드를 친구 한테 보내 준다
        }

        override fun onDisconnect() {
//            log("Disconnect!\n")
        }

        override fun onError(e: Exception?) {
//            e?.let { log(e.toString() + "\n") }
        }

        override fun onReceive(msg: String?) {
            //바로 연결 됬을떄 내코드를 친구 한테 보내준다 친구한테 받은 코드로
//            SpringServerCall.checkIfFriend(accessToken, BluetoothFreindCode)
//            msg?.let { log("Receive : $it\n") }


            //            springServer.getDiaryList(it1,  onSuccess = { diaryList ->
//                // 성공 시 실행될 코드
//                Log.d("ITM", "리스트 콜백 ${diaryList.reversed()}")
//                diaryAdapter.updateData(diaryList.reversed())
//            }, onFailure = {
//                // 실패 시 실행될 코드
//                Toast.makeText(activity, "NONO", Toast.LENGTH_SHORT).show()
//            })

            //
            Log.d("ITTTTM", "데이터 파싱전 ${msg}")
            val gson = Gson()
            data = gson.fromJson(msg, WriteDataRequestTransfer::class.java)
            Log.d("ITTTTM", "데이터 파싱 후${data?.diaryId}")
            var friendCode = ""
            if (data != null) {
                // 캐스팅 성공, data 사용
                friendCode = data!!.code
                Log.d("ITM", "${friendCode}")

                //친구체크
                SpringServerCall.checkIfFriend(accessToken, friendCode, onSuccess = { CheckNumber ->
                    // 성공 시 실행될 코드
//                    binding.shareContent.setText(share!!.context)
//                    binding.shareTitle.setText(share!!.title)
                    //친구가 아니면 다이어로그 띄워서 친구 추가 메세지 구현
                    if(CheckNumber ==1){
                        //다이어로그로 친구 추가할지 선택한다
                        AlertDialog.Builder(this@BluetoothServerActivity)
                            .setMessage(
                               "Do you want add this person to Friend?"
                            )
                            //세팅으로 간다
                            .setPositiveButton("Add Friend") { _, _ ->
                                SpringServerCall.AddFriend(accessToken, friendCode)
                            }
                            //_-> 이게 뭐야
                            .setNegativeButton("Keep Going") { dialog, _ ->

                                dialog.dismiss()
                            }.show()
                    }

                    binding.shareTitle.text =data?.title
                    binding.shareContent.setText(data?.context)

                }, onFailure = {

                })

                // 이어쓰기 파트 진행


            } else {
                // 캐스팅 실패, 적절한 처리 수행

            }


        }

        override fun onSend(msg: String?) {
//            msg?.let { log("Send : $it\n") }
        }

        override fun onLogPrint(msg: String?) {
//            msg?.let { log("$it\n") }
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
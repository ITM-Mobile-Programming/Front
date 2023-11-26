package com.hwido.pieceofdayfront

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.hwido.pieceofdayfront.databinding.LoginMainpageBinding
import com.hwido.pieceofdayfront.databinding.LoginSigninpageBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileOutputStream
import java.text.SimpleDateFormat


class LoginSigninpage : AppCompatActivity() {
    private lateinit var  binding : LoginSigninpageBinding

    //여기서는 프로필 사진이랑 텍스트 추가 데이터를 보낸다, 일단 비싸게 한다
    // storage 권한 처리에 필요한 변수
    companion object {
        val CAMERA = arrayOf(android.Manifest.permission.CAMERA)
        val STORAGE = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        const val CAMERA_CODE = 98
        const val STORAGE_CODE = 99
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginSigninpageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        findViewById<View>(R.id.login_signinpage_signinBtn).setOnClickListener {
            showPopup()
        }

        // 카메라
        val camera = findViewById<Button>(R.id.login_signinpage_takePicture)
        camera.setOnClickListener {
            CallCamera()
        }

        // 사진 저장
        val picture = findViewById<Button>(R.id.login_signinpage_changeProfileGalleryPicture)
        picture.setOnClickListener {
            GetAlbum()
        }

        val email : String?
        val password : String?
        val nickname = binding.loginSigninpageNickNameSigninArea.text.toString()
        val intro = binding.loginSigninpageSelfShortIntroSigninArea.text.toString()


        // 서버 api로 넣어준다
        // 넣고 다시 access 토큰 받은거로 요청해서 접근 access 토큰 받아온다
        // 여기서 다시 설정 해야할 듯
        // 로그인 계정을 바로 불러오면 좋을텐데
        // 1. 계정확인 or 재입력후 access_token 받는다
        // 2. 메인 페이지로 돌아간다 그 후 access_token 받는다

        binding.loginSigninpageSigninBtn.setOnClickListener {


            // 현재 로그인된 사용자 확인//그럼 이 계정으로 다시 받아와?
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                Toast.makeText(this, "${account.serverAuthCode}", Toast.LENGTH_LONG).show()
            }


        }


    }







    private fun showPopup() {
        val popup = LoginSigninpagePopup(this)
        popup.show()

    }

    // 카메라 권한, 저장소 권한
    // 요청 권한
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CAMERA_CODE -> {
                for (grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "카메라 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }

            STORAGE_CODE -> {
                for(grant in grantResults){
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "저장소 권한을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // 다른 권한등도 확인이 가능하도록
    fun checkPermission(permissions: Array<out String>, type:Int):Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (permission in permissions){
                if(ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
            }
        }
        return true
    }

    // 카메라 촬영 - 권한 처리
    fun CallCamera(){
        if(checkPermission(CAMERA, CAMERA_CODE) && checkPermission(STORAGE, STORAGE_CODE)){
            val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(itt, CAMERA_CODE)
        }
    }

    // 사진 저장 // 이미지를 저장하고 해당 uri를 반환한다
    fun saveFile(fileName:String, mimeType:String, bitmap: Bitmap): Uri?{

        var CV = ContentValues()

        // MediaStore 에 파일명, mimeType 을 지정
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        // 안정성 검사
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        // MediaStore 에 파일을 저장
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        if(uri != null){
            var scriptor = contentResolver.openFileDescriptor(uri, "w")

            val fos = FileOutputStream(scriptor?.fileDescriptor)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                CV.clear()
                // IS_PENDING 을 초기화
                CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, CV, null, null)
            }
        }
        return uri
    }

    // 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView = findViewById<ImageView>(R.id.individualPicture)

        if(resultCode == RESULT_OK){
            when(requestCode){
                CAMERA_CODE -> {
                    if(data?.extras?.get("data") != null){
                        val img = data?.extras?.get("data") as Bitmap
                        val uri = saveFile(RandomFileName(), "image/jpeg", img)
                        imageView.setImageURI(uri)
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    imageView.setImageURI(uri)
                }
            }
        }
    }

    // 파일명을 날짜 저장
    fun RandomFileName() : String{
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }

    // 갤러리 취득
    fun GetAlbum(){
        if(checkPermission(STORAGE, STORAGE_CODE)){
            val itt = Intent(Intent.ACTION_PICK)
            itt.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(itt, STORAGE_CODE)
        }
    }
}
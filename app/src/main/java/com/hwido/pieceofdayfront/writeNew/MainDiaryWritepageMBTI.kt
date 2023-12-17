package com.hwido.pieceofdayfront.writeNew

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hwido.pieceofdayfront.MainMainpage
import com.hwido.pieceofdayfront.ServerAPI.SpringServerAPI
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageMbtiBinding
import com.hwido.pieceofdayfront.datamodel.SendMBTI
import com.hwido.pieceofdayfront.login.LoginMainpage

class MainDiaryWritepageMBTI : AppCompatActivity() {
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


    private lateinit var binding : MainDiarywritepageMbtiBinding
    private val springServer = SpringServerAPI()
    private var iScore = 0
    private var eScore = 0
    private var sScore = 0
    private var nScore = 0
    private var tScore = 0
    private var fScore = 0
    private var jScore = 0
    private var pScore = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainDiarywritepageMbtiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 엄청난 MBTI 검사의 향연
        // 내부 로직을 통해 만든 후에  서버로 MBTI만 보내면된다
        // I vs E
        // 1번
        binding.mainDiarywritepageThirdfragmentCheckbox1.setOnClickListener {
            onCheckBoxClicked("I")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox2.setOnClickListener {
            onCheckBoxClicked("E")
        }
        // 2번
        binding.mainDiarywritepageThirdfragmentSeekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress in 0..2) {
                    iScore++
                } else if (progress in 3..5) {
                    eScore++
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        // 3번
        binding.mainDiarywritepageThirdfragmentCheckbox3.setOnClickListener {
            onCheckBoxClicked("I")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox4.setOnClickListener {
            onCheckBoxClicked("E")
        }





        
        // S vs N
        // 4번
        binding.mainDiarywritepageThirdfragmentCheckbox5.setOnClickListener {
            onCheckBoxClicked("S")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox6.setOnClickListener {
            onCheckBoxClicked("N")
        }

        // 5번
        binding.mainDiarywritepageThirdfragmentSeekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress in 0..2) {
                    sScore++
                } else if (progress in 3..5) {
                    nScore++
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        // 6번
        binding.mainDiarywritepageThirdfragmentSeekBar3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress in 0..2) {
                    sScore++
                } else if (progress in 3..5) {
                    nScore++
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        // T vs F
        // 7번
        binding.mainDiarywritepageThirdfragmentCheckbox7.setOnClickListener {
            onCheckBoxClicked("T")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox8.setOnClickListener {
            onCheckBoxClicked("F")
        }
        // 8번
        binding.mainDiarywritepageThirdfragmentCheckbox9.setOnClickListener {
            onCheckBoxClicked("T")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox10.setOnClickListener {
            onCheckBoxClicked("F")
        }
        // 9번
        binding.mainDiarywritepageThirdfragmentCheckbox11.setOnClickListener {
            onCheckBoxClicked("T")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox12.setOnClickListener {
            onCheckBoxClicked("F")
        }

        // J vs P
        // 10번
        binding.mainDiarywritepageThirdfragmentCheckbox13.setOnClickListener {
            onCheckBoxClicked("J")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox14.setOnClickListener {
            onCheckBoxClicked("P")
        }
        // 11번
        binding.mainDiarywritepageThirdfragmentCheckbox15.setOnClickListener {
            onCheckBoxClicked("J")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox16.setOnClickListener {
            onCheckBoxClicked("P")
        }
        // 12번
        binding.mainDiarywritepageThirdfragmentCheckbox17.setOnClickListener {
            onCheckBoxClicked("J")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox18.setOnClickListener {
            onCheckBoxClicked("P")
        }

        val diaryId = intent.getStringExtra("numberPost")?.toInt()!!
        val accessToken = sharedPreferences.getString(LoginMainpage.app_JWT_token, "access").toString()



        //MainActivity로 intent를 보내서 intent를 Extras 해서 있으면 두번째 fragment로 이동하게 한다
        binding.mainDiarywritepageMBTISaveBtn.setOnClickListener {
            val result = determineResult()
            Log.d("MBTI" ,"MBTI : $result")
            val diaryMBTIForm = SendMBTI(diaryId, result)
            // result를 일단 보낸다 // id 가져와야됨
            springServer.sendDiaryWithMBTI(diaryMBTIForm, accessToken)

            // 그리고 프레트 먼드로 간다
            // MainActivity로 이동하는 인텐트 생성
            val intent = Intent(this, MainMainpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("FRAGMENT_NAME", "MainWriteFragment")
            startActivity(intent)
        }
    }
    // score update 해주기
    private fun onCheckBoxClicked(type: String) {
        when (type) {
            "I" -> iScore++
            "E" -> eScore++
            "S" -> sScore++
            "N" -> nScore++
            "T" -> tScore++
            "F" -> fScore++
            "J" -> jScore++
            "P" -> pScore++
        }
    }

    // 결과값 정해주기. 3개 질문 중, 더 많은 대답 나온 쪽으로 결정된다
    private fun determineResult(): String {

        Log.d("MBTI","$iScore")
        Log.d("MBTI","$eScore")
        Log.d("MBTI","$sScore")
        Log.d("MBTI","$nScore")
        Log.d("MBTI","$tScore")
        Log.d("MBTI","$fScore")
        Log.d("MBTI","$jScore")
        Log.d("MBTI","$pScore")

        val firstPreference = if (iScore > eScore) "I" else "E"
        val secondPreference = if (sScore > nScore) "S" else "N"
        val thirdPreference = if (tScore > fScore) "T" else "F"
        val fourthPreference = if (jScore > pScore) "J" else "P"

        return "$firstPreference$secondPreference$thirdPreference$fourthPreference"


    }



}
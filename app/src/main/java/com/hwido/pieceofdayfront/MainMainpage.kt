package com.hwido.pieceofdayfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import android.content.SharedPreferences
import androidx.fragment.app.commit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.hwido.pieceofdayfront.login.LoginMainpage
import com.hwido.pieceofdayfront.myPage.MainMypageFragment

class MainMainpage : AppCompatActivity() {


    private lateinit var binding: MainMainpageBinding

    // sharepreference에  서버 토큰 저장하는것은 맞다
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMainpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment = MainListpageFragment()
        val secondFragment = MainDiaryWritepageFragment()
        val thridFragment = MainDiarySharepageFragment()
        val fourthFragment = MainMypageFragment()

        val fManager = supportFragmentManager
        fManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            add(binding.mainMainpageBaseframe.id, firstFragment)
        }


        // 하단 버튼 통한 페이지 변경
        binding.mainButtonDiaryList.setOnClickListener {
            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,firstFragment)
            }
        }


        binding.mainButtonDiaryWrite.setOnClickListener {
            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,secondFragment)
            }
        }

        binding.mainButtonDiaryShare.setOnClickListener {
            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,thridFragment)
            }
        }

        binding.mainButtonMyPage.setOnClickListener {
            fManager.commit{
                replace(binding.mainMainpageBaseframe.id,fourthFragment)
            }
        }


    }




}
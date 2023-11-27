package com.hwido.pieceofdayfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageBaseBinding

class MainDiaryWritepageBase : AppCompatActivity() {
    private lateinit var binding: MainDiarywritepageBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_diarywritepage_base)

        binding = MainDiarywritepageBaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
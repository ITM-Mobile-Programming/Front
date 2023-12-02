package com.hwido.pieceofdayfront.writeNew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.hwido.pieceofdayfront.MainDiaryWritepage
import com.hwido.pieceofdayfront.databinding.MainDiarywritepageMbtiBinding

class MainDiaryWritepageMBTI : AppCompatActivity() {
    private lateinit var binding : MainDiarywritepageMbtiBinding
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
            onCheckBoxClicked("T")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox14.setOnClickListener {
            onCheckBoxClicked("F")
        }
        // 11번
        binding.mainDiarywritepageThirdfragmentCheckbox15.setOnClickListener {
            onCheckBoxClicked("T")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox16.setOnClickListener {
            onCheckBoxClicked("F")
        }
        // 12번
        binding.mainDiarywritepageThirdfragmentCheckbox17.setOnClickListener {
            onCheckBoxClicked("T")
        }
        binding.mainDiarywritepageThirdfragmentCheckBox18.setOnClickListener {
            onCheckBoxClicked("F")
        }

        binding.mainDiarywritepageMBTISaveBtn.setOnClickListener {
            val result = determineResult()
            val intent = Intent(this, MainDiaryWritepage::class.java)
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
        return when {
            iScore > eScore -> "I"
            eScore > iScore -> "E"
            sScore > nScore -> "S"
            nScore > sScore -> "N"
            tScore > fScore -> "T"
            fScore > tScore -> "F"
            jScore > pScore -> "J"
            pScore > jScore -> "P"
            else -> "Wrong"
        }
    }
}
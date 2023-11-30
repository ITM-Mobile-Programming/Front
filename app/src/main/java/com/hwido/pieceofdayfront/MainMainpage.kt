package com.hwido.pieceofdayfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import com.hwido.pieceofdayfront.databinding.MainDiaryformatModifyBinding
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class MainMainpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_mainpage)

        // 객체 생성
        val dayText: TextView = findViewById(R.id.main_mainpage_day)
        val calendarView: CalendarView = findViewById(R.id.main_mainpage_calendar)

        // 날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        // date 타입
        val date = Date(calendarView.date)

        // 현재 날짜
        dayText.text = dateFormat.format(date)

        // 날짜 변환
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            // 날짜 변수에 담기
            val day: String = "${year}년 ${month+1}월 ${dayOfMonth}일"

            // 변수를 텍스트뷰에 담아준다
            dayText.text = day
        }

        // 하단 버튼 통한 페이지 변경
        val mainBtnWrite = findViewById<Button>(R.id.main_button_diaryWrite)
        val mainBtnShare = findViewById<Button>(R.id.main_button_diaryShare)
        val mainBtnMyPage = findViewById<Button>(R.id.main_button_myPage)

        mainBtnWrite.setOnClickListener {
            val intent = Intent(baseContext, MainDiaryWritepage::class.java)
            startActivity(intent)
        }

        mainBtnShare.setOnClickListener {
            val intent = Intent(baseContext, MainDiarySharepage::class.java)
            startActivity(intent)
        }

        mainBtnMyPage.setOnClickListener {
            val intent = Intent(baseContext, MainMypage::class.java)
            startActivity(intent)
        }

        // 이후 달력 하단에 위치할 일기들의 목록을 구현해야 한다
    }
}
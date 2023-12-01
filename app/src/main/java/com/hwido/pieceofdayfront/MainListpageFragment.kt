package com.hwido.pieceofdayfront

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import com.hwido.pieceofdayfront.databinding.MainListpageBinding
import com.hwido.pieceofdayfront.databinding.MainMainpageBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainListpageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainListpageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: MainListpageBinding
    private lateinit var mainbinding: MainMainpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // 객체 생성
        val dayText: TextView = binding.mainMainpageDay
        val calendarView: CalendarView = binding.mainMainpageCalendar

        // 날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        // date 타입
        val date = Date(calendarView.date)

        // 현재 날짜
        dayText.text = dateFormat.format(date)
        Log.d("mainpage","${dayText.text}")

        // 날짜 변환
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

            // 날짜 변수에 담기
            val day  = "${year}년 ${month+1}월 ${dayOfMonth}일"

            // 변수를 텍스트뷰에 담아준다
            dayText.text = day
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MainListpageBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainListpageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainListpageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}